import { useEffect, useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { citasAPI, tecnicosAPI } from '../services/api'
import Swal from 'sweetalert2'
import './DashboardTecnico.css'

const DashboardTecnico = () => {
  const { user } = useAuth()
  const [citasHoy, setCitasHoy] = useState([])
  const [tecnicoId, setTecnicoId] = useState(null)
  const [loading, setLoading] = useState(true)
  const [stats, setStats] = useState({
    totalCitas: 0,
    citasHoy: 0,
    pendientes: 0,
    enProceso: 0,
    completadas: 0
  })

  useEffect(() => {
    if (user?.id) {
      loadTecnicoId()
    }
  }, [user])

  useEffect(() => {
    if (tecnicoId) {
      loadCitasTecnico()
    }
  }, [tecnicoId])

  const loadTecnicoId = async () => {
    try {
      console.log('🔍 Buscando técnico para socio ID:', user.id)
      const response = await tecnicosAPI.getBySocioId(user.id)
      console.log('📋 Respuesta técnico:', response.data)
      
      if (response.data?.id) {
        console.log('✅ Técnico encontrado con ID:', response.data.id)
        setTecnicoId(response.data.id)
      } else {
        console.warn('⚠️ Usuario técnico no tiene registro en tabla técnico')
        // Mostrar dashboard vacío sin error
        setStats({
          totalCitas: 0,
          citasHoy: 0,
          pendientes: 0,
          enProceso: 0,
          completadas: 0
        })
        setCitasHoy([])
        setLoading(false)
      }
    } catch (error) {
      console.error('❌ Error cargando técnico:', error)
      console.error('❌ Error response:', error.response)
      // Si es 404, el usuario técnico no tiene registro en tabla técnico
      // Mostrar dashboard vacío sin error
      if (error.response?.status === 404) {
        console.warn('⚠️ Técnico no encontrado (404)')
        setStats({
          totalCitas: 0,
          citasHoy: 0,
          pendientes: 0,
          enProceso: 0,
          completadas: 0
        })
        setCitasHoy([])
      } else {
        // Solo mostrar error para otros tipos de errores
        Swal.fire({
          icon: 'warning',
          title: 'Aviso',
          text: 'No se encontró tu registro como técnico. Contacta al administrador.',
          confirmButtonColor: '#2c3e50'
        })
      }
      setLoading(false)
    }
  }

  const loadCitasTecnico = async () => {
    try {
      setLoading(true)
      console.log('🔍 Cargando citas para técnico ID:', tecnicoId)
      
      const [hoyRes, todasRes] = await Promise.all([
        citasAPI.getHoyPorTecnico(tecnicoId).catch((err) => {
          console.error('Error en getHoyPorTecnico:', err)
          return { data: [] }
        }),
        citasAPI.getByTecnico(tecnicoId).catch((err) => {
          console.error('Error en getByTecnico:', err)
          return { data: [] }
        })
      ])

      console.log('📊 Respuesta getHoyPorTecnico:', hoyRes.data)
      console.log('📊 Respuesta getByTecnico:', todasRes.data)

      const citasHoyData = Array.isArray(hoyRes.data) ? hoyRes.data : []
      const todasCitas = Array.isArray(todasRes.data) ? todasRes.data : []

      console.log('✅ Citas de hoy:', citasHoyData.length)
      console.log('✅ Total citas:', todasCitas.length)

      setCitasHoy(citasHoyData)
      setStats({
        totalCitas: todasCitas.length,
        citasHoy: citasHoyData.length,
        pendientes: todasCitas.filter(c => c.estado === 'PENDIENTE').length,
        enProceso: todasCitas.filter(c => c.estado === 'EN_PROCESO').length,
        completadas: todasCitas.filter(c => c.estado === 'COMPLETADA').length
      })
    } catch (error) {
      console.error('❌ Error cargando citas:', error)
      console.error('❌ Error response:', error.response)
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: error.response?.data?.message || 'No se pudieron cargar las citas',
        confirmButtonColor: '#2c3e50'
      })
    } finally {
      setLoading(false)
    }
  }

  const handleCambiarEstado = async (citaId, nuevoEstado) => {
    try {
      await citasAPI.updateEstado(citaId, {
        estado: nuevoEstado,
        motivoEstado: `Estado actualizado por el técnico`
      })
      Swal.fire('Actualizado', 'Estado de la cita actualizado', 'success')
      loadCitasTecnico()
    } catch (error) {
      Swal.fire('Error', 'No se pudo actualizar el estado', 'error')
    }
  }

  const getEstadoColor = (estado) => {
    switch (estado) {
      case 'PENDIENTE': return '#f59e0b'
      case 'CONFIRMADA': return '#3b82f6'
      case 'EN_PROCESO': return '#8b5cf6'
      case 'COMPLETADA': return '#10b981'
      case 'CANCELADA': return '#ef4444'
      default: return '#6b7280'
    }
  }

  const getEstadoLabel = (estado) => {
    switch (estado) {
      case 'PENDIENTE': return 'Pendiente'
      case 'CONFIRMADA': return 'Confirmada'
      case 'EN_PROCESO': return 'En Proceso'
      case 'COMPLETADA': return 'Completada'
      case 'CANCELADA': return 'Cancelada'
      default: return estado
    }
  }

  if (loading) {
    return <div className="dashboard-tecnico-loading">Cargando...</div>
  }

  return (
    <div className="dashboard-tecnico">
      <div className="dashboard-tecnico-header">
        <h1>Mi Dashboard</h1>
        <p>Gestiona tus citas asignadas</p>
      </div>

      <div className="dashboard-tecnico-stats">
        <div className="stat-card">
          <div className="stat-icon">📅</div>
          <div className="stat-content">
            <h3>{stats.totalCitas}</h3>
            <p>Total de citas</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">⏰</div>
          <div className="stat-content">
            <h3>{stats.citasHoy}</h3>
            <p>Citas de hoy</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">⏳</div>
          <div className="stat-content">
            <h3>{stats.pendientes}</h3>
            <p>Pendientes</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">🔧</div>
          <div className="stat-content">
            <h3>{stats.enProceso}</h3>
            <p>En proceso</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">✅</div>
          <div className="stat-content">
            <h3>{stats.completadas}</h3>
            <p>Completadas</p>
          </div>
        </div>
      </div>

      <div className="dashboard-tecnico-citas">
        <h2>Citas de hoy</h2>
        {citasHoy.length === 0 ? (
          <div className="no-citas">No tienes citas programadas para hoy</div>
        ) : (
          <div className="citas-list">
            {citasHoy.map((cita) => (
              <div key={cita.id} className="cita-card">
                <div className="cita-header">
                  <div className="cita-info">
                    <h3>{cita.cliente?.nombreCompleto || 'Cliente'}</h3>
                    <p className="cita-moto">{cita.moto?.marca} {cita.moto?.modelo} - {cita.moto?.placa}</p>
                    <p className="cita-servicio">{cita.tipoServicio?.nombre}</p>
                  </div>
                  <div className="cita-estado-badge" style={{ backgroundColor: getEstadoColor(cita.estado) }}>
                    {getEstadoLabel(cita.estado)}
                  </div>
                </div>
                <div className="cita-details">
                  <p><strong>Fecha:</strong> {cita.fechaCita} a las {cita.horaCita}</p>
                  {cita.observaciones && <p><strong>Observaciones:</strong> {cita.observaciones}</p>}
                </div>
                <div className="cita-actions">
                  {cita.estado === 'PENDIENTE' && (
                    <button
                      className="btn-estado btn-confirmar"
                      onClick={() => handleCambiarEstado(cita.id, 'CONFIRMADA')}
                    >
                      Confirmar
                    </button>
                  )}
                  {cita.estado === 'CONFIRMADA' && (
                    <button
                      className="btn-estado btn-proceso"
                      onClick={() => handleCambiarEstado(cita.id, 'EN_PROCESO')}
                    >
                      Iniciar
                    </button>
                  )}
                  {cita.estado === 'EN_PROCESO' && (
                    <button
                      className="btn-estado btn-completar"
                      onClick={() => handleCambiarEstado(cita.id, 'COMPLETADA')}
                    >
                      Completar
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default DashboardTecnico

