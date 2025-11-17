import { useState, useEffect, useMemo } from 'react'
import Swal from 'sweetalert2'
import { tecnicosAPI, sociosAPI, citasAPI } from '../services/api'
import './Tecnicos.css'

const initialFormData = {
  socioId: '',
  especialidad: '',
  estado: 'DISPONIBLE'
}

const HOURS = Array.from({ length: 13 }, (_, idx) => 8 + idx) // 8am - 20pm
const DAY_CONFIG = [
  { key: 'LUNES', label: 'Lun' },
  { key: 'MARTES', label: 'Mar' },
  { key: 'MIERCOLES', label: 'Mié' },
  { key: 'JUEVES', label: 'Jue' },
  { key: 'VIERNES', label: 'Vie' },
  { key: 'SABADO', label: 'Sáb' }
]

const getMonday = (date) => {
  const d = new Date(date)
  const day = d.getDay() || 7
  if (day !== 1) d.setDate(d.getDate() - (day - 1))
  d.setHours(0, 0, 0, 0)
  return d
}

const formatDate = (date) => date.toISOString().split('T')[0]

const normalizeArray = (payload) => {
  if (Array.isArray(payload)) return payload
  if (Array.isArray(payload?.content)) return payload.content
  if (Array.isArray(payload?.data)) return payload.data
  return []
}

const Tecnicos = () => {
  const [tecnicos, setTecnicos] = useState([])
  const [socios, setSocios] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState(initialFormData)
  const [editingTecnico, setEditingTecnico] = useState(null)
  const [selectedTecnico, setSelectedTecnico] = useState(null)
  const [citasTecnico, setCitasTecnico] = useState([])
  const [slotModal, setSlotModal] = useState(null)
  const [weekStart] = useState(getMonday(new Date()))
  const [showAgendaModal, setShowAgendaModal] = useState(false)

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      const [tecRes, socioRes] = await Promise.all([
        tecnicosAPI.getAll(),
        sociosAPI.getAll()
      ])
      setTecnicos(normalizeArray(tecRes.data))
      setSocios(normalizeArray(socioRes.data))
    } catch (error) {
      console.error('Error cargando datos de técnicos:', error)
      Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo cargar la información', confirmButtonColor: '#2c3e50' })
    } finally {
      setLoading(false)
    }
  }

  const handleSelectTecnico = async (tecnico) => {
    setSelectedTecnico(tecnico)
    setShowAgendaModal(true)
    try {
      const response = await citasAPI.getByTecnico(tecnico.id)
      setCitasTecnico(response.data || [])
    } catch (error) {
      console.error('Error cargando agenda del técnico:', error)
      setCitasTecnico([])
    }
  }

  const assignedSocios = useMemo(() => new Set(tecnicos.map(t => t.socioId)), [tecnicos])

  const availableSocios = socios.filter(s => !assignedSocios.has(s.id) || (editingTecnico && editingTecnico.socioId === s.id))

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!editingTecnico && !formData.socioId) {
      Swal.fire({ icon: 'warning', title: 'Seleccione un socio', confirmButtonColor: '#2c3e50' })
      return
    }
    try {
      if (editingTecnico) {
        await tecnicosAPI.update(editingTecnico.id, formData)
        Swal.fire({ icon: 'success', title: 'Técnico actualizado', confirmButtonColor: '#2c3e50' })
      } else {
        await tecnicosAPI.create(formData)
        Swal.fire({ icon: 'success', title: 'Técnico creado', confirmButtonColor: '#2c3e50' })
      }
      setShowForm(false)
      setEditingTecnico(null)
      setFormData(initialFormData)
      loadData()
    } catch (error) {
      Swal.fire({ icon: 'error', title: 'Error', text: error.response?.data?.message || error.message, confirmButtonColor: '#2c3e50' })
    }
  }

  const handleDeactivate = async (tecnico) => {
    const result = await Swal.fire({
      icon: 'warning',
      title: 'Desactivar técnico',
      text: `¿Desea desactivar a ${tecnico.nombre}?`,
      showCancelButton: true,
      confirmButtonText: 'Sí, desactivar',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#e67e22',
      cancelButtonColor: '#6c757d'
    })
    if (!result.isConfirmed) return
    try {
      await tecnicosAPI.changeEstado(tecnico.id, 'INACTIVO')
      Swal.fire({ icon: 'success', title: 'Técnico desactivado', confirmButtonColor: '#2c3e50' })
      loadData()
    } catch (error) {
      Swal.fire({ icon: 'error', title: 'Error', text: error.response?.data?.message || error.message, confirmButtonColor: '#2c3e50' })
    }
  }

  const handleDelete = async (tecnico) => {
    const result = await Swal.fire({
      icon: 'warning',
      title: 'Eliminar técnico',
      text: `¿Desea eliminar el técnico ${tecnico.nombre}?`,
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#e74c3c',
      cancelButtonColor: '#6c757d'
    })
    if (!result.isConfirmed) return
    try {
      await tecnicosAPI.delete(tecnico.id)
      Swal.fire({ icon: 'success', title: 'Técnico eliminado', confirmButtonColor: '#2c3e50' })
      if (selectedTecnico?.id === tecnico.id) {
        setSelectedTecnico(null)
        setCitasTecnico([])
      }
      loadData()
    } catch (error) {
      Swal.fire({ icon: 'error', title: 'Error', text: error.response?.data?.message || error.message, confirmButtonColor: '#2c3e50' })
    }
  }

  const handleResetHorarios = async () => {
    const result = await Swal.fire({
      icon: 'question',
      title: 'Restablecer horarios',
      text: 'Esto reasignará el horario 8am-8pm de lunes a sábado para todos los técnicos. ¿Continuar?',
      showCancelButton: true,
      confirmButtonText: 'Sí, restablecer',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#2c3e50'
    })
    if (!result.isConfirmed) return
    try {
      await tecnicosAPI.resetHorario()
      Swal.fire({ icon: 'success', title: 'Horarios actualizados', confirmButtonColor: '#2c3e50' })
      loadData()
      if (selectedTecnico) {
        handleSelectTecnico(selectedTecnico)
      }
    } catch (error) {
      Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo restablecer los horarios', confirmButtonColor: '#2c3e50' })
    }
  }

  const weekDays = useMemo(() => {
    return DAY_CONFIG.map((day, index) => {
      const date = new Date(weekStart)
      date.setDate(date.getDate() + index)
      return { ...day, date, dateKey: formatDate(date) }
    })
  }, [weekStart])

  const scheduleMatrix = useMemo(() => {
    const busyMap = new Map()
    const start = weekDays[0]?.date
    const end = weekDays[weekDays.length - 1]?.date
    const startTime = start ? new Date(start) : null
    const endTime = end ? new Date(end) : null

    citasTecnico.forEach(cita => {
      if (!cita.fechaCita || !cita.horaCita) return
      const citaDate = new Date(cita.fechaCita + 'T00:00:00')
      if (startTime && endTime && (citaDate < startTime || citaDate > endTime)) {
        return
      }
      const hour = parseInt(cita.horaCita.split(':')[0], 10)
      const key = `${cita.fechaCita}-${hour}`
      busyMap.set(key, cita)
    })

    return weekDays.map(day => ({
      ...day,
      slots: HOURS.map(hour => {
        const key = `${day.dateKey}-${hour}`
        const cita = busyMap.get(key)
        return {
          hour,
          displayHour: `${hour.toString().padStart(2, '0')}:00`,
          status: cita ? 'busy' : 'available',
          cita,
          date: day.date,
          dateKey: day.dateKey
        }
      })
    }))
  }, [weekDays, citasTecnico])

  const getEstadoBadgeClass = (estado) => {
    const estados = {
      DISPONIBLE: 'badge-success',
      OCUPADO: 'badge-danger',
      INACTIVO: 'badge-secondary'
    }
    return estados[estado] || 'badge-secondary'
  }

  if (loading) {
    return <div className="loading">Cargando técnicos...</div>
  }

  return (
    <div className="tecnicos-page">
      <div className="page-header">
        <div>
          <h1>Gestión de Técnicos</h1>
          <p className="subtitle">Administra tus técnicos, horarios y agenda semanal</p>
        </div>
        <div className="header-actions">
          <button className="btn-secondary" onClick={handleResetHorarios}>Restablecer horarios</button>
          <button
            className="btn-primary"
            onClick={() => {
              setFormData(initialFormData)
              setEditingTecnico(null)
              setShowForm(true)
            }}
          >
            Agregar técnico
          </button>
        </div>
      </div>

      <div className="tecnicos-table-card full-width">
        <table className="tecnicos-table">
            <thead>
              <tr>
                <th>Técnico</th>
                <th>Especialidad</th>
                <th>Correo</th>
                <th>Teléfono</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {tecnicos.length === 0 ? (
                <tr>
                  <td colSpan={6} className="no-data">No hay técnicos registrados</td>
                </tr>
              ) : (
                tecnicos.map(tecnico => (
                  <tr key={tecnico.id} className={selectedTecnico?.id === tecnico.id ? 'row-selected' : ''}>
                    <td>{tecnico.nombre} {tecnico.apellidos}</td>
                    <td>{tecnico.especialidad || '-'}</td>
                    <td>{tecnico.correo || '-'}</td>
                    <td>{tecnico.telefono || '-'}</td>
                    <td>
                      <span className={`badge ${getEstadoBadgeClass(tecnico.estado)}`}>
                        {tecnico.estado}
                      </span>
                    </td>
                    <td>
                      <div className="table-actions">
                        <button className="btn-link" onClick={() => handleSelectTecnico(tecnico)}>Agenda</button>
                        <button className="btn-link" onClick={() => {
                          setEditingTecnico(tecnico)
                          setFormData({
                            socioId: tecnico.socioId,
                            especialidad: tecnico.especialidad || '',
                            estado: tecnico.estado
                          })
                          setShowForm(true)
                        }}>Editar</button>
                        <button className="btn-link" onClick={() => handleDeactivate(tecnico)}>Desactivar</button>
                        <button className="btn-link danger" onClick={() => handleDelete(tecnico)}>Eliminar</button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
        </table>
      </div>
      {showAgendaModal && selectedTecnico && (
        <div className="modal-overlay" onClick={() => setShowAgendaModal(false)}>
          <div className="modal agenda-modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <div>
                <h3>Agenda semanal</h3>
                <p>{selectedTecnico.nombre} {selectedTecnico.apellidos} · {selectedTecnico.especialidad || 'Sin especialidad'}</p>
              </div>
              <button className="close-modal" onClick={() => setShowAgendaModal(false)}>✕</button>
            </div>
            <div className="agenda-table-wrapper">
              <table className="agenda-table">
                <thead>
                  <tr>
                    <th>Hora</th>
                    {scheduleMatrix.map(day => (
                      <th key={day.key}>{day.label}<br /><span>{day.dateKey.slice(5)}</span></th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {HOURS.map(hour => (
                    <tr key={hour}>
                      <td className="hour-col">{`${hour.toString().padStart(2, '0')}:00`}</td>
                      {scheduleMatrix.map(day => {
                        const slot = day.slots.find(s => s.hour === hour)
                        const classes = slot?.status === 'busy' ? 'slot busy' : 'slot available'
                        return (
                          <td
                            key={`${day.key}-${hour}`}
                            className={classes}
                            onClick={() => {
                              if (slot?.status === 'busy') {
                                setSlotModal(slot.cita)
                              }
                            }}
                          >
                            {slot?.status === 'busy' ? 'Ocupado' : ''}
                          </td>
                        )
                      })}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="legend">
              <span><span className="legend-dot available" /> Disponible</span>
              <span><span className="legend-dot busy" /> Ocupado</span>
            </div>
          </div>
        </div>
      )}

      {showForm && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>{editingTecnico ? 'Editar técnico' : 'Nuevo técnico'}</h3>
              <button className="close-modal" onClick={() => { setShowForm(false); setEditingTecnico(null) }}>✕</button>
            </div>
            <form onSubmit={handleSubmit} className="tecnico-form">
              {!editingTecnico && (
                <div className="form-group">
                  <label>Socio</label>
                  <select
                    value={formData.socioId}
                    onChange={(e) => setFormData({ ...formData, socioId: Number(e.target.value) })}
                    required
                  >
                    <option value="">Seleccione un socio</option>
                    {availableSocios.map(socio => (
                      <option key={socio.id} value={socio.id}>
                        {socio.nombre} {socio.apellidos} - {socio.correo}
                      </option>
                    ))}
                  </select>
                </div>
              )}
              <div className="form-group">
                <label>Especialidad</label>
                <input
                  type="text"
                  value={formData.especialidad}
                  onChange={(e) => setFormData({ ...formData, especialidad: e.target.value })}
                  placeholder="Ej: Mecánica general"
                  required
                />
              </div>
              <div className="form-group">
                <label>Estado</label>
                <select
                  value={formData.estado}
                  onChange={(e) => setFormData({ ...formData, estado: e.target.value })}
                >
                  <option value="DISPONIBLE">Disponible</option>
                  <option value="OCUPADO">Ocupado</option>
                  <option value="INACTIVO">Inactivo</option>
                </select>
              </div>
              <div className="modal-actions">
                <button type="button" className="btn-secondary" onClick={() => { setShowForm(false); setEditingTecnico(null) }}>Cancelar</button>
                <button type="submit" className="btn-primary">Guardar</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {slotModal && (
        <div className="modal-overlay" onClick={() => setSlotModal(null)}>
          <div className="modal slot-modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Detalle de cita</h3>
              <button className="close-modal" onClick={() => setSlotModal(null)}>✕</button>
            </div>
            <div className="slot-details">
              <p><strong>Cliente:</strong> {slotModal.cliente?.nombre} {slotModal.cliente?.apellidos}</p>
              <p><strong>Fecha:</strong> {slotModal.fechaCita} {slotModal.horaCita}</p>
              <p><strong>Servicio:</strong> {slotModal.tipoServicio?.nombre || 'General'}</p>
              <p><strong>Observaciones:</strong> {slotModal.observaciones || 'Sin observaciones'}</p>
              <p><strong>Estado:</strong> {slotModal.estado}</p>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default Tecnicos

