import { useState } from 'react'
import './Reportes.css'

const Reportes = () => {
  const [filters, setFilters] = useState({
    periodo: 'ULTIMOS_6_MESES',
    tecnico: 'TODOS',
    marca: 'TODAS'
  })

  return (
    <div className="reportes-page">
      <h1>Reportes</h1>

      <div className="reportes-grid">
        {/* Mejores trabajadores */}
        <div className="reporte-card">
          <h2>Mejores trabajadores</h2>
          <div className="filters">
            <select
              value={filters.periodo}
              onChange={(e) => setFilters({ ...filters, periodo: e.target.value })}
            >
              <option value="ULTIMOS_6_MESES">Últimos 6 meses</option>
              <option value="ULTIMOS_3_MESES">Últimos 3 meses</option>
              <option value="ULTIMO_MES">Último mes</option>
            </select>
            <select
              value={filters.tecnico}
              onChange={(e) => setFilters({ ...filters, tecnico: e.target.value })}
            >
              <option value="TODOS">Todos</option>
            </select>
            <button className="btn-primary">Buscar</button>
          </div>
          <div className="reporte-content">
            <table className="reporte-table">
              <thead>
                <tr>
                  <th>Técnico</th>
                  <th>Citas atendidas</th>
                  <th>Nivel de satisfacción</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td colSpan="3" className="no-data">No hay datos disponibles</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        {/* Días más trabajados */}
        <div className="reporte-card">
          <h2>Días más trabajados</h2>
          <div className="filters">
            <select
              value={filters.periodo}
              onChange={(e) => setFilters({ ...filters, periodo: e.target.value })}
            >
              <option value="ULTIMOS_6_MESES">Últimos 6 meses</option>
              <option value="ULTIMOS_3_MESES">Últimos 3 meses</option>
              <option value="ULTIMO_MES">Último mes</option>
            </select>
            <select
              value={filters.tecnico}
              onChange={(e) => setFilters({ ...filters, tecnico: e.target.value })}
            >
              <option value="TODOS">Todas</option>
            </select>
            <button className="btn-primary">Buscar</button>
          </div>
          <div className="reporte-content">
            <table className="reporte-table">
              <thead>
                <tr>
                  <th>Día</th>
                  <th>Número de citas</th>
                  <th>Porcentaje</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td colSpan="3" className="no-data">No hay datos disponibles</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        {/* Repuestos más usados */}
        <div className="reporte-card">
          <h2>Repuestos más usados</h2>
          <div className="filters">
            <select
              value={filters.periodo}
              onChange={(e) => setFilters({ ...filters, periodo: e.target.value })}
            >
              <option value="ULTIMOS_6_MESES">Últimos 6 meses</option>
              <option value="ULTIMOS_3_MESES">Últimos 3 meses</option>
              <option value="ULTIMO_MES">Último mes</option>
            </select>
            <select
              value={filters.marca}
              onChange={(e) => setFilters({ ...filters, marca: e.target.value })}
            >
              <option value="TODAS">Todas</option>
            </select>
            <button className="btn-primary">Buscar</button>
          </div>
          <div className="reporte-content">
            <table className="reporte-table">
              <thead>
                <tr>
                  <th>Repuesto</th>
                  <th>Cantidad usada</th>
                  <th>Porcentaje</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td colSpan="3" className="no-data">No hay datos disponibles</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Reportes

