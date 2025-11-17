import { useEffect, useState } from 'react'
import Swal from 'sweetalert2'
import { sociosAPI } from '../services/api'
import './Usuarios.css'

const initialUser = {
  nombre: '',
  apellidos: '',
  correo: '',
  telefono: '',
  dni: '',
  genero: 'MASCULINO',
  estadoCivil: 'SOLTERO',
  contrasena: ''
}

const Usuarios = () => {
  const [usuarios, setUsuarios] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState(initialUser)
  const [editing, setEditing] = useState(null)

  useEffect(() => {
    loadUsuarios()
  }, [])

  const loadUsuarios = async () => {
    try {
      setLoading(true)
      const { data } = await sociosAPI.getAll()
      setUsuarios(Array.isArray(data) ? data : [])
    } catch (error) {
      Swal.fire('Error', 'No se pudieron cargar los usuarios', 'error')
    } finally {
      setLoading(false)
    }
  }

  const openForm = (usuario) => {
    if (usuario) {
      setEditing(usuario)
      setFormData({
        nombre: usuario.nombres,
        apellidos: usuario.apellidos,
        correo: usuario.correo,
        telefono: usuario.telefono || '',
        dni: usuario.dni || '',
        genero: usuario.genero || 'MASCULINO',
        estadoCivil: usuario.estadoCivil || 'SOLTERO',
        contrasena: ''
      })
    } else {
      setEditing(null)
      setFormData(initialUser)
    }
    setShowForm(true)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      if (editing) {
        await sociosAPI.update(editing.idSocio, formData)
        Swal.fire('Actualizado', 'Usuario actualizado correctamente', 'success')
      } else {
        await sociosAPI.create(formData)
        Swal.fire('Creado', 'Usuario creado correctamente', 'success')
      }
      setShowForm(false)
      setEditing(null)
      setFormData(initialUser)
      loadUsuarios()
    } catch (error) {
      Swal.fire('Error', error.response?.data?.message || 'No se pudo guardar', 'error')
    }
  }

  const handleDelete = async (usuario) => {
    const result = await Swal.fire({
      icon: 'warning',
      title: `¿Eliminar a ${usuario.nombres}?`,
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar'
    })
    if (!result.isConfirmed) return
    try {
      await sociosAPI.delete(usuario.idSocio)
      Swal.fire('Eliminado', 'Usuario eliminado', 'success')
      loadUsuarios()
    } catch (error) {
      Swal.fire('Error', 'No se pudo eliminar', 'error')
    }
  }

  if (loading) {
    return <div className="loading">Cargando usuarios...</div>
  }

  return (
    <div className="usuarios-page">
      <div className="page-header">
        <div>
          <h1>Usuarios del sistema</h1>
          <p>Solo administradores</p>
        </div>
        <button className="btn-primary" onClick={() => openForm(null)}>Nuevo usuario</button>
      </div>

      <div className="usuarios-card">
        <table className="usuarios-table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Correo</th>
              <th>DNI</th>
              <th>Teléfono</th>
              <th>Género</th>
              <th>Estado civil</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {usuarios.length === 0 ? (
              <tr>
                <td colSpan="7" className="no-data">No hay usuarios registrados</td>
              </tr>
            ) : (
              usuarios.map((user) => (
                <tr key={user.idSocio}>
                  <td>{user.nombres} {user.apellidos}</td>
                  <td>{user.correo}</td>
                  <td>{user.dni || '-'}</td>
                  <td>{user.telefono || '-'}</td>
                  <td>{user.genero || '-'}</td>
                  <td>{user.estadoCivil || '-'}</td>
                  <td>
                    <div className="table-actions">
                      <button className="btn-link" onClick={() => openForm(user)}>Editar</button>
                      <button className="btn-link danger" onClick={() => handleDelete(user)}>Eliminar</button>
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
              <h3>{editing ? 'Editar usuario' : 'Nuevo usuario'}</h3>
              <button className="close-modal" onClick={() => setShowForm(false)}>✕</button>
            </div>
            <form className="usuario-form" onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Nombres</label>
                  <input
                    type="text"
                    value={formData.nombre}
                    onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Apellidos</label>
                  <input
                    type="text"
                    value={formData.apellidos}
                    onChange={(e) => setFormData({ ...formData, apellidos: e.target.value })}
                    required
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Correo</label>
                  <input
                    type="email"
                    value={formData.correo}
                    onChange={(e) => setFormData({ ...formData, correo: e.target.value })}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Teléfono</label>
                  <input
                    type="text"
                    value={formData.telefono}
                    onChange={(e) => setFormData({ ...formData, telefono: e.target.value })}
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>DNI</label>
                  <input
                    type="text"
                    value={formData.dni}
                    onChange={(e) => setFormData({ ...formData, dni: e.target.value })}
                  />
                </div>
                <div className="form-group">
                  <label>Género</label>
                  <select
                    value={formData.genero}
                    onChange={(e) => setFormData({ ...formData, genero: e.target.value })}
                  >
                    <option value="MASCULINO">Masculino</option>
                    <option value="FEMENINO">Femenino</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Estado civil</label>
                  <select
                    value={formData.estadoCivil}
                    onChange={(e) => setFormData({ ...formData, estadoCivil: e.target.value })}
                  >
                    <option value="SOLTERO">Soltero</option>
                    <option value="CASADO">Casado</option>
                    <option value="DIVORCIADO">Divorciado</option>
                    <option value="VIUDO">Viudo</option>
                  </select>
                </div>
              </div>
              <div className="form-group">
                <label>Contraseña {editing && '(dejar en blanco para mantener)'}</label>
                <input
                  type="password"
                  value={formData.contrasena}
                  onChange={(e) => setFormData({ ...formData, contrasena: e.target.value })}
                  required={!editing}
                />
              </div>
              <div className="modal-actions">
                <button className="btn-secondary" type="button" onClick={() => setShowForm(false)}>Cancelar</button>
                <button className="btn-primary" type="submit">Guardar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

export default Usuarios

