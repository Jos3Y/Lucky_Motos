import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import { sociosAPI, tecnicosAPI } from '../services/api'
import Swal from 'sweetalert2'
import './MiPerfil.css'

const MiPerfil = () => {
  const { user } = useAuth()
  const [loading, setLoading] = useState(true)
  const [formData, setFormData] = useState({
    nombre: '',
    apellidos: '',
    correo: '',
    telefono: '',
    dni: '',
    genero: 'MASCULINO',
    estadoCivil: 'SOLTERO',
    contrasena: '',
    confirmarContrasena: ''
  })
  const [tecnicoData, setTecnicoData] = useState(null)

  useEffect(() => {
    if (user?.id) {
      loadDatos()
    }
  }, [user])

  const loadDatos = async () => {
    try {
      setLoading(true)
      const [socioRes, tecnicoRes] = await Promise.all([
        sociosAPI.getById(user.id).catch(() => ({ data: null })),
        tecnicosAPI.getBySocioId(user.id).catch(() => ({ data: null }))
      ])

      if (socioRes.data) {
        const socio = socioRes.data
        setFormData({
          nombre: socio.nombres || socio.nombre || '',
          apellidos: socio.apellidos || '',
          correo: socio.correo || '',
          telefono: socio.telefono || '',
          dni: socio.dni || '',
          genero: socio.genero || 'MASCULINO',
          estadoCivil: socio.estadoCivil || 'SOLTERO',
          contrasena: '',
          confirmarContrasena: ''
        })
      }

      if (tecnicoRes.data) {
        setTecnicoData(tecnicoRes.data)
      }
    } catch (error) {
      console.error('Error cargando datos:', error)
      Swal.fire('Error', 'No se pudieron cargar tus datos', 'error')
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (formData.contrasena && formData.contrasena !== formData.confirmarContrasena) {
      Swal.fire('Error', 'Las contraseñas no coinciden', 'error')
      return
    }

    try {
      const updateData = {
        nombre: formData.nombre,
        apellidos: formData.apellidos,
        correo: formData.correo,
        telefono: formData.telefono,
        dni: formData.dni,
        genero: formData.genero,
        estadoCivil: formData.estadoCivil
      }

      if (formData.contrasena && formData.contrasena.trim() !== '') {
        updateData.contrasena = formData.contrasena
      }

      await sociosAPI.update(user.id, updateData)
      Swal.fire({
        icon: 'success',
        title: 'Datos actualizados',
        text: 'Tus datos personales han sido actualizados correctamente',
        confirmButtonColor: '#2c3e50',
        timer: 2000
      })
      
      setFormData(prev => ({ ...prev, contrasena: '', confirmarContrasena: '' }))
    } catch (error) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: error.response?.data?.message || 'No se pudieron actualizar los datos',
        confirmButtonColor: '#2c3e50'
      })
    }
  }

  if (loading) {
    return <div className="mi-perfil-loading">Cargando...</div>
  }

  return (
    <div className="mi-perfil">
      <div className="mi-perfil-header">
        <h1>Mi Perfil</h1>
        <p>Actualiza tus datos personales</p>
      </div>

      {tecnicoData && (
        <div className="tecnico-info-card">
          <h3>Información de Técnico</h3>
          <div className="info-row">
            <span className="info-label">Especialidad:</span>
            <span className="info-value">{tecnicoData.especialidad || 'No asignada'}</span>
          </div>
          <div className="info-row">
            <span className="info-label">Estado:</span>
            <span className={`info-badge ${tecnicoData.estado?.toLowerCase()}`}>
              {tecnicoData.estado || 'N/A'}
            </span>
          </div>
        </div>
      )}

      <form onSubmit={handleSubmit} className="mi-perfil-form">
        <div className="form-section">
          <h3>Datos Personales</h3>
          <div className="form-row">
            <div className="form-group">
              <label>Nombre *</label>
              <input
                type="text"
                value={formData.nombre}
                onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                required
              />
            </div>
            <div className="form-group">
              <label>Apellidos *</label>
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
              <label>Correo electrónico *</label>
              <input
                type="email"
                value={formData.correo}
                onChange={(e) => setFormData({ ...formData, correo: e.target.value })}
                required
              />
            </div>
            <div className="form-group">
              <label>Teléfono *</label>
              <input
                type="tel"
                value={formData.telefono}
                onChange={(e) => setFormData({ ...formData, telefono: e.target.value })}
                required
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
                <option value="OTRO">Otro</option>
              </select>
            </div>
            <div className="form-group">
              <label>Estado Civil</label>
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
        </div>

        <div className="form-section">
          <h3>Cambiar Contraseña</h3>
          <p className="form-help">Deja en blanco si no deseas cambiar la contraseña</p>
          <div className="form-row">
            <div className="form-group">
              <label>Nueva contraseña</label>
              <input
                type="password"
                value={formData.contrasena}
                onChange={(e) => setFormData({ ...formData, contrasena: e.target.value })}
                placeholder="Dejar en blanco para mantener la actual"
              />
            </div>
            <div className="form-group">
              <label>Confirmar contraseña</label>
              <input
                type="password"
                value={formData.confirmarContrasena}
                onChange={(e) => setFormData({ ...formData, confirmarContrasena: e.target.value })}
                placeholder="Repite la nueva contraseña"
              />
            </div>
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn-primary">Guardar Cambios</button>
          <button
            type="button"
            className="btn-secondary"
            onClick={() => loadDatos()}
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  )
}

export default MiPerfil

