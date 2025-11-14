import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import '../css/Login.css'

function Login() {
    const [formData, setFormData] = useState({
        correo: '',
        contrasena: ''
    })
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)
    const { login } = useAuth()
    const navigate = useNavigate()

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        })
        setError('')
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setError('')
        setLoading(true)
        
        if (!formData.correo || !formData.contrasena) {
            setError('Por favor, complete todos los campos')
            setLoading(false)
            return
        }
        
        console.log('Enviando login:', { correo: formData.correo })
        
        try {
            const result = await login(formData.correo, formData.contrasena)
            
            if (result.success) {
                console.log('Login exitoso, redirigiendo...')
                navigate('/dashboard')
            } else {
                console.error('Error en login:', result.message)
                setError(result.message || 'Error al iniciar sesión')
            }
        } catch (err) {
            console.error('Excepción en handleSubmit:', err)
            setError('Error inesperado al iniciar sesión')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="login-container">
            {/* Panel izquierdo - Branding */}
            <div className="branding-panel">
                <div className="logo-container">
                    <div className="logo-box">
                        <img src="/images/photos/image.png" alt="Logo Lucky Motos" className="main-logo" />
                    </div>
                </div>
            </div>

            {/* Panel derecho - Formulario */}
            <div className="form-panel">
                <div className="form-container">
                    <div className="top-logo-placeholder">
                        <img src="/images/photos/image.png" alt="Logo Lucky Motos" className="small-logo" />
                    </div>
                    
                    <h1 className="form-title">Log in</h1>
                    
                    <form onSubmit={handleSubmit} className="login-form">
                        {error && <div className="error-message">{error}</div>}
                        
                        <div className="form-group">
                            <label htmlFor="correo" className="form-label">CORREO ELECTRÓNICO</label>
                            <input 
                                type="email" 
                                id="correo" 
                                name="correo" 
                                className="form-input" 
                                placeholder="correo@ejemplo.com"
                                value={formData.correo}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="contrasena" className="form-label">CONTRASEÑA</label>
                            <input 
                                type="password" 
                                id="contrasena" 
                                name="contrasena" 
                                className="form-input" 
                                placeholder="************"
                                value={formData.contrasena}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <button type="submit" className="login-button" disabled={loading}>
                            {loading ? 'Iniciando sesión...' : 'Log in'}
                        </button>
                    </form>

                    <a href="#" className="forgot-password">Forgot your password?</a>
                    
                    <div className="register-link-section">
                        <span>¿No tienes una cuenta? </span>
                        <Link to="/register" className="register-link">Regístrate aquí</Link>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Login

