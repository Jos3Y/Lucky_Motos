import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import { citasAPI, motosAPI, tecnicosAPI, tiposServicioAPI, clientesAPI, comprobantesAPI } from '../services/api'
import Swal from 'sweetalert2'
import './Citas.css'

const Citas = () => {
  const { user, hasRole } = useAuth()
  const [citas, setCitas] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editingCita, setEditingCita] = useState(null)
  const [searchTerm, setSearchTerm] = useState('')
  const [currentPage, setCurrentPage] = useState(1)
  const ITEMS_PER_PAGE = 10
  const [formData, setFormData] = useState({
    // Datos del Cliente
    clienteId: null,
    nombreCompleto: '',
    dni: '',
    telefono: '',
    correo: '',
    direccion: '',
    // Datos de la Moto
    modelo: '',
    placa: '',
    marca: '',
    anio: '',
    motoId: null,
    // Datos de la Cita
    tecnicoId: '',
    tipoServicioId: '',
    fechaCita: '',
    horaCita: '',
    observaciones: '',
    // Pago
    pagoInicial: false,
    montoPagoInicial: 0,
    comprobantePago: null
  })
  const [modelos, setModelos] = useState([])
  const [tecnicos, setTecnicos] = useState([])
  const [tecnicosDisponibles, setTecnicosDisponibles] = useState([])
  const [tiposServicio, setTiposServicio] = useState([])
  const [previewImage, setPreviewImage] = useState(null)
  const [comprobanteUrl, setComprobanteUrl] = useState(null)
  // Pasarela de pago simulada
  const [pagoProcesado, setPagoProcesado] = useState(false)
  const [showPaymentModal, setShowPaymentModal] = useState(false)
  const [metodoPago, setMetodoPago] = useState('tarjeta')
  const [datosPagoSimulado, setDatosPagoSimulado] = useState({
    tarjeta: '',
    vencimiento: '',
    cvv: '',
    correo: '',
    yapeNumero: '',
    yapeCodigo: ''
  })
  const [voucherData, setVoucherData] = useState(null)
  const puedeProcesarPago = formData.pagoInicial && formData.montoPagoInicial > 0 && (!editingCita || !editingCita.pagoInicial)

  const resetPagoSimulado = () => {
    setPagoProcesado(false)
    setShowPaymentModal(false)
    setVoucherData(null)
    setMetodoPago('tarjeta')
    setDatosPagoSimulado({
      tarjeta: '',
      vencimiento: '',
      cvv: '',
      correo: '',
      yapeNumero: '',
      yapeCodigo: ''
    })
  }

  useEffect(() => {
    loadCitas()
    if (showForm) {
      loadFormData()
    }
  }, [showForm])

  useEffect(() => {
    if (hasRole('TECNICO') && user?.id) {
      loadCitasHoy()
    } else {
      loadCitas()
    }
  }, [user])

  useEffect(() => {
    setCurrentPage(1)
  }, [searchTerm, citas.length])

  useEffect(() => {
    if (formData.fechaCita && formData.horaCita) {
      filterTecnicosDisponibles()
    } else {
      setTecnicosDisponibles(tecnicos)
    }
  }, [formData.fechaCita, formData.horaCita, tecnicos])

  // Auto-completar datos de moto cuando se selecciona un modelo
  useEffect(() => {
    if (formData.modelo && formData.modelo !== '') {
      loadDatosMotoPorModelo(formData.modelo)
    }
  }, [formData.modelo])

  const sortCitas = (lista = []) => {
    return [...lista].sort((a, b) => {
      const fechaA = new Date(`${a.fechaCita || ''}T${a.horaCita || '00:00'}`)
      const fechaB = new Date(`${b.fechaCita || ''}T${b.horaCita || '00:00'}`)
      if (isNaN(fechaA)) return 1
      if (isNaN(fechaB)) return -1
      return fechaB - fechaA
    })
  }

  const loadCitas = async () => {
    try {
      setLoading(true)
      let response
      if (hasRole('TECNICO')) {
        response = await citasAPI.getHoy()
      } else if (hasRole('RECEPCIONISTA') || hasRole('ADMIN')) {
        response = await citasAPI.getAll()
      } else {
        response = await citasAPI.getAll()
      }
      const citasData = Array.isArray(response.data) ? response.data : []
      setCitas(sortCitas(citasData))
    } catch (error) {
      console.error('Error cargando citas:', error)
      setCitas([])
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se pudieron cargar las citas',
        confirmButtonColor: '#2c3e50'
      })
    } finally {
      setLoading(false)
    }
  }

  const loadCitasHoy = async () => {
    try {
      setLoading(true)
      const response = await citasAPI.getHoy()
      const citasData = Array.isArray(response.data) ? response.data : []
      setCitas(sortCitas(citasData))
    } catch (error) {
      console.error('Error cargando citas de hoy:', error)
      setCitas([])
    } finally {
      setLoading(false)
    }
  }

  const loadFormData = async () => {
    try {
      const [modelosRes, tecnicosRes, tiposRes] = await Promise.all([
        motosAPI.getModelos().catch(err => {
          console.error('Error cargando modelos:', err)
          return { data: [] }
        }),
        tecnicosAPI.getAll().catch(err => {
          console.error('Error cargando técnicos:', err)
          return { data: [] }
        }),
        tiposServicioAPI.getAll().catch(err => {
          console.error('Error cargando tipos de servicio:', err)
          return { data: [] }
        })
      ])
      
      const modelosData = Array.isArray(modelosRes.data) ? modelosRes.data : []
      const tecnicosData = Array.isArray(tecnicosRes.data) ? tecnicosRes.data : []
      const tiposData = Array.isArray(tiposRes.data) ? tiposRes.data : []
      
      setModelos(modelosData)
      setTecnicos(tecnicosData)
      setTecnicosDisponibles(tecnicosData)
      setTiposServicio(tiposData)
    } catch (error) {
      console.error('Error cargando datos del formulario:', error)
    }
  }

  const loadDatosMotoPorModelo = async (modelo) => {
    try {
      const response = await motosAPI.getByModelo(modelo)
      if (response.data) {
        setFormData(prev => ({
          ...prev,
          marca: response.data.marca || '',
          anio: response.data.anio || '',
          motoId: response.data.id || null
        }))
      }
    } catch (error) {
      console.error('Error cargando datos de moto:', error)
      // Si no se encuentra, limpiar campos
      setFormData(prev => ({
        ...prev,
        marca: '',
        anio: '',
        motoId: null
      }))
    }
  }

  const filterTecnicosDisponibles = () => {
    if (!formData.fechaCita || !formData.horaCita) {
      setTecnicosDisponibles(tecnicos)
      return
    }

    const fecha = new Date(formData.fechaCita)
    const diaSemana = fecha.toLocaleDateString('es-ES', { weekday: 'long' }).toUpperCase()
    const horaCita = formData.horaCita

    const disponibles = tecnicos.filter(tecnico => {
      if (tecnico.estado !== 'DISPONIBLE') {
        return false
      }

      if (tecnico.disponibilidades && tecnico.disponibilidades.length > 0) {
        return tecnico.disponibilidades.some(disp => {
          if (disp.diaSemana !== diaSemana || !disp.disponible) {
            return false
          }
          
          const horaInicio = disp.horaInicio || '00:00'
          const horaFin = disp.horaFin || '23:59'
          
          return horaCita >= horaInicio && horaCita <= horaFin
        })
      }

      return true
    })

    setTecnicosDisponibles(disponibles)
    
    if (formData.tecnicoId && !disponibles.find(t => t.id === parseInt(formData.tecnicoId))) {
      setFormData(prev => ({ ...prev, tecnicoId: '' }))
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    // Validar campos requeridos
    if (!formData.nombreCompleto || formData.nombreCompleto.trim() === '') {
      Swal.fire({
        icon: 'warning',
        title: 'Campo requerido',
        text: 'Por favor ingrese el nombre completo del cliente',
        confirmButtonColor: '#2c3e50'
      })
      return
    }

    if (!formData.telefono || formData.telefono.trim() === '') {
      Swal.fire({
        icon: 'warning',
        title: 'Campo requerido',
        text: 'Por favor ingrese el teléfono del cliente',
        confirmButtonColor: '#2c3e50'
      })
      return
    }

    if (!formData.placa || formData.placa.trim() === '') {
      Swal.fire({
        icon: 'warning',
        title: 'Campo requerido',
        text: 'Por favor ingrese la placa de la moto',
        confirmButtonColor: '#2c3e50'
      })
      return
    }

    // Validar que si hay pago inicial, debe estar procesado
    if (formData.pagoInicial && formData.montoPagoInicial > 0 && !pagoProcesado) {
      Swal.fire({
        icon: 'warning',
        title: 'Pago pendiente',
        text: 'Por favor procese el pago antes de guardar la cita',
        confirmButtonColor: '#2c3e50'
      })
      return
    }

    try {
      // Separar nombre completo en nombre y apellidos
      const nombreParts = formData.nombreCompleto.trim().split(' ')
      const nombre = nombreParts[0] || ''
      const apellidos = nombreParts.slice(1).join(' ') || ''

      // Buscar o crear/actualizar cliente
      let clienteIdFinal = formData.clienteId
      
      if (!clienteIdFinal && formData.dni) {
        try {
          const clienteRes = await clientesAPI.buscarPorDni(formData.dni)
          if (clienteRes.data && clienteRes.data.id) {
            clienteIdFinal = clienteRes.data.id
          }
        } catch (err) {
          console.log('Cliente no encontrado por DNI, se creará uno nuevo')
        }
      }

      // Si no existe, crear el cliente
      if (!clienteIdFinal) {
        const clienteData = {
          nombre: nombre,
          apellidos: apellidos,
          dni: formData.dni || null,
          correo: formData.correo || null,
          telefono: formData.telefono.trim(),
          direccion: formData.direccion || null
        }
        
        const nuevoCliente = await clientesAPI.create(clienteData)
        clienteIdFinal = nuevoCliente.data.id
      } else {
        // Si existe, actualizar la información del cliente (especialmente la dirección)
        try {
          const clienteData = {
            nombre: nombre,
            apellidos: apellidos,
            dni: formData.dni || null,
            correo: formData.correo || null,
            telefono: formData.telefono.trim(),
            direccion: formData.direccion || null
          }
          await clientesAPI.update(clienteIdFinal, clienteData)
        } catch (err) {
          console.error('Error actualizando cliente:', err)
        }
      }

      // Buscar o crear/actualizar moto
      let motoIdFinal = formData.motoId
      if (!motoIdFinal && formData.placa) {
        // Buscar moto por placa
        try {
          const motosRes = await motosAPI.getAll()
          const motoExistente = motosRes.data.find(m => m.placa === formData.placa)
          if (motoExistente) {
            motoIdFinal = motoExistente.id
          } else {
            // Crear nueva moto
            const motoData = {
              clienteId: clienteIdFinal,
              placa: formData.placa.trim(),
              modelo: formData.modelo || '',
              marca: formData.marca || '',
              anio: formData.anio ? parseInt(formData.anio) : null
            }
            const nuevaMoto = await motosAPI.create(motoData)
            motoIdFinal = nuevaMoto.data.id
          }
        } catch (err) {
          console.error('Error con moto:', err)
        }
      } else if (motoIdFinal) {
        // Si existe la moto, actualizar sus datos (especialmente el año)
        try {
          const motoData = {
            clienteId: clienteIdFinal,
            placa: formData.placa.trim(),
            modelo: formData.modelo || '',
            marca: formData.marca || '',
            anio: formData.anio ? parseInt(formData.anio) : null
          }
          await motosAPI.update(motoIdFinal, motoData)
        } catch (err) {
          console.error('Error actualizando moto:', err)
        }
      }

      // Subir comprobante si existe
      let comprobanteUrlFinal = comprobanteUrl
      if (formData.comprobantePago && formData.pagoInicial) {
        try {
          const uploadResponse = await comprobantesAPI.upload(formData.comprobantePago)
          comprobanteUrlFinal = uploadResponse.data.url
        } catch (err) {
          console.error('Error subiendo comprobante:', err)
          Swal.fire({
            icon: 'warning',
            title: 'Advertencia',
            text: 'No se pudo subir el comprobante, pero la cita se guardará sin él',
            confirmButtonColor: '#2c3e50'
          })
        }
      }

      const dataToSend = {
        clienteId: clienteIdFinal,
        motoId: motoIdFinal,
        tecnicoId: formData.tecnicoId ? parseInt(formData.tecnicoId) : null,
        tipoServicioId: formData.tipoServicioId ? parseInt(formData.tipoServicioId) : null,
        fechaCita: formData.fechaCita,
        horaCita: formData.horaCita,
        pagoInicial: formData.pagoInicial || false,
        montoPagoInicial: formData.pagoInicial ? formData.montoPagoInicial : null,
        comprobantePagoUrl: comprobanteUrlFinal,
        observaciones: formData.observaciones || null
      }

      if (editingCita) {
        await citasAPI.update(editingCita.id, dataToSend)
        Swal.fire({
          icon: 'success',
          title: '¡Éxito!',
          text: 'Cita actualizada exitosamente',
          confirmButtonColor: '#2c3e50',
          timer: 2000
        })
      } else {
        await citasAPI.create(dataToSend)
        Swal.fire({
          icon: 'success',
          title: '¡Éxito!',
          text: 'Cita creada exitosamente',
          confirmButtonColor: '#2c3e50',
          timer: 2000
        })
      }
      
      resetForm()
      loadCitas()
    } catch (error) {
      console.error('Error guardando cita:', error)
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: error.response?.data?.message || 'Error al guardar la cita',
        confirmButtonColor: '#2c3e50'
      })
    }
  }

  const handleEdit = (cita) => {
    setEditingCita(cita)
    const nombreCompleto = cita.cliente 
      ? `${cita.cliente.nombre || ''} ${cita.cliente.apellidos || ''}`.trim()
      : ''
    
    const urlComprobante = cita.comprobantePagoUrl || null
    setComprobanteUrl(urlComprobante)
    setPreviewImage(urlComprobante ? `http://localhost:8080${urlComprobante}` : null)
    
    setFormData({
      clienteId: cita.cliente?.id || null,
      nombreCompleto: nombreCompleto,
      dni: cita.cliente?.dni || '',
      telefono: cita.cliente?.telefono || '',
      correo: cita.cliente?.correo || '',
      direccion: cita.cliente?.direccion || '',
      modelo: cita.moto?.modelo || '',
      placa: cita.moto?.placa || '',
      marca: cita.moto?.marca || '',
      anio: cita.moto?.anio?.toString() || '',
      motoId: cita.moto?.id || null,
      tecnicoId: cita.tecnico?.id?.toString() || '',
      tipoServicioId: cita.tipoServicio?.id?.toString() || '',
      fechaCita: cita.fechaCita || '',
      horaCita: cita.horaCita || '',
      observaciones: cita.observaciones || '',
      pagoInicial: cita.pagoInicial || false,
      montoPagoInicial: cita.montoPagoInicial || 0,
      comprobantePago: null
    })
    if (cita.pagoInicial && (cita.montoPagoInicial || 0) > 0) {
      const resumen = {
        codigo: cita.codigoCita ? `LP-${cita.codigoCita}` : `LP-${Math.floor(100000 + Math.random() * 900000)}`,
        metodo: 'Pago registrado',
        monto: cita.montoPagoInicial || 0,
        fecha: cita.fechaCita ? `${cita.fechaCita} ${cita.horaCita || ''}` : new Date().toLocaleString(),
        cliente: nombreCompleto || 'Cliente',
        correo: cita.cliente?.correo || ''
      }
      setVoucherData(resumen)
      setPagoProcesado(true)
      setShowPaymentModal(false)
    } else {
      resetPagoSimulado()
    }
    setShowForm(true)
  }

  const handleDelete = async (cita) => {
    const result = await Swal.fire({
      icon: 'warning',
      title: '¿Está seguro?',
      text: `¿Desea eliminar la cita del ${cita.fechaCita}?`,
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    })

    if (result.isConfirmed) {
      try {
        await citasAPI.delete(cita.id)
        Swal.fire({
          icon: 'success',
          title: '¡Eliminado!',
          text: 'La cita ha sido eliminada',
          confirmButtonColor: '#2c3e50',
          timer: 2000
        })
        loadCitas()
      } catch (error) {
        console.error('Error eliminando cita:', error)
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: error.response?.data?.message || 'No se pudo eliminar la cita',
          confirmButtonColor: '#2c3e50'
        })
      }
    }
  }

  const resetForm = () => {
    setFormData({
      clienteId: null,
      nombreCompleto: '',
      dni: '',
      telefono: '',
      correo: '',
      direccion: '',
      modelo: '',
      placa: '',
      marca: '',
      anio: '',
      motoId: null,
      tecnicoId: '',
      tipoServicioId: '',
      fechaCita: '',
      horaCita: '',
      observaciones: '',
      pagoInicial: false,
      montoPagoInicial: 0,
      comprobantePago: null
    })
    setEditingCita(null)
    setShowForm(false)
    setPreviewImage(null)
    setComprobanteUrl(null)
    resetPagoSimulado()
  }

  const abrirModalPago = () => {
    setShowPaymentModal(true)
    setMetodoPago('tarjeta')
    setDatosPagoSimulado({
      tarjeta: '',
      vencimiento: '',
      cvv: '',
      correo: '',
      yapeNumero: '',
      yapeCodigo: ''
    })
  }

  const procesarPagoSimulado = () => {
    if (metodoPago === 'tarjeta') {
      if (!datosPagoSimulado.tarjeta || !datosPagoSimulado.vencimiento || !datosPagoSimulado.cvv || !datosPagoSimulado.correo) {
        Swal.fire({
          icon: 'warning',
          title: 'Datos incompletos',
          text: 'Completa los datos de la tarjeta para continuar',
          confirmButtonColor: '#2c3e50'
        })
        return
      }
    } else {
      if (!datosPagoSimulado.yapeNumero || !datosPagoSimulado.yapeCodigo) {
        Swal.fire({
          icon: 'warning',
          title: 'Datos incompletos',
          text: 'Completa los datos de Yape para continuar',
          confirmButtonColor: '#2c3e50'
        })
        return
      }
    }

    const codigoOperacion = `LP-${Math.floor(100000 + Math.random() * 900000)}`
    const resumen = {
      codigo: codigoOperacion,
      metodo: metodoPago === 'tarjeta' ? 'Tarjeta' : 'Yape',
      monto: formData.montoPagoInicial,
      fecha: new Date().toLocaleString(),
      cliente: formData.nombreCompleto || 'Cliente',
      correo: datosPagoSimulado.correo || formData.correo || 'Sin correo'
    }

    setVoucherData(resumen)
    setPagoProcesado(true)
    setShowPaymentModal(false)

    Swal.fire({
      icon: 'success',
      title: 'Pago simulado exitoso',
      html: `
        <p>Método: ${resumen.metodo}</p>
        <p>Monto: S/ ${resumen.monto.toFixed(2)}</p>
        <p>Código de operación: <strong>${resumen.codigo}</strong></p>
      `,
      confirmButtonColor: '#2c3e50',
      timer: 2500
    })
  }

  const descargarVoucher = () => {
    if (!voucherData) return
    const canvas = document.createElement('canvas')
    canvas.width = 800
    canvas.height = 500
    const ctx = canvas.getContext('2d')

    ctx.fillStyle = '#ffffff'
    ctx.fillRect(0, 0, canvas.width, canvas.height)

    ctx.fillStyle = '#f0ad4e'
    ctx.fillRect(0, 0, canvas.width, 120)

    ctx.fillStyle = '#2c3e50'
    ctx.font = 'bold 32px Arial'
    ctx.fillText('Lucky Motos S.A.C.', 40, 60)

    ctx.font = '20px Arial'
    ctx.fillText('Voucher de pago simulado', 40, 95)

    ctx.fillStyle = '#2c3e50'
    ctx.font = 'bold 24px Arial'
    ctx.fillText(`Código de operación: ${voucherData.codigo}`, 40, 160)

    ctx.font = '20px Arial'
    ctx.fillText(`Cliente: ${voucherData.cliente}`, 40, 210)
    ctx.fillText(`Correo: ${voucherData.correo}`, 40, 250)
    ctx.fillText(`Método de pago: ${voucherData.metodo}`, 40, 290)
    ctx.fillText(`Monto pagado: S/ ${voucherData.monto.toFixed(2)}`, 40, 330)
    ctx.fillText(`Fecha: ${voucherData.fecha}`, 40, 370)
    ctx.fillText('Este comprobante es solo para fines de simulación.', 40, 420)

    ctx.strokeStyle = '#f0ad4e'
    ctx.lineWidth = 4
    ctx.strokeRect(20, 20, canvas.width - 40, canvas.height - 40)

    const dataUrl = canvas.toDataURL('image/png')
    const link = document.createElement('a')
    link.href = dataUrl
    link.download = `voucher_${voucherData.codigo}.png`
    link.click()
  }

  const normalizedSearch = searchTerm.trim().toLowerCase()
  const filteredCitas = (Array.isArray(citas) ? citas : []).filter((cita) => {
    if (!normalizedSearch) return true
    const cliente = `${cita.cliente?.nombre || ''} ${cita.cliente?.apellidos || ''}`.toLowerCase()
    const correo = (cita.cliente?.correo || '').toLowerCase()
    const moto = `${cita.moto?.marca || ''} ${cita.moto?.modelo || ''}`.toLowerCase()
    const estado = (cita.estado || '').toLowerCase()
    return (
      cliente.includes(normalizedSearch) ||
      correo.includes(normalizedSearch) ||
      moto.includes(normalizedSearch) ||
      estado.includes(normalizedSearch)
    )
  })

  const totalPages = Math.max(1, Math.ceil(filteredCitas.length / ITEMS_PER_PAGE))

  useEffect(() => {
    if (currentPage > totalPages) {
      setCurrentPage(totalPages)
    }
  }, [totalPages, currentPage])

  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE
  const paginatedCitas = filteredCitas.slice(startIndex, startIndex + ITEMS_PER_PAGE)

  const goToPage = (page) => {
    const next = Math.max(1, Math.min(page, totalPages))
    setCurrentPage(next)
  }

  const handleFileChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      if (file.size > 5 * 1024 * 1024) { // 5MB
        Swal.fire({
          icon: 'warning',
          title: 'Archivo muy grande',
          text: 'El archivo no debe exceder 5MB',
          confirmButtonColor: '#2c3e50'
        })
        return
      }
      
      // Validar tipo de archivo
      if (!file.type.startsWith('image/') && file.type !== 'application/pdf') {
        Swal.fire({
          icon: 'warning',
          title: 'Tipo de archivo no válido',
          text: 'Solo se permiten imágenes y PDFs',
          confirmButtonColor: '#2c3e50'
        })
        return
      }
      
      setFormData(prev => ({ ...prev, comprobantePago: file }))
      
      // Crear previsualización si es imagen
      if (file.type.startsWith('image/')) {
        const reader = new FileReader()
        reader.onloadend = () => {
          setPreviewImage(reader.result)
        }
        reader.readAsDataURL(file)
      } else {
        setPreviewImage(null)
      }
    }
  }

  const getEstadoBadgeClass = (estado) => {
    const estados = {
      PENDIENTE: 'badge-warning',
      CONFIRMADA: 'badge-info',
      EN_PROCESO: 'badge-primary',
      COMPLETADA: 'badge-success',
      CANCELADA: 'badge-danger'
    }
    return estados[estado] || 'badge-secondary'
  }

  if (loading) {
    return <div className="loading">Cargando citas...</div>
  }

  return (
    <div className="citas-page">
      <div className="page-header">
        <h1>Gestión de Citas</h1>
        {(hasRole('ADMIN') || hasRole('RECEPCIONISTA')) && (
          <button 
            className="btn-primary" 
            onClick={() => {
              if (showForm) {
                resetForm()
              } else {
                setEditingCita(null)
                setShowForm(true)
                loadFormData()
              }
            }}
          >
            {showForm ? 'Cancelar' : 'Registrar nueva cita'}
          </button>
        )}
      </div>

      {showForm && (
        <div className="cita-form-container">
          <h2>{editingCita ? 'Editar cita' : 'Registrar nueva cita'}</h2>
          <form onSubmit={handleSubmit} className="cita-form">
            {/* SECCIÓN: DATOS DEL CLIENTE */}
            <div className="form-section">
              <h3 className="section-title">📋 Datos del Cliente</h3>
              <div className="form-row">
                <div className="form-group">
                  <label>Nombre completo *</label>
                  <input 
                    type="text" 
                    value={formData.nombreCompleto}
                    onChange={(e) => setFormData({ ...formData, nombreCompleto: e.target.value })}
                    placeholder="Ej: Juan Pérez García"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>DNI</label>
                  <input 
                    type="text" 
                    value={formData.dni}
                    onChange={(e) => setFormData({ ...formData, dni: e.target.value })}
                    placeholder="Ingrese el DNI"
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Teléfono / WhatsApp *</label>
                  <input 
                    type="tel" 
                    value={formData.telefono}
                    onChange={(e) => setFormData({ ...formData, telefono: e.target.value })}
                    placeholder="Ingrese el teléfono"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Correo electrónico</label>
                  <input 
                    type="email" 
                    value={formData.correo}
                    onChange={(e) => setFormData({ ...formData, correo: e.target.value })}
                    placeholder="Ingrese el correo"
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group full-width">
                  <label>Dirección</label>
                  <input 
                    type="text" 
                    value={formData.direccion}
                    onChange={(e) => setFormData({ ...formData, direccion: e.target.value })}
                    placeholder="Ingrese la dirección completa"
                  />
                </div>
              </div>
            </div>

            {/* SECCIÓN: DATOS DE LA MOTO */}
            <div className="form-section">
              <h3 className="section-title">🏍️ Datos de la Moto</h3>
              <div className="form-row">
                <div className="form-group">
                  <label>Modelo *</label>
                  <select
                    value={formData.modelo}
                    onChange={(e) => setFormData({ ...formData, modelo: e.target.value, marca: '', anio: '' })}
                    required
                  >
                    <option value="">- Seleccione un modelo -</option>
                    {Array.isArray(modelos) && modelos.map(modelo => (
                      <option key={modelo} value={modelo}>
                        {modelo}
                      </option>
                    ))}
                  </select>
                  {modelos.length === 0 && (
                    <small className="error-text">No hay modelos disponibles</small>
                  )}
                </div>
                <div className="form-group">
                  <label>Placa *</label>
                  <input 
                    type="text" 
                    value={formData.placa}
                    onChange={(e) => setFormData({ ...formData, placa: e.target.value.toUpperCase() })}
                    placeholder="Ej: ABC-123"
                    required
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Marca</label>
                  <input 
                    type="text" 
                    value={formData.marca}
                    onChange={(e) => setFormData({ ...formData, marca: e.target.value })}
                    placeholder="Se completa automáticamente"
                    readOnly
                    className="readonly-input"
                  />
                </div>
                <div className="form-group">
                  <label>Año</label>
                  <input 
                    type="number" 
                    value={formData.anio}
                    onChange={(e) => setFormData({ ...formData, anio: e.target.value })}
                    placeholder="Ingrese el año"
                    min="1900"
                    max={new Date().getFullYear() + 1}
                  />
                </div>
              </div>
            </div>

            {/* SECCIÓN: DATOS DE LA CITA */}
            <div className="form-section">
              <h3 className="section-title">📅 Datos de la Cita</h3>
              <div className="form-row">
                <div className="form-group">
                  <label>Fecha *</label>
                  <input
                    type="date"
                    value={formData.fechaCita}
                    onChange={(e) => setFormData({ ...formData, fechaCita: e.target.value })}
                    required
                    min={new Date().toISOString().split('T')[0]}
                  />
                </div>
                <div className="form-group">
                  <label>Hora *</label>
                  <input
                    type="time"
                    value={formData.horaCita}
                    onChange={(e) => setFormData({ ...formData, horaCita: e.target.value })}
                    required
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Técnico asignado</label>
                  <select
                    value={formData.tecnicoId}
                    onChange={(e) => setFormData({ ...formData, tecnicoId: e.target.value })}
                  >
                    <option value="">- Seleccione -</option>
                    {Array.isArray(tecnicosDisponibles) && tecnicosDisponibles.map(tecnico => (
                      <option key={tecnico.id} value={tecnico.id}>
                        {tecnico.nombre || ''} {tecnico.apellidos || ''}
                        {formData.fechaCita && formData.horaCita ? ' ✓ Disponible' : ''}
                      </option>
                    ))}
                  </select>
                  {tecnicosDisponibles.length === 0 && formData.fechaCita && formData.horaCita && (
                    <small className="warning-text">No hay técnicos disponibles para esta fecha y hora</small>
                  )}
                  {tecnicos.length === 0 && (
                    <small className="error-text">No hay técnicos registrados</small>
                  )}
                </div>
                <div className="form-group">
                  <label>Tipo de servicio</label>
                  <select
                    value={formData.tipoServicioId}
                    onChange={(e) => setFormData({ ...formData, tipoServicioId: e.target.value })}
                  >
                    <option value="">- Seleccione -</option>
                    {Array.isArray(tiposServicio) && tiposServicio.length > 0 ? (
                      tiposServicio.map(tipo => (
                        <option key={tipo.id_tipo_servicio || tipo.id} value={tipo.id_tipo_servicio || tipo.id}>
                          {tipo.nombre}
                        </option>
                      ))
                    ) : (
                      <option disabled>No hay tipos de servicio disponibles</option>
                    )}
                  </select>
                  {(!Array.isArray(tiposServicio) || tiposServicio.length === 0) && (
                    <small className="error-text">No hay tipos de servicio disponibles</small>
                  )}
                </div>
              </div>
              <div className="form-row">
                <div className="form-group full-width">
                  <label>Observaciones</label>
                  <textarea
                    value={formData.observaciones}
                    onChange={(e) => setFormData({ ...formData, observaciones: e.target.value })}
                    rows="3"
                    placeholder="Ingrese observaciones adicionales"
                  />
                </div>
              </div>
            </div>

            {/* SECCIÓN: PAGO */}
            <div className="form-section">
              <h3 className="section-title">💳 Pago Inicial</h3>
              <div className="form-row">
                <div className="form-group">
                  <label>¿Realizó pago inicial?</label>
                  <div className="radio-group">
                    <label>
                      <input
                        type="radio"
                        checked={formData.pagoInicial}
                        onChange={() => setFormData({ ...formData, pagoInicial: true })}
                      />
                      Sí
                    </label>
                    <label>
                      <input
                        type="radio"
                        checked={!formData.pagoInicial}
                        onChange={() => setFormData({ ...formData, pagoInicial: false, montoPagoInicial: 0 })}
                      />
                      No
                    </label>
                  </div>
                </div>
                {formData.pagoInicial && (
                  <div className="form-group">
                    <label>Monto del pago inicial</label>
                    <input
                      type="number"
                      step="0.01"
                      min="0"
                      value={formData.montoPagoInicial}
                      onChange={(e) => setFormData({ ...formData, montoPagoInicial: parseFloat(e.target.value) || 0 })}
                      placeholder="0.00"
                    />
                  </div>
                )}
              </div>
              {puedeProcesarPago && (
                <>
                  {/* PASARELA DE PAGO CULQI */}
                  {!pagoProcesado ? (
                    <div className="payment-gateway">
                      <h4 className="payment-title">Pago Inicial</h4>
                      <p className="payment-amount">Monto a pagar: <strong>S/ {formData.montoPagoInicial.toFixed(2)}</strong></p>
                      <div className="culqi-info">
                        <p style={{ fontSize: '0.9rem', color: '#666', marginBottom: '10px' }}>
                          Utilizaremos Culqi para procesar tu pago de forma segura.
                        </p>
                        <p style={{ fontSize: '0.85rem', color: '#999', fontStyle: 'italic' }}>
                          💡 Modo de prueba: Usa tarjeta de prueba 4111 1111 1111 1111
                        </p>
                      </div>
                      <button
                        type="button"
                        className="btn-pay"
                        onClick={abrirModalPago}
                      >
                        💳 Pagar
                      </button>
                    </div>
                  ) : (
                    <div className="payment-success">
                      <div className="success-icon">✅</div>
                      <p className="success-message">Pago procesado exitosamente</p>
                      {voucherData && (
                        <p className="success-details">
                          {voucherData.metodo}: código {voucherData.codigo} - {voucherData.cliente}
                        </p>
                      )}
                      {puedeProcesarPago && (
                        <button
                          type="button"
                          className="btn-secondary"
                          onClick={() => {
                            resetPagoSimulado()
                          }}
                        >
                          Volver a pagar
                        </button>
                      )}
                      {voucherData && (
                        <button
                          type="button"
                          className="btn-primary"
                          onClick={descargarVoucher}
                        >
                          📄 Descargar voucher
                        </button>
                      )}
                    </div>
                  )}
                </>
              )}

              {formData.pagoInicial && pagoProcesado && (
                <div className="form-row">
                  <div className="form-group full-width">
                    <label>Comprobante de pago</label>
                    <div className="file-upload-container">
                      <input
                        type="file"
                        id="comprobantePago"
                        accept="image/*,.pdf"
                        onChange={handleFileChange}
                        className="file-input"
                      />
                      <label htmlFor="comprobantePago" className="file-label">
                        {formData.comprobantePago 
                          ? `📄 ${formData.comprobantePago.name}` 
                          : comprobanteUrl 
                            ? '📄 Cambiar comprobante'
                            : '📎 Subir comprobante'}
                      </label>
                    </div>
                    {formData.comprobantePago && (
                      <small className="info-text">
                        Archivo seleccionado: {formData.comprobantePago.name} 
                        ({(formData.comprobantePago.size / 1024).toFixed(2)} KB)
                      </small>
                    )}
                    {comprobanteUrl && !formData.comprobantePago && (
                      <small className="info-text">
                        Comprobante actual: {comprobanteUrl.split('/').pop()}
                      </small>
                    )}
                    {/* Previsualización de imagen */}
                    {previewImage && (
                      <div className="preview-container">
                        <img 
                          src={previewImage} 
                          alt="Vista previa del comprobante" 
                          className="preview-image"
                        />
                        <button
                          type="button"
                          className="btn-remove-preview"
                          onClick={() => {
                            setPreviewImage(null)
                            setFormData(prev => ({ ...prev, comprobantePago: null }))
                            if (!editingCita) {
                              setComprobanteUrl(null)
                            }
                          }}
                        >
                          ✕ Eliminar
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              )}
            </div>

            <div className="form-actions">
              <button type="submit" className="btn-primary">
                {editingCita ? 'Actualizar cita' : 'Guardar cita'}
              </button>
              <button type="button" className="btn-secondary" onClick={resetForm}>
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="citas-controls">
        <input
          type="text"
          className="citas-search"
          placeholder="Filtrar por cliente, moto o estado"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>

      <div className="citas-table-container">
        <h2>Citas registradas</h2>
        <table className="citas-table">
          <thead>
            <tr>
              <th>Cliente</th>
              <th>Moto</th>
              <th>Técnico</th>
              <th>Fecha</th>
              <th>Hora</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {filteredCitas.length === 0 ? (
              <tr>
                <td colSpan="7" className="no-data">No hay citas que coincidan con el filtro</td>
              </tr>
            ) : (
              paginatedCitas.map(cita => (
                <tr key={cita.id}>
                  <td>
                    {cita.cliente?.nombre ? `${cita.cliente.nombre} ${cita.cliente.apellidos || ''}`.trim() : cita.cliente?.correo || 'N/A'}
                  </td>
                  <td>{cita.moto ? `${cita.moto.marca} ${cita.moto.modelo}` : 'Sin moto'}</td>
                  <td>{cita.tecnico ? `${cita.tecnico.nombre} ${cita.tecnico.apellidos || ''}` : 'Sin asignar'}</td>
                  <td>{cita.fechaCita || 'N/A'}</td>
                  <td>{cita.horaCita || 'N/A'}</td>
                  <td>
                    <span className={`badge ${getEstadoBadgeClass(cita.estado)}`}>
                      {cita.estado}
                    </span>
                  </td>
                  <td>
                    <div className="action-buttons">
                      {(hasRole('ADMIN') || hasRole('RECEPCIONISTA')) && (
                        <>
                          <button 
                            className="btn-edit" 
                            onClick={() => handleEdit(cita)}
                            title="Editar"
                          >
                            ✏️
                          </button>
                          {hasRole('ADMIN') && (
                            <button 
                              className="btn-delete" 
                              onClick={() => handleDelete(cita)}
                              title="Eliminar"
                            >
                              🗑️
                            </button>
                          )}
                        </>
                      )}
                      {hasRole('TECNICO') && (
                        <button 
                          className="btn-edit" 
                          onClick={() => handleEdit(cita)}
                          title="Cambiar estado"
                        >
                          📝 Cambiar estado
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="pagination">
          <button
            type="button"
            onClick={() => goToPage(currentPage - 1)}
            disabled={currentPage === 1}
          >
            ← Anterior
          </button>
          <span>Página {currentPage} de {totalPages}</span>
          <button
            type="button"
            onClick={() => goToPage(currentPage + 1)}
            disabled={currentPage === totalPages}
          >
            Siguiente →
          </button>
        </div>
      )}

      {showPaymentModal && (
        <div className="payment-modal-overlay">
          <div className="payment-modal">
            <div className="payment-modal-header">
              <h4>Simulación de pago</h4>
              <button type="button" className="close-modal" onClick={() => setShowPaymentModal(false)}>✕</button>
            </div>
            <p className="modal-amount">Monto: <strong>S/ {formData.montoPagoInicial.toFixed(2)}</strong></p>
            <div className="modal-tabs">
              <button
                type="button"
                className={`modal-tab ${metodoPago === 'tarjeta' ? 'active' : ''}`}
                onClick={() => setMetodoPago('tarjeta')}
              >
                Tarjeta
              </button>
              <button
                type="button"
                className={`modal-tab ${metodoPago === 'yape' ? 'active' : ''}`}
                onClick={() => setMetodoPago('yape')}
              >
                Yape
              </button>
            </div>

            {metodoPago === 'tarjeta' ? (
              <div className="modal-form">
                <div className="form-group full-width">
                  <label>Número de tarjeta</label>
                  <input
                    type="text"
                    value={datosPagoSimulado.tarjeta}
                    onChange={(e) => {
                      const value = e.target.value.replace(/\s/g, '').replace(/\D/g, '')
                      const formatted = value.match(/.{1,4}/g)?.join(' ') || value
                      setDatosPagoSimulado(prev => ({ ...prev, tarjeta: formatted }))
                    }}
                    placeholder="1234 5678 9012 3456"
                    maxLength="19"
                  />
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>Vencimiento</label>
                    <input
                      type="text"
                      value={datosPagoSimulado.vencimiento}
                      onChange={(e) => {
                        let value = e.target.value.replace(/\D/g, '')
                        if (value.length >= 2) {
                          value = value.substring(0, 2) + '/' + value.substring(2, 4)
                        }
                        setDatosPagoSimulado(prev => ({ ...prev, vencimiento: value }))
                      }}
                      placeholder="MM/AA"
                      maxLength="5"
                    />
                  </div>
                  <div className="form-group">
                    <label>CVV</label>
                    <input
                      type="text"
                      value={datosPagoSimulado.cvv}
                      onChange={(e) => {
                        const value = e.target.value.replace(/\D/g, '').substring(0, 3)
                        setDatosPagoSimulado(prev => ({ ...prev, cvv: value }))
                      }}
                      placeholder="123"
                      maxLength="3"
                    />
                  </div>
                </div>
                <div className="form-group full-width">
                  <label>Correo electrónico</label>
                  <input
                    type="email"
                    value={datosPagoSimulado.correo}
                    onChange={(e) => setDatosPagoSimulado(prev => ({ ...prev, correo: e.target.value }))}
                    placeholder="cliente@correo.com"
                  />
                </div>
              </div>
            ) : (
              <div className="modal-form">
                <div className="form-group full-width">
                  <label>Número de Yape</label>
                  <input
                    type="text"
                    value={datosPagoSimulado.yapeNumero}
                    onChange={(e) => {
                      const value = e.target.value.replace(/\D/g, '').substring(0, 9)
                      setDatosPagoSimulado(prev => ({ ...prev, yapeNumero: value }))
                    }}
                    placeholder="9XXXXXXXX"
                  />
                </div>
                <div className="form-group full-width">
                  <label>Código de aprobación</label>
                  <input
                    type="text"
                    value={datosPagoSimulado.yapeCodigo}
                    onChange={(e) => setDatosPagoSimulado(prev => ({ ...prev, yapeCodigo: e.target.value.substring(0, 6) }))}
                    placeholder="ABC123"
                  />
                </div>
              </div>
            )}

            <div className="modal-footer">
              <button type="button" className="btn-secondary" onClick={() => setShowPaymentModal(false)}>Cancelar</button>
              <button type="button" className="btn-primary" onClick={procesarPagoSimulado}>Procesar pago</button>
            </div>
            <p className="modal-note">* Simulación de pago. No se realiza ningún cargo real.</p>
          </div>
        </div>
      )}
    </div>
  )
}

export default Citas
