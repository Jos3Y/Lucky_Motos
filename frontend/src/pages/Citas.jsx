import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import { citasAPI, motosAPI, tecnicosAPI, tiposServicioAPI } from '../services/api'
import './Citas.css'

const Citas = () => {
  const { user, hasRole } = useAuth()
  const [citas, setCitas] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    clienteId: user?.id || null,
    motoId: '',
    tecnicoId: '',
    tipoServicioId: '',
    fechaCita: '',
    horaCita: '',
    pagoInicial: false,
    montoPagoInicial: 0,
    observaciones: ''
  })
  const [motos, setMotos] = useState([])
  const [tecnicos, setTecnicos] = useState([])
  const [tiposServicio, setTiposServicio] = useState([])

  // Actualizar clienteId cuando user cambie
  useEffect(() => {
    if (user?.id) {
      setFormData(prev => ({ ...prev, clienteId: user.id }))
    }
  }, [user])

  useEffect(() => {
    loadCitas()
    if (showForm) {
      loadFormData()
    }
  }, [showForm])

  const loadCitas = async () => {
    try {
      let response
      if (hasRole('CLIENTE') && user?.id) {
        response = await citasAPI.getByCliente(user.id)
      } else {
        response = await citasAPI.getAll()
      }
      setCitas(response.data)
    } catch (error) {
      console.error('Error cargando citas:', error)
    } finally {
      setLoading(false)
    }
  }

  const loadFormData = async () => {
    try {
      const [motosRes, tecnicosRes, tiposRes] = await Promise.all([
        motosAPI.getAll(),
        tecnicosAPI.getAll(),
        tiposServicioAPI.getAll()
      ])
      setMotos(motosRes.data)
      setTecnicos(tecnicosRes.data)
      setTiposServicio(tiposRes.data)
    } catch (error) {
      console.error('Error cargando datos del formulario:', error)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await citasAPI.create(formData)
      setShowForm(false)
      loadCitas()
      alert('Cita creada exitosamente')
    } catch (error) {
      alert('Error al crear la cita: ' + (error.response?.data?.message || error.message))
    }
  }

  const getEstadoBadgeClass = (estado) => {
    const estados = {
      PENDIENTE: 'badge-warning',
      CONFIRMADA: 'badge-info',
      EN_PROCESO: 'badge-primary',
      COMPLETADA: 'badge-success',
      CANCELADA: 'badge-danger'
    }
    return estados[estado] || 'badge-secondary'
  }

  if (loading) {
    return <div className="loading">Cargando citas...</div>
  }

  return (
    <div className="citas-page">
      <div className="page-header">
        <h1>Gestión de Citas</h1>
        {(hasRole('ADMIN') || hasRole('RECEPCIONISTA') || hasRole('CLIENTE')) && (
          <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
            {showForm ? 'Cancelar' : 'Registrar nueva cita'}
          </button>
        )}
      </div>

      {showForm && (
        <div className="cita-form-container">
          <h2>Registrar nueva cita</h2>
          <form onSubmit={handleSubmit} className="cita-form">
            <div className="form-row">
              <div className="form-group">
                <label>Nombre completo</label>
                <input type="text" value={user?.correo || ''} disabled />
              </div>
              <div className="form-group">
                <label>Teléfono / WhatsApp</label>
                <input type="tel" placeholder="Ingrese su teléfono" />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Moto</label>
                <select
                  value={formData.motoId}
                  onChange={(e) => setFormData({ ...formData, motoId: e.target.value })}
                  required
                >
                  <option value="">- Seleccione -</option>
                  {motos.map(moto => (
                    <option key={moto.id} value={moto.id}>
                      {moto.marca} {moto.modelo} - {moto.placa}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Técnico asignado</label>
                <select
                  value={formData.tecnicoId}
                  onChange={(e) => setFormData({ ...formData, tecnicoId: e.target.value })}
                >
                  <option value="">- Seleccione -</option>
                  {tecnicos.map(tecnico => (
                    <option key={tecnico.id} value={tecnico.id}>
                      {tecnico.nombre} {tecnico.apellidos}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Fecha</label>
                <input
                  type="date"
                  value={formData.fechaCita}
                  onChange={(e) => setFormData({ ...formData, fechaCita: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Hora</label>
                <input
                  type="time"
                  value={formData.horaCita}
                  onChange={(e) => setFormData({ ...formData, horaCita: e.target.value })}
                  required
                />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Tipo de servicio</label>
                <select
                  value={formData.tipoServicioId}
                  onChange={(e) => setFormData({ ...formData, tipoServicioId: e.target.value })}
                >
                  <option value="">- Seleccione -</option>
                  {tiposServicio.map(tipo => (
                    <option key={tipo.id} value={tipo.id}>
                      {tipo.nombre}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Pago inicial</label>
                <div className="radio-group">
                  <label>
                    <input
                      type="radio"
                      checked={formData.pagoInicial}
                      onChange={() => setFormData({ ...formData, pagoInicial: true })}
                    />
                    Sí
                  </label>
                  <label>
                    <input
                      type="radio"
                      checked={!formData.pagoInicial}
                      onChange={() => setFormData({ ...formData, pagoInicial: false })}
                    />
                    No
                  </label>
                </div>
              </div>
            </div>
            {formData.pagoInicial && (
              <div className="form-group">
                <label>Monto pago inicial</label>
                <input
                  type="number"
                  step="0.01"
                  value={formData.montoPagoInicial}
                  onChange={(e) => setFormData({ ...formData, montoPagoInicial: parseFloat(e.target.value) })}
                />
              </div>
            )}
            <div className="form-actions">
              <button type="submit" className="btn-primary">Guardar cita</button>
              <button type="button" className="btn-secondary" onClick={() => setShowForm(false)}>
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="citas-table-container">
        <h2>Citas previas</h2>
        <table className="citas-table">
          <thead>
            <tr>
              <th>Cliente</th>
              <th>Moto</th>
              <th>Técnico</th>
              <th>Fecha</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {citas.length === 0 ? (
              <tr>
                <td colSpan="6" className="no-data">No hay citas registradas</td>
              </tr>
            ) : (
              citas.map(cita => (
                <tr key={cita.id}>
                  <td>{cita.cliente?.nombre} {cita.cliente?.apellidos}</td>
                  <td>{cita.moto?.marca} {cita.moto?.modelo}</td>
                  <td>{cita.tecnico?.nombre || 'Sin asignar'}</td>
                  <td>{cita.fechaCita} {cita.horaCita}</td>
                  <td>
                    <span className={`badge ${getEstadoBadgeClass(cita.estado)}`}>
                      {cita.estado}
                    </span>
                  </td>
                  <td>
                    <button className="btn-link">Ver</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default Citas

