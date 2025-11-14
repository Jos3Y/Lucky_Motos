import { createContext, useContext, useState, useEffect } from 'react'
import axios from 'axios'

const AuthContext = createContext()

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth debe usarse dentro de AuthProvider')
  }
  return context
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [token, setToken] = useState(localStorage.getItem('token'))
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      // Decodificar token para obtener usuario (simple, sin verificar firma)
      try {
        const payload = JSON.parse(atob(token.split('.')[1]))
        setUser({
          id: payload.idSocio || payload.sub,
          correo: payload.sub,
          roles: payload.roles || []
        })
      } catch (error) {
        console.error('Error decodificando token:', error)
        logout()
      }
    }
    setLoading(false)
  }, [token])

  const login = async (correo, contrasena) => {
    try {
      console.log('Intentando login con:', correo)
      const response = await axios.post('/auth/login', {
        correo,
        contrasena
      })
      console.log('Respuesta del servidor:', response.data)
      const { token, correo: email, roles, idSocio } = response.data

      if (!token) {
        console.error('No se recibió token en la respuesta')
        return {
          success: false,
          message: 'No se recibió token de autenticación'
        }
      }

      setToken(token)
      setUser({ id: idSocio, correo: email, roles })
      localStorage.setItem('token', token)
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      return { success: true }
    } catch (error) {
      console.error('Error en login:', error)
      console.error('Error response:', error.response)
      console.error('Error message:', error.message)

      let errorMessage = 'Error al iniciar sesión'

      if (error.response) {
        // El servidor respondió con un código de error
        errorMessage = error.response.data?.message ||
          error.response.data?.error ||
          `Error ${error.response.status}: ${error.response.statusText}`
      } else if (error.request) {
        // La petición se hizo pero no hubo respuesta
        errorMessage = 'No se pudo conectar con el servidor. Verifica que Spring Boot esté corriendo.'
      } else {
        // Algo más pasó
        errorMessage = error.message || 'Error desconocido'
      }

      return {
        success: false,
        message: errorMessage
      }
    }
  }

  const register = async (data) => {
    try {
      const response = await axios.post('/auth/register', data)
      return { success: true, data: response.data }
    } catch (error) {
      return {
        success: false,
        message: error.response?.data?.message || 'Error al registrar'
      }
    }
  }

  const logout = () => {
    setToken(null)
    setUser(null)
    localStorage.removeItem('token')
    delete axios.defaults.headers.common['Authorization']
  }

  const hasRole = (role) => {
    if (!user || !user.roles) return false
    return user.roles.some(r => r.toUpperCase() === role.toUpperCase())
  }

  const hasAnyRole = (roles) => {
    if (!user || !user.roles) return false
    return roles.some(role => hasRole(role))
  }

  const value = {
    user,
    token,
    loading,
    login,
    register,
    logout,
    hasRole,
    hasAnyRole,
    isAuthenticated: !!token
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

