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
  getHoy: () => axios.get('/api/citas/hoy'),
  getHoyPorTecnico: (tecnicoId) => axios.get(`/api/citas/tecnico/${tecnicoId}/hoy`),
  delete: (id) => axios.delete(`/api/citas/${id}`)
}

// API de Clientes
export const clientesAPI = {
  getAll: () => axios.get('/api/clientes'),
  getById: (id) => axios.get(`/api/clientes/${id}`),
  create: (data) => axios.post('/api/clientes', data),
  update: (id, data) => axios.put(`/api/clientes/${id}`, data),
  delete: (id) => axios.delete(`/api/clientes/${id}`),
  buscarPorDni: (dni) => axios.get(`/api/clientes/buscar/dni/${dni}`)
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
  updateDisponibilidad: (id, data) => axios.put(`/api/tecnicos/${id}/disponibilidad`, data),
  create: (data) => axios.post('/api/tecnicos', data),
  update: (id, data) => axios.put(`/api/tecnicos/${id}`, data),
  changeEstado: (id, estado) => axios.patch(`/api/tecnicos/${id}/estado`, null, { params: { estado } }),
  delete: (id) => axios.delete(`/api/tecnicos/${id}`),
  resetHorario: () => axios.put('/api/tecnicos/reset-horario')
}

// API de Tipos de Servicio
export const tiposServicioAPI = {
  getAll: () => axios.get('/api/tipos-servicio'),
  getById: (id) => axios.get(`/api/tipos-servicio/${id}`),
  create: (data) => axios.post('/api/tipos-servicio', data),
  update: (id, data) => axios.put(`/api/tipos-servicio/${id}`, data),
  delete: (id) => axios.delete(`/api/tipos-servicio/${id}`)
}

// API de Motos
export const motosAPI = {
  getAll: () => axios.get('/motos/all'),
  getById: (id) => axios.get(`/motos/${id}`),
  create: (data) => axios.post('/motos/save', data),
  update: (id, data) => axios.put(`/motos/update/${id}`, data),
  getActivas: () => axios.get('/motos/activas'),
  getModelos: () => axios.get('/motos/modelos'),
  getByModelo: (modelo) => axios.get(`/motos/modelo/${encodeURIComponent(modelo)}`)
}

// API de Socios/Usuarios
export const sociosAPI = {
  getAll: () => axios.get('/socio'),
  getById: (id) => axios.get(`/socio/${id}`),
  create: (data) => axios.post('/socio/save', data),
  update: (id, data) => axios.put(`/socio/update/${id}`, data),
  delete: (id) => axios.delete(`/socio/${id}`)
}

// API de Reportes
export const reportesAPI = {
  getResumen: (periodo = 'ULTIMOS_6_MESES') => axios.get('/api/reportes/resumen', { params: { periodo } }),
  downloadExcel: (periodo = 'ULTIMOS_6_MESES') =>
    axios.get('/api/reportes/export/excel', { params: { periodo }, responseType: 'blob' })
}

// API de Comprobantes
export const comprobantesAPI = {
  upload: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return axios.post('/api/comprobantes/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  view: (filename) => axios.get(`/api/comprobantes/view/${filename}`, { responseType: 'blob' }),
  delete: (filename) => axios.delete(`/api/comprobantes/${filename}`)
}

export default axios

