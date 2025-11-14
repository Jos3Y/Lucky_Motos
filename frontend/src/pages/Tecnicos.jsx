import { useState, useEffect } from 'react'
import { tecnicosAPI } from '../services/api'
import './Tecnicos.css'

const Tecnicos = () => {
  const [tecnicos, setTecnicos] = useState([])
  const [loading, setLoading] = useState(true)
  const [selectedTecnico, setSelectedTecnico] = useState(null)

  useEffect(() => {
    loadTecnicos()
  }, [])

  const loadTecnicos = async () => {
    try {
      const response = await tecnicosAPI.getAll()
      setTecnicos(response.data)
    } catch (error) {
      console.error('Error cargando técnicos:', error)
    } finally {
      setLoading(false)
    }
  }

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
        <h1>Gestión de Técnicos</h1>
      </div>

      <div className="tecnicos-container">
        <div className="tecnicos-list">
          <h2>Lista de Técnicos</h2>
          <div className="tecnicos-grid">
            {tecnicos.map(tecnico => (
              <div
                key={tecnico.id}
                className={`tecnico-card ${selectedTecnico?.id === tecnico.id ? 'selected' : ''}`}
                onClick={() => setSelectedTecnico(tecnico)}
              >
                <div className="tecnico-info">
                  <h3>{tecnico.nombre} {tecnico.apellidos}</h3>
                  <p>{tecnico.especialidad}</p>
                  <span className={`badge ${getEstadoBadgeClass(tecnico.estado)}`}>
                    {tecnico.estado}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {selectedTecnico && (
          <div className="disponibilidad-container">
            <h2>Disponibilidad - {selectedTecnico.nombre} {selectedTecnico.apellidos}</h2>
            <div className="disponibilidad-list">
              {selectedTecnico.disponibilidades && selectedTecnico.disponibilidades.length > 0 ? (
                selectedTecnico.disponibilidades.map(disp => (
                  <div key={disp.id} className="disponibilidad-item">
                    <span className="dia">{disp.diaSemana}</span>
                    <span className="horario">
                      {disp.horaInicio} - {disp.horaFin}
                    </span>
                    <span className={`status ${disp.disponible ? 'disponible' : 'no-disponible'}`}>
                      {disp.disponible ? '✓ Disponible' : '✗ No disponible'}
                    </span>
                  </div>
                ))
              ) : (
                <p className="no-disponibilidad">No hay disponibilidad configurada</p>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default Tecnicos

