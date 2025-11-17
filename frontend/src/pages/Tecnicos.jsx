import { useState, useEffect, useMemo } from 'react'
import Swal from 'sweetalert2'
import { tecnicosAPI, sociosAPI, citasAPI, tiposServicioAPI, rolSocioAPI } from '../services/api'
import './Tecnicos.css'

const initialFormData = {
  socioId: '',
  especialidad: '',
  estado: 'DISPONIBLE',
  nombre: '',
  apellidos: '',
  correo: '',
  telefono: '',
  dni: '',
  genero: 'MASCULINO',
  estadoCivil: 'SOLTERO',
  contrasena: '',
  rol: 'ROLE_TECNICO'
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
  const [tiposServicio, setTiposServicio] = useState([])
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
      const [tecRes, socioRes, tipoRes] = await Promise.all([
        tecnicosAPI.getAll(),
        sociosAPI.getAll(),
        tiposServicioAPI.getAll()
      ])
      setTecnicos(normalizeArray(tecRes.data))
      setSocios(normalizeArray(socioRes.data))
      setTiposServicio(normalizeArray(tipoRes.data))
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

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!formData.especialidad) {
      Swal.fire({ icon: 'warning', title: 'Seleccione una especialidad', confirmButtonColor: '#2c3e50' })
      return
    }
    if (!editingTecnico && (!formData.contrasena || formData.contrasena.length < 6)) {
      Swal.fire({ icon: 'warning', title: 'Defina una contraseña (mínimo 6 caracteres)', confirmButtonColor: '#2c3e50' })
      return
    }
    try {
      const socioPayload = {
        nombre: formData.nombre,
        apellidos: formData.apellidos,
        correo: formData.correo,
        telefono: formData.telefono,
        dni: formData.dni,
        genero: formData.genero,
        estadoCivil: formData.estadoCivil
      }
      let socioId = formData.socioId

      if (editingTecnico) {
        if (formData.contrasena) {
          socioPayload.contrasena = formData.contrasena
        }
        await sociosAPI.update(editingTecnico.socioId, socioPayload)
        await tecnicosAPI.update(editingTecnico.id, { ...formData, socioId: editingTecnico.socioId })
        socioId = editingTecnico.socioId
        Swal.fire({ icon: 'success', title: 'Técnico actualizado', confirmButtonColor: '#2c3e50' })
      } else {
        const resp = await sociosAPI.create({ ...socioPayload, contrasena: formData.contrasena })
        socioId = resp.data?.idSocio ?? resp.data?.id ?? resp.data?.socioId
        await tecnicosAPI.create({ ...formData, socioId })
        Swal.fire({ icon: 'success', title: 'Técnico creado', confirmButtonColor: '#2c3e50' })
      }

      if (formData.rol && formData.correo) {
        await rolSocioAPI.assign({
          correoSocio: formData.correo,
          nombreRol: formData.rol,
          descripcion: 'Asignado desde gestión de técnicos',
          estado: 'ACTIVO'
        })
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
                          const socio = socios.find(s => (s.id ?? s.idSocio) === tecnico.socioId)
                          setFormData({
                            socioId: tecnico.socioId,
                            especialidad: tecnico.especialidad || '',
                            estado: tecnico.estado,
                            nombre: socio?.nombres || socio?.nombre || tecnico.nombre || '',
                            apellidos: socio?.apellidos || tecnico.apellidos || '',
                            correo: socio?.correo || tecnico.correo || '',
                            telefono: socio?.telefono || tecnico.telefono || '',
                            dni: socio?.dni || '',
                            genero: socio?.genero || 'MASCULINO',
                            estadoCivil: socio?.estadoCivil || 'SOLTERO',
                            contrasena: '',
                            rol: 'ROLE_TECNICO'
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
              <div className="form-row">
                <div className="form-group">
                  <label>Nombres</label>
                  <input
                    type="text"
                    value={formData.nombre}
                    onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                    placeholder="Nombres completos"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Apellidos</label>
                  <input
                    type="text"
                    value={formData.apellidos}
                    onChange={(e) => setFormData({ ...formData, apellidos: e.target.value })}
                    placeholder="Apellidos"
                    required
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>DNI</label>
                  <input
                    type="text"
                    value={formData.dni}
                    onChange={(e) => setFormData({ ...formData, dni: e.target.value })}
                    placeholder="Documento"
                  />
                </div>
                <div className="form-group">
                  <label>Género</label>
                  <select
                    value={formData.genero}
                    onChange={(e) => setFormData({ ...formData, genero: e.target.value })}
                  >
                    <option value="MASCULINO">Masculino</option>
                    <option value="FEMENINO">Femenino</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Estado civil</label>
                  <select
                    value={formData.estadoCivil}
                    onChange={(e) => setFormData({ ...formData, estadoCivil: e.target.value })}
                  >
                    <option value="SOLTERO">Soltero</option>
                    <option value="CASADO">Casado</option>
                    <option value="DIVORCIADO">Divorciado</option>
                    <option value="VIUDO">Viudo</option>
                  </select>
                </div>
              </div>
              {!editingTecnico && (
                <div className="form-group">
                  <label>Contraseña</label>
                  <input
                    type="password"
                    value={formData.contrasena}
                    onChange={(e) => setFormData({ ...formData, contrasena: e.target.value })}
                    placeholder="Contraseña para el usuario"
                    required={!editingTecnico}
                  />
                </div>
              )}
              <div className="form-row">
                <div className="form-group">
                  <label>Correo</label>
                  <input
                    type="email"
                    value={formData.correo}
                    onChange={(e) => setFormData({ ...formData, correo: e.target.value })}
                    placeholder="correo@ejemplo.com"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Teléfono</label>
                  <input
                    type="tel"
                    value={formData.telefono}
                    onChange={(e) => setFormData({ ...formData, telefono: e.target.value })}
                    placeholder="999999999"
                    required
                  />
                </div>
              </div>
              <div className="form-group">
                <label>Especialidad</label>
                <select
                  value={formData.especialidad}
                  onChange={(e) => setFormData({ ...formData, especialidad: e.target.value })}
                  required
                >
                  <option value="">Seleccione una especialidad</option>
                  {tiposServicio.map(tipo => (
                    <option key={tipo.id || tipo.id_tipo_servicio} value={tipo.nombre}>
                      {tipo.nombre}
                    </option>
                  ))}
                </select>
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
              <div className="form-group">
                <label>Rol del usuario</label>
                <select
                  value={formData.rol}
                  onChange={(e) => setFormData({ ...formData, rol: e.target.value })}
                >
                  <option value="ROLE_TECNICO">Técnico</option>
                  <option value="ROLE_RECEPCIONISTA">Recepcionista</option>
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

