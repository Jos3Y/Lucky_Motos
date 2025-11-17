import { useState, useEffect } from 'react'
import Swal from 'sweetalert2'
import { tiposServicioAPI } from '../services/api'
import './Especialidades.css'

const initialForm = {
    nombre: '',
    descripcion: '',
    precioBase: 0,
    duracionEstimadaMinutos: 60
}

const Especialidades = () => {
    const [especialidades, setEspecialidades] = useState([])
    const [loading, setLoading] = useState(true)
    const [showForm, setShowForm] = useState(false)
    const [formData, setFormData] = useState(initialForm)
    const [editing, setEditing] = useState(null)

    useEffect(() => {
        loadEspecialidades()
    }, [])

    const loadEspecialidades = async () => {
        try {
            setLoading(true)
            const { data } = await tiposServicioAPI.getAll()
            setEspecialidades(Array.isArray(data) ? data : [])
        } catch (error) {
            Swal.fire('Error', 'No se pudieron cargar las especialidades', 'error')
        } finally {
            setLoading(false)
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        try {
            if (editing) {
                await tiposServicioAPI.update(editing.id, formData)
                Swal.fire('Actualizado', 'Especialidad actualizada correctamente', 'success')
            } else {
                await tiposServicioAPI.create(formData)
                Swal.fire('Creado', 'Especialidad registrada correctamente', 'success')
            }
            setShowForm(false)
            setFormData(initialForm)
            setEditing(null)
            loadEspecialidades()
        } catch (error) {
            Swal.fire('Error', error.response?.data?.message || 'No se pudo guardar', 'error')
        }
    }

    const handleDelete = async (especialidad) => {
        const result = await Swal.fire({
            icon: 'warning',
            title: '¿Eliminar especialidad?',
            text: especialidad.nombre,
            showCancelButton: true,
            confirmButtonText: 'Sí, eliminar'
        })
        if (!result.isConfirmed) return
        try {
            await tiposServicioAPI.delete(especialidad.id)
            Swal.fire('Eliminado', 'Especialidad eliminada', 'success')
            loadEspecialidades()
        } catch (error) {
            Swal.fire('Error', 'No se pudo eliminar', 'error')
        }
    }

    const openForm = (item) => {
        if (item) {
            setEditing(item)
            setFormData({
                nombre: item.nombre,
                descripcion: item.descripcion || '',
                precioBase: item.precioBase ?? 0,
                duracionEstimadaMinutos: item.duracionEstimadaMinutos ?? 60
            })
        } else {
            setEditing(null)
            setFormData(initialForm)
        }
        setShowForm(true)
    }

    if (loading) {
        return <div className="loading">Cargando especialidades...</div>
    }

    return (
        <div className="especialidades-page">
            <div className="page-header">
                <div>
                    <h1>Especialidades / Tipos de servicio</h1>
                    <p>Gestiona los servicios ofrecidos por el taller</p>
                </div>
                <button className="btn-primary" onClick={() => openForm(null)}>Nuevo tipo de servicio</button>
            </div>

            <div className="especialidades-card">
                <table className="especialidades-table">
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Descripción</th>
                            <th>Precio base (S/)</th>
                            <th>Duración (min)</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {especialidades.length === 0 ? (
                            <tr>
                                <td colSpan="5" className="no-data">No hay especialidades registradas</td>
                            </tr>
                        ) : (
                            especialidades.map(item => (
                                <tr key={item.id}>
                                    <td>{item.nombre}</td>
                                    <td>{item.descripcion || '-'}</td>
                                    <td>{Number(item.precioBase || 0).toFixed(2)}</td>
                                    <td>{item.duracionEstimadaMinutos || '-'}</td>
                                    <td>
                                        <div className="table-actions">
                                            <button className="btn-link" onClick={() => openForm(item)}>Editar</button>
                                            <button className="btn-link danger" onClick={() => handleDelete(item)}>Eliminar</button>
                                        </div>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {showForm && (
                <div className="modal-overlay">
                    <div className="modal">
                        <div className="modal-header">
                            <h3>{editing ? 'Editar especialidad' : 'Nueva especialidad'}</h3>
                            <button className="close-modal" onClick={() => setShowForm(false)}>✕</button>
                        </div>
                        <form className="especialidad-form" onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Nombre</label>
                                <input
                                    type="text"
                                    value={formData.nombre}
                                    onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label>Descripción</label>
                                <textarea
                                    value={formData.descripcion}
                                    onChange={(e) => setFormData({ ...formData, descripcion: e.target.value })}
                                    rows="3"
                                />
                            </div>
                            <div className="form-row">
                                <div className="form-group">
                                    <label>Precio base (S/)</label>
                                    <input
                                        type="number"
                                        step="0.1"
                                        value={formData.precioBase}
                                        onChange={(e) => setFormData({ ...formData, precioBase: Number(e.target.value) })}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Duración (min)</label>
                                    <input
                                        type="number"
                                        value={formData.duracionEstimadaMinutos}
                                        onChange={(e) => setFormData({ ...formData, duracionEstimadaMinutos: Number(e.target.value) })}
                                        required
                                    />
                                </div>
                            </div>
                            <div className="modal-actions">
                                <button type="button" className="btn-secondary" onClick={() => setShowForm(false)}>Cancelar</button>
                                <button className="btn-primary" type="submit">Guardar</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    )
}

export default Especialidades

