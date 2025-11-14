import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import '../css/Register.css'

function Register() {
    const [formData, setFormData] = useState({
        nombre: '',
        apellidos: '',
        correo: '',
        celular: '',
        contrasena: '',
        confirmarContrasena: '',
        aceptarTerminos: false
    })
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)
    const { register } = useAuth()
    const navigate = useNavigate()

    const handleChange = (e) => {
        const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value
        setFormData({
            ...formData,
            [e.target.name]: value
        })
        setError('')
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setError('')
        setLoading(true)
        
        if (!formData.nombre || !formData.apellidos || !formData.correo || 
            !formData.celular || !formData.contrasena || !formData.confirmarContrasena) {
            setError('Por favor, complete todos los campos')
            setLoading(false)
            return
        }
        
        if (formData.contrasena !== formData.confirmarContrasena) {
            setError('Las contraseñas no coinciden')
            setLoading(false)
            return
        }
        
        if (formData.contrasena.length < 8) {
            setError('La contraseña debe tener al menos 8 caracteres')
            setLoading(false)
            return
        }
        
        if (!formData.aceptarTerminos) {
            setError('Debe aceptar los términos y condiciones')
            setLoading(false)
            return
        }
        
        const result = await register({
            nombre: formData.nombre,
            apellidos: formData.apellidos,
            correo: formData.correo,
            telefono: formData.celular,
            contrasena: formData.contrasena
        })
        
        if (result.success) {
            alert('Registro exitoso. Por favor, inicia sesión.')
            navigate('/login')
        } else {
            setError(result.message || 'Error al registrar')
        }
        
        setLoading(false)
    }

    return (
        <div className="register-container">
            {/* Panel izquierdo - Formulario */}
            <div className="form-panel">
                <div className="form-container">
                    <h1 className="form-title">Formulario de Registro</h1>
                    
                    <form onSubmit={handleSubmit} className="register-form">
                        {error && <div className="error-message">{error}</div>}
                        <div className="form-row">
                            <div className="form-group">
                                <label htmlFor="nombre" className="form-label">Nombre</label>
                                <input 
                                    type="text" 
                                    id="nombre" 
                                    name="nombre" 
                                    className="form-input" 
                                    placeholder="Ingrese sus nombes"
                                    value={formData.nombre}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="apellidos" className="form-label">Apellidos</label>
                                <input 
                                    type="text" 
                                    id="apellidos" 
                                    name="apellidos" 
                                    className="form-input" 
                                    placeholder="Ingrese sus apellidos"
                                    value={formData.apellidos}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label htmlFor="correo" className="form-label">Correo Electronico</label>
                            <input 
                                type="email" 
                                id="correo" 
                                name="correo" 
                                className="form-input" 
                                placeholder="Ingrese su correo electronico"
                                value={formData.correo}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="celular" className="form-label">Numero celular</label>
                            <input 
                                type="tel" 
                                id="celular" 
                                name="celular" 
                                className="form-input" 
                                placeholder="Ingrese su numero"
                                value={formData.celular}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="contrasena" className="form-label">Contraseña</label>
                            <input 
                                type="password" 
                                id="contrasena" 
                                name="contrasena" 
                                className="form-input" 
                                placeholder="Cree una contraseña segura"
                                value={formData.contrasena}
                                onChange={handleChange}
                                required
                                minLength="8"
                            />
                            <span className="password-hint">Mínimo 8 caracteres.</span>
                        </div>

                        <div className="form-group">
                            <label htmlFor="confirmarContrasena" className="form-label">Confirmar Contraseña</label>
                            <input 
                                type="password" 
                                id="confirmarContrasena" 
                                name="confirmarContrasena" 
                                className="form-input" 
                                placeholder="Confirm your password"
                                value={formData.confirmarContrasena}
                                onChange={handleChange}
                                required
                                minLength="8"
                            />
                        </div>

                        {/* Términos y condiciones */}
                        <div className="terms-section">
                            <label className="checkbox-label">
                                <input 
                                    type="checkbox" 
                                    id="aceptarTerminos" 
                                    name="aceptarTerminos" 
                                    checked={formData.aceptarTerminos}
                                    onChange={handleChange}
                                    required
                                />
                                <span className="checkbox-text">
                                    <a href="#" className="terms-link">Terms of Service</a> and 
                                    <a href="#" className="terms-link">Privacy Policy</a>
                                </span>
                            </label>
                        </div>

                        <button type="submit" className="register-button" disabled={loading}>
                            {loading ? 'Registrando...' : 'Crear Cuenta'}
                        </button>
                    </form>

                    {/* Enlace al login */}
                    <div className="login-link-section">
                        <span>¡Ya tienes una cuenta? </span>
                        <Link to="/login" className="login-link">Inicio Seccion Aqui</Link>
                    </div>
                </div>
            </div>

            {/* Panel derecho - Branding */}
            <div className="branding-panel">
                <div className="logo-container">
                    <div className="logo-box">
                        <img src="/images/photos/image.png" alt="Logo Lucky Motos" className="main-logo" />
                    </div>
                    
                    <p className="header-text">
                        Para acceder a nuestros servicios desde nuestra pagina web, regístrate y accede a todos los beneficios que tenemos para usted, desde nuestra pagina web.
                    </p>
                </div>
            </div>
        </div>
    )
}

export default Register

