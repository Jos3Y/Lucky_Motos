import { useEffect, useState } from 'react'
import Swal from 'sweetalert2'
import { reportesAPI } from '../services/api'
import './Reportes.css'

const periodos = [
  { label: 'Últimos 6 meses', value: 'ULTIMOS_6_MESES' },
  { label: 'Últimos 3 meses', value: 'ULTIMOS_3_MESES' },
  { label: 'Último mes', value: 'ULTIMO_MES' }
]

const Reportes = () => {
  const [periodo, setPeriodo] = useState('ULTIMOS_6_MESES')
  const [data, setData] = useState({
    mejoresTecnicos: [],
    diasConMasCitas: [],
    repuestosMasUsados: []
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchDatos()
  }, [periodo])

  const fetchDatos = async () => {
    try {
      setLoading(true)
      const { data } = await reportesAPI.getResumen(periodo)
      setData(data)
    } catch (error) {
      Swal.fire('Error', 'No se pudieron cargar los reportes', 'error')
    } finally {
      setLoading(false)
    }
  }

  const descargarExcel = async () => {
    try {
      const response = await reportesAPI.downloadExcel(periodo)
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', 'reportes.xlsx')
      document.body.appendChild(link)
      link.click()
      link.remove()
    } catch (error) {
      Swal.fire('Error', 'No se pudo descargar el Excel', 'error')
    }
  }

  const renderTabla = (headers, rows, emptyMessage) => (
    <table className="reporte-table">
      <thead>
        <tr>
          {headers.map((h) => <th key={h}>{h}</th>)}
        </tr>
      </thead>
      <tbody>
        {rows.length === 0 ? (
          <tr>
            <td colSpan={headers.length} className="no-data">{emptyMessage}</td>
          </tr>
        ) : rows}
      </tbody>
    </table>
  )

  return (
    <div className="reportes-page">
      <div className="page-header">
        <div>
          <h1>Reportes</h1>
          <p>Visualiza el desempeño del taller</p>
        </div>
        <div className="header-actions">
          <select value={periodo} onChange={(e) => setPeriodo(e.target.value)}>
            {periodos.map(p => <option key={p.value} value={p.value}>{p.label}</option>)}
          </select>
          <button className="btn-primary" onClick={descargarExcel}>Descargar Excel</button>
        </div>
      </div>

      {loading ? (
        <div className="loading">Cargando reportes...</div>
      ) : (
        <div className="reportes-grid">
          <div className="reporte-card">
            <h2>Mejores técnicos</h2>
            {renderTabla(
              ['Técnico', 'Total de citas'],
              data.mejoresTecnicos.map(item => (
                <tr key={item.tecnicoId}>
                  <td>{item.nombreCompleto}</td>
                  <td>{item.totalCitas}</td>
                </tr>
              )),
              'No hay información disponible'
            )}
          </div>

          <div className="reporte-card">
            <h2>Días con más citas</h2>
            {renderTabla(
              ['Fecha', 'Total de citas'],
              data.diasConMasCitas.map((item, idx) => (
                <tr key={idx}>
                  <td>{item.fecha}</td>
                  <td>{item.totalCitas}</td>
                </tr>
              )),
              'No hay información disponible'
            )}
          </div>

          <div className="reporte-card">
            <h2>Repuestos más usados</h2>
            {renderTabla(
              ['Repuesto', 'Cantidad'],
              data.repuestosMasUsados.map((item, idx) => (
                <tr key={idx}>
                  <td>{item.nombreRepuesto}</td>
                  <td>{item.cantidad}</td>
                </tr>
              )),
              'No hay información disponible'
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default Reportes

