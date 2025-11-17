import { useState, useEffect } from 'react'
import Swal from 'sweetalert2'
import { repuestosAPI } from '../services/api'
import './Repuestos.css'

const initialFormData = {
  nombre: '',
  marca: '',
  modeloCompatible: '',
  stock: 0,
  precio: 0
}

const Repuestos = () => {
  const [repuestos, setRepuestos] = useState([])
  const [loading, setLoading] = useState(true)
  const [filters, setFilters] = useState({
    marca: '',
    modelo: '',
    repuesto: ''
  })
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState(initialFormData)
  const [editingRepuesto, setEditingRepuesto] = useState(null)

  useEffect(() => {
    loadRepuestos()
  }, [])

  const loadRepuestos = async () => {
    try {
      setLoading(true)
      // Filtrar solo los parámetros que tienen valores
      const params = {}
      if (filters.marca && filters.marca.trim() !== '') {
        params.marca = filters.marca
      }
      if (filters.modelo && filters.modelo.trim() !== '') {
        params.modelo = filters.modelo
      }
      if (filters.repuesto && filters.repuesto.trim() !== '') {
        params.repuesto = filters.repuesto
      }

      console.log('DEBUG Frontend - Filtros originales:', filters)
      console.log('DEBUG Frontend - Parámetros a enviar:', params)
      console.log('DEBUG Frontend - URL:', '/api/repuestos')

      const response = await repuestosAPI.getAll(params)
      console.log('DEBUG Frontend - Respuesta completa:', response)
      console.log('DEBUG Frontend - Response data:', response.data)
      console.log('DEBUG Frontend - Tipo de data:', typeof response.data)
      console.log('DEBUG Frontend - Es array?', Array.isArray(response.data))
      console.log('DEBUG Frontend - Cantidad de repuestos:', response.data?.length || 0)

      setRepuestos(response.data || [])
      console.log('Repuestos recibidos:', response.data)
    } catch (error) {
      console.error('Error cargando repuestos:', error)
      console.error('Error response:', error.response)
      console.error('Error data:', error.response?.data)
      setRepuestos([])
    } finally {
      setLoading(false)
    }
  }

  const handleFilter = () => {
    loadRepuestos()
  }

  const handleEditRepuesto = (repuesto) => {
    setFormData({
      nombre: repuesto.nombre || '',
      marca: repuesto.marca || '',
      modeloCompatible: repuesto.modeloCompatible || '',
      stock: repuesto.stock || 0,
      precio: repuesto.precio || 0
    })
    setEditingRepuesto(repuesto)
    setShowForm(true)
  }

  const handleDeleteRepuesto = async (repuesto) => {
    const confirmar = window.confirm(`¿Eliminar el repuesto "${repuesto.nombre}"?`)
    if (!confirmar) return
    try {
      const result = await Swal.fire({
        icon: 'warning',
        title: 'Eliminar repuesto',
        text: `¿Desea eliminar el repuesto "${repuesto.nombre}"?`,
        showCancelButton: true,
        confirmButtonColor: '#e74c3c',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
      })
      if (!result.isConfirmed) return

      await repuestosAPI.delete(repuesto.id)
      Swal.fire({
        icon: 'success',
        title: 'Repuesto eliminado',
        confirmButtonColor: '#2c3e50'
      })
      loadRepuestos()
    } catch (error) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: error.response?.data?.message || error.message,
        confirmButtonColor: '#2c3e50'
      })
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      if (editingRepuesto) {
        await repuestosAPI.update(editingRepuesto.id, formData)
        Swal.fire({
          icon: 'success',
          title: 'Repuesto actualizado',
          confirmButtonColor: '#2c3e50'
        })
      } else {
        await repuestosAPI.create(formData)
        Swal.fire({
          icon: 'success',
          title: 'Repuesto creado',
          confirmButtonColor: '#2c3e50'
        })
      }
      setShowForm(false)
      setEditingRepuesto(null)
      setFormData(initialFormData)
      loadRepuestos()
    } catch (error) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: error.response?.data?.message || error.message,
        confirmButtonColor: '#2c3e50'
      })
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
        <button
          className="btn-primary"
          onClick={() => {
            if (showForm && !editingRepuesto) {
              setShowForm(false)
            } else {
              setFormData(initialFormData)
              setEditingRepuesto(null)
              setShowForm(!showForm)
            }
          }}
        >
          {showForm ? 'Cerrar formulario' : 'Agregar Repuesto'}
        </button>
      </div>

      {showForm && (
        <div className="repuesto-form-container">
          <h2>{editingRepuesto ? 'Editar repuesto' : 'Agregar nuevo repuesto'}</h2>
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
              <button
                type="button"
                className="btn-secondary"
                onClick={() => {
                  setShowForm(false)
                  setEditingRepuesto(null)
                  setFormData(initialFormData)
                }}
              >
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
                    <div className="table-actions">
                      <button
                        type="button"
                        className="btn-link"
                        onClick={() => handleEditRepuesto(repuesto)}
                      >
                        Editar
                      </button>
                      <button
                        type="button"
                        className="btn-link danger"
                        onClick={() => handleDeleteRepuesto(repuesto)}
                      >
                        Eliminar
                      </button>
                    </div>
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

