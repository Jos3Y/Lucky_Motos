import { useEffect, useState } from 'react'
import DashboardCharts from '../components/DashboardCharts'
import { citasAPI, repuestosAPI, tecnicosAPI } from '../services/api'
import './Dashboard.css'

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalCitas: 0,
    citasHoy: 0,
    tecnicosDisponibles: 0,
    repuestosCriticos: 0
  })
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const loadStats = async () => {
      try {
        setLoading(true)
        setError(null)

        const [citasRes, tecnicosRes, repuestosRes] = await Promise.all([
          citasAPI.getAll().catch(() => ({ data: [] })),
          tecnicosAPI.getAll().catch(() => ({ data: [] })),
          repuestosAPI.getAll().catch(() => ({ data: [] }))
        ])

        const citas = Array.isArray(citasRes.data) ? citasRes.data : []
        const tecnicos = Array.isArray(tecnicosRes.data) ? tecnicosRes.data : []
        const repuestos = Array.isArray(repuestosRes.data) ? repuestosRes.data : []

        const hoy = new Date().toISOString().slice(0, 10)
        const totalCitas = citas.length
        const citasHoy = citas.filter((c) => c.fechaCita === hoy).length
        const tecnicosDisponibles = tecnicos.filter((t) => t.estado !== 'INACTIVO').length
        const repuestosCriticos = repuestos.filter(
          (r) => (r.estado && r.estado.toUpperCase() === 'BAJO_STOCK') || (r.stock ?? 0) <= 5
        ).length

        setStats({
          totalCitas,
          citasHoy,
          tecnicosDisponibles,
          repuestosCriticos
        })
      } catch (err) {
        console.error('Error cargando estadísticas del Dashboard', err)
        setError('No se pudieron cargar las estadísticas. Verifica el backend.')
      } finally {
        setLoading(false)
      }
    }

    loadStats()
  }, [])

  return (
    <div className="dashboard">
      <div className="dashboard-main">
        <div className="welcome-message">
          <h2>Bienvenido a Lucky Motos</h2>
          <p>Mira el resumen del taller y navega entre los módulos para seguir trabajando.</p>
        </div>

        {error && (
          <div className="dashboard-loading" style={{ color: '#ef4444' }}>
            {error}
          </div>
        )}

        <div className="dashboard-stats">
          <div className="stat-card modern">
            <div className="stat-icon">
              <span role="img" aria-label="citas">
                📅
              </span>
            </div>
            <div className="stat-info">
              <h3>{loading ? '---' : stats.totalCitas}</h3>
              <p>Total de citas registradas</p>
            </div>
          </div>

          <div className="stat-card modern">
            <div className="stat-icon">
              <span role="img" aria-label="hoy">
                ⏰
              </span>
            </div>
            <div className="stat-info">
              <h3>{loading ? '---' : stats.citasHoy}</h3>
              <p>Citas para hoy</p>
            </div>
          </div>

          <div className="stat-card modern">
            <div className="stat-icon">
              <span role="img" aria-label="tecnicos">
                👨‍🔧
              </span>
            </div>
            <div className="stat-info">
              <h3>{loading ? '---' : stats.tecnicosDisponibles}</h3>
              <p>Técnicos activos</p>
            </div>
          </div>

          <div className="stat-card modern">
            <div className="stat-icon">
              <span role="img" aria-label="stock">
                ⚠️
              </span>
            </div>
            <div className="stat-info">
              <h3>{loading ? '---' : stats.repuestosCriticos}</h3>
              <p>Repuestos en nivel crítico</p>
            </div>
          </div>
        </div>

        <DashboardCharts />
      </div>
    </div>
  )
}

export default Dashboard

