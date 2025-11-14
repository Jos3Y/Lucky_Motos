import { useState, useEffect } from 'react'
import { repuestosAPI } from '../services/api'
import './Repuestos.css'

const Repuestos = () => {
  const [repuestos, setRepuestos] = useState([])
  const [loading, setLoading] = useState(true)
  const [filters, setFilters] = useState({
    marca: '',
    modelo: '',
    repuesto: ''
  })
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    nombre: '',
    marca: '',
    modeloCompatible: '',
    stock: 0,
    precio: 0
  })

  useEffect(() => {
    loadRepuestos()
  }, [])

  const loadRepuestos = async () => {
    try {
      const response = await repuestosAPI.getAll(filters)
      setRepuestos(response.data)
    } catch (error) {
      console.error('Error cargando repuestos:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleFilter = () => {
    loadRepuestos()
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await repuestosAPI.create(formData)
      setShowForm(false)
      loadRepuestos()
      alert('Repuesto creado exitosamente')
      setFormData({ nombre: '', marca: '', modeloCompatible: '', stock: 0, precio: 0 })
    } catch (error) {
      alert('Error al crear repuesto: ' + (error.response?.data?.message || error.message))
    }
  }

  const getEstadoBadgeClass = (estado) => {
    const estados = {
      DISPONIBLE: 'badge-success',
      AGOTADO: 'badge-danger',
      BAJO_STOCK: 'badge-warning'
    }
    return estados[estado] || 'badge-secondary'
  }

  if (loading) {
    return <div className="loading">Cargando repuestos...</div>
  }

  return (
    <div className="repuestos-page">
      <div className="page-header">
        <h1>Gestión de Repuestos</h1>
        <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : 'Agregar Repuesto'}
        </button>
      </div>

      {showForm && (
        <div className="repuesto-form-container">
          <h2>Agregar nuevo repuesto</h2>
          <form onSubmit={handleSubmit} className="repuesto-form">
            <div className="form-row">
              <div className="form-group">
                <label>Nombre del repuesto</label>
                <input
                  type="text"
                  value={formData.nombre}
                  onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Marca</label>
                <input
                  type="text"
                  value={formData.marca}
                  onChange={(e) => setFormData({ ...formData, marca: e.target.value })}
                  required
                />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Modelo compatible</label>
                <input
                  type="text"
                  value={formData.modeloCompatible}
                  onChange={(e) => setFormData({ ...formData, modeloCompatible: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Stock</label>
                <input
                  type="number"
                  value={formData.stock}
                  onChange={(e) => setFormData({ ...formData, stock: parseInt(e.target.value) })}
                  required
                  min="0"
                />
              </div>
            </div>
            <div className="form-group">
              <label>Precio</label>
              <input
                type="number"
                step="0.01"
                value={formData.precio}
                onChange={(e) => setFormData({ ...formData, precio: parseFloat(e.target.value) })}
                required
                min="0"
              />
            </div>
            <div className="form-actions">
              <button type="submit" className="btn-primary">Guardar</button>
              <button type="button" className="btn-secondary" onClick={() => setShowForm(false)}>
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="filters-container">
        <h2>Filtros de búsqueda</h2>
        <div className="filters">
          <div className="filter-group">
            <label>Marca</label>
            <select
              value={filters.marca}
              onChange={(e) => setFilters({ ...filters, marca: e.target.value })}
            >
              <option value="">Todas</option>
              {[...new Set(repuestos.map(r => r.marca))].map(marca => (
                <option key={marca} value={marca}>{marca}</option>
              ))}
            </select>
          </div>
          <div className="filter-group">
            <label>Modelo</label>
            <select
              value={filters.modelo}
              onChange={(e) => setFilters({ ...filters, modelo: e.target.value })}
            >
              <option value="">Todos</option>
              {[...new Set(repuestos.map(r => r.modeloCompatible))].map(modelo => (
                <option key={modelo} value={modelo}>{modelo}</option>
              ))}
            </select>
          </div>
          <div className="filter-group">
            <label>Repuesto</label>
            <input
              type="text"
              placeholder="Buscar repuesto..."
              value={filters.repuesto}
              onChange={(e) => setFilters({ ...filters, repuesto: e.target.value })}
            />
          </div>
          <button className="btn-primary" onClick={handleFilter}>Buscar</button>
        </div>
      </div>

      <div className="repuestos-table-container">
        <table className="repuestos-table">
          <thead>
            <tr>
              <th>Repuesto</th>
              <th>Marca</th>
              <th>Modelo</th>
              <th>Stock</th>
              <th>Estado</th>
              <th>Acción</th>
            </tr>
          </thead>
          <tbody>
            {repuestos.length === 0 ? (
              <tr>
                <td colSpan="6" className="no-data">No hay repuestos registrados</td>
              </tr>
            ) : (
              repuestos.map(repuesto => (
                <tr key={repuesto.id}>
                  <td>{repuesto.nombre}</td>
                  <td>{repuesto.marca}</td>
                  <td>{repuesto.modeloCompatible || '-'}</td>
                  <td>{repuesto.stock}</td>
                  <td>
                    <span className={`badge ${getEstadoBadgeClass(repuesto.estado)}`}>
                      {repuesto.estado}
                    </span>
                  </td>
                  <td>
                    {repuesto.estado === 'AGOTADO' && (
                      <button className="btn-link">Solicitar compra</button>
                    )}
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

export default Repuestos

