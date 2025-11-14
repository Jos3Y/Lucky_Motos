import axios from 'axios'

// Configurar axios - sin baseURL para usar rutas relativas con proxy
// axios.defaults.baseURL = '/api' // Comentado para usar proxy de Vite
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// API de Citas
export const citasAPI = {
  getAll: () => axios.get('/api/citas'),
  getById: (id) => axios.get(`/api/citas/${id}`),
  create: (data) => axios.post('/api/citas', data),
  update: (id, data) => axios.put(`/api/citas/${id}`, data),
  updateEstado: (id, data) => axios.put(`/api/citas/${id}/estado`, data),
  getByCliente: (clienteId) => axios.get(`/api/citas/cliente/${clienteId}`),
  getByTecnico: (tecnicoId) => axios.get(`/api/citas/tecnico/${tecnicoId}`),
  getByEstado: (estado) => axios.get(`/api/citas/estado/${estado}`),
  delete: (id) => axios.delete(`/api/citas/${id}`)
}

// API de Repuestos
export const repuestosAPI = {
  getAll: (filters) => axios.get('/api/repuestos', { params: filters }),
  getById: (id) => axios.get(`/api/repuestos/${id}`),
  create: (data) => axios.post('/api/repuestos', data),
  update: (id, data) => axios.put(`/api/repuestos/${id}`, data),
  delete: (id) => axios.delete(`/api/repuestos/${id}`),
  getBajoStock: () => axios.get('/api/repuestos/bajo-stock')
}

// API de Técnicos
export const tecnicosAPI = {
  getAll: () => axios.get('/api/tecnicos'),
  getById: (id) => axios.get(`/api/tecnicos/${id}`),
  getBySocioId: (socioId) => axios.get(`/api/tecnicos/socio/${socioId}`),
  getDisponibles: () => axios.get('/api/tecnicos/disponibles'),
  updateDisponibilidad: (id, data) => axios.put(`/api/tecnicos/${id}/disponibilidad`, data)
}

// API de Tipos de Servicio
export const tiposServicioAPI = {
  getAll: () => axios.get('/api/tipos-servicio'),
  getById: (id) => axios.get(`/api/tipos-servicio/${id}`)
}

// API de Motos
export const motosAPI = {
  getAll: () => axios.get('/api/moto'),
  getById: (id) => axios.get(`/api/moto/${id}`),
  create: (data) => axios.post('/api/moto', data),
  update: (id, data) => axios.put(`/api/moto/${id}`, data)
}

// API de Socios/Usuarios
export const sociosAPI = {
  getAll: () => axios.get('/api/socio'),
  getById: (id) => axios.get(`/api/socio/${id}`)
}

export default axios

