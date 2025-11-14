import { useEffect, useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { citasAPI, repuestosAPI, tecnicosAPI } from '../services/api'
import './Dashboard.css'

const Dashboard = () => {
  const { user, hasRole } = useAuth()
  const [stats, setStats] = useState({
    citasPendientes: 0,
    citasHoy: 0,
    repuestosBajoStock: 0,
    tecnicosDisponibles: 0
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadStats()
  }, [])

  const loadStats = async () => {
    try {
      const [citasRes, repuestosRes, tecnicosRes] = await Promise.all([
        citasAPI.getByEstado('PENDIENTE').catch(() => ({ data: [] })),
        repuestosAPI.getBajoStock().catch(() => ({ data: [] })),
        tecnicosAPI.getDisponibles().catch(() => ({ data: [] }))
      ])

      const hoy = new Date().toISOString().split('T')[0]
      const citasHoy = citasRes.data.filter(c => c.fechaCita === hoy).length

      setStats({
        citasPendientes: citasRes.data.length,
        citasHoy,
        repuestosBajoStock: repuestosRes.data.length,
        tecnicosDisponibles: tecnicosRes.data.length
      })
    } catch (error) {
      console.error('Error cargando estadísticas:', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return <div className="dashboard-loading">Cargando...</div>
  }

  return (
    <div className="dashboard">
      <h1>Dashboard</h1>
      
      <div className="dashboard-stats">
        {hasRole('ADMIN') || hasRole('RECEPCIONISTA') || hasRole('TECNICO') ? (
          <>
            <div className="stat-card">
              <div className="stat-icon">📅</div>
              <div className="stat-info">
                <h3>{stats.citasPendientes}</h3>
                <p>Citas Pendientes</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon">📆</div>
              <div className="stat-info">
                <h3>{stats.citasHoy}</h3>
                <p>Citas Hoy</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon">📦</div>
              <div className="stat-info">
                <h3>{stats.repuestosBajoStock}</h3>
                <p>Repuestos Bajo Stock</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon">👷</div>
              <div className="stat-info">
                <h3>{stats.tecnicosDisponibles}</h3>
                <p>Técnicos Disponibles</p>
              </div>
            </div>
          </>
        ) : (
          <div className="welcome-message">
            <h2>Bienvenido, {user?.correo}</h2>
            <p>Gestiona tus citas y servicios desde aquí</p>
          </div>
        )}
      </div>
    </div>
  )
}

export default Dashboard

