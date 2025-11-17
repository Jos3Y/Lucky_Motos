import React, { useEffect, useState } from 'react'
import { citasAPI, repuestosAPI, tecnicosAPI } from '../services/api'
import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
    LineChart,
    Line,
    PieChart,
    Pie,
    Cell,
    Legend
} from 'recharts'


export default function DashboardCharts() {
    const [barData, setBarData] = useState([])
    const [lineData, setLineData] = useState([])
    const [pieData, setPieData] = useState([])
    const [pieStatusData, setPieStatusData] = useState([])
    const [barMonthData, setBarMonthData] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

    useEffect(() => {
        async function fetchData() {
            try {
                setLoading(true)
                setError(null)
                
                // Mejores trabajadores: técnicos con más citas completadas
                const citasRes = await citasAPI.getAll().catch(err => {
                    console.error('Error cargando citas:', err)
                    return { data: [] }
                })
                const tecnicosRes = await tecnicosAPI.getAll().catch(err => {
                    console.error('Error cargando técnicos:', err)
                    return { data: [] }
                })
                const repuestosRes = await repuestosAPI.getAll().catch(err => {
                    console.error('Error cargando repuestos:', err)
                    return { data: [] }
                })
                
                console.log('Datos recibidos:', {
                    citas: citasRes.data?.length || 0,
                    tecnicos: tecnicosRes.data?.length || 0,
                    repuestos: repuestosRes.data?.length || 0,
                    citasData: citasRes.data,
                    tecnicosData: tecnicosRes.data,
                    repuestosData: repuestosRes.data
                })
                
                // Verificar si hay errores en las respuestas
                if (!citasRes.data || !Array.isArray(citasRes.data)) {
                    console.warn('Citas: respuesta inválida o vacía')
                }
                if (!tecnicosRes.data || !Array.isArray(tecnicosRes.data)) {
                    console.warn('Técnicos: respuesta inválida o vacía')
                }
                if (!repuestosRes.data || !Array.isArray(repuestosRes.data)) {
                    console.warn('Repuestos: respuesta inválida o vacía')
                }

                // BarChart: técnicos con más citas completadas
                const trabajosPorTecnico = {}
                if (citasRes.data && Array.isArray(citasRes.data)) {
                citasRes.data.forEach(cita => {
                    if (cita.estado === 'COMPLETADA' && cita.tecnico) {
                        const nombre = cita.tecnico.nombre || 'Sin nombre'
                        trabajosPorTecnico[nombre] = (trabajosPorTecnico[nombre] || 0) + 1
                    }
                })
                }
                const bar = Object.entries(trabajosPorTecnico).map(([name, trabajos]) => ({ name, trabajos }))
                setBarData(bar.length ? bar : [{ name: 'Sin datos', trabajos: 0 }])

                // LineChart: citas completadas por día
                const trabajosPorDia = {}
                if (citasRes.data && Array.isArray(citasRes.data)) {
                citasRes.data.forEach(cita => {
                    if (cita.estado === 'COMPLETADA' && cita.fechaCita) {
                        trabajosPorDia[cita.fechaCita] = (trabajosPorDia[cita.fechaCita] || 0) + 1
                    }
                })
                }
                const line = Object.entries(trabajosPorDia).map(([fecha, valor]) => ({ fecha, valor }))
                setLineData(line.length ? line : [{ fecha: 'Sin datos', valor: 0 }])

                // PieChart: repuestos más usados (por cantidad en repuestos) - Solo top 3
                const repuestoCount = {}
                if (repuestosRes.data && Array.isArray(repuestosRes.data)) {
                repuestosRes.data.forEach(r => {
                        if (r.nombre) {
                    repuestoCount[r.nombre] = (repuestoCount[r.nombre] || 0) + (r.stock || 0)
                        }
                })
                }
                const pieAll = Object.entries(repuestoCount)
                    .map(([name, value]) => ({ name, value }))
                    .sort((a, b) => b.value - a.value) // Ordenar de mayor a menor
                // Tomar solo los 3 primeros
                const pie = pieAll.slice(0, 3)
                setPieData(pie.length ? pie : [{ name: 'Sin datos', value: 0 }])

                // PieChart: Citas por estado
                const statusCount = {}
                if (citasRes.data && Array.isArray(citasRes.data)) {
                citasRes.data.forEach(cita => {
                        if (cita.estado) {
                    statusCount[cita.estado] = (statusCount[cita.estado] || 0) + 1
                        }
                })
                }
                const pieStatus = Object.entries(statusCount).map(([name, value]) => ({ name, value }))
                setPieStatusData(pieStatus.length ? pieStatus : [{ name: 'Sin datos', value: 0 }])

                // BarChart: Citas por mes
                const monthCount = {}
                if (citasRes.data && Array.isArray(citasRes.data)) {
                citasRes.data.forEach(cita => {
                        if (cita.fechaCita) {
                            try {
                    const month = new Date(cita.fechaCita).toLocaleString('es-ES', { month: 'long' });
                    monthCount[month] = (monthCount[month] || 0) + 1
                            } catch (e) {
                                console.warn('Error parseando fecha:', cita.fechaCita)
                            }
                        }
                })
                }
                const barMonth = Object.entries(monthCount).map(([name, value]) => ({ name, value }))
                setBarMonthData(barMonth.length ? barMonth : [{ name: 'Sin datos', value: 0 }])

            } catch (err) {
                console.error('Error general cargando datos:', err)
                setBarData([{ name: 'Sin datos', trabajos: 0 }])
                setLineData([{ fecha: 'Sin datos', valor: 0 }])
                setPieData([{ name: 'Sin datos', value: 0 }])
                setPieStatusData([{ name: 'Sin datos', value: 0 }])
                setBarMonthData([{ name: 'Sin datos', value: 0 }])
                setError('No se pudo cargar los datos de los gráficos. Verifica que el backend esté corriendo y las APIs respondan correctamente.')
            } finally {
                setLoading(false)
            }
        }
        fetchData()
    }, [])

    const COLORS = ['#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe', '#00f2fe', '#43e97b', '#38f9d7', '#fa709a', '#fee140'];
    const STATUS_COLORS = {
        'PENDIENTE': '#fbbf24',
        'CONFIRMADA': '#3b82f6',
        'EN_PROCESO': '#10b981',
        'COMPLETADA': '#8b5cf6',
        'CANCELADA': '#ef4444'
    };

    if (loading) return (
        <div className="dashboard-charts">
            <div style={{ textAlign: 'center', padding: '40px', color: '#64748b', fontSize: '1.1rem' }}>
                Cargando gráficos...
            </div>
        </div>
    )

    return (
        <div className="dashboard-charts">
            {error && (
                <div style={{
                    color: '#ef4444',
                    marginBottom: 24,
                    fontWeight: '600',
                    textAlign: 'center',
                    padding: '16px',
                    background: '#fef2f2',
                    borderRadius: '12px',
                    border: '1px solid #fecaca'
                }}>
                    {error}
                </div>
            )}
            <div className="chart-card">
                <h3>Mejores trabajadores (última semana)</h3>
                <ResponsiveContainer width="100%" height={280}>
                    <BarChart data={barData} margin={{ top: 20, right: 20, left: 0, bottom: 10 }}>
                        <XAxis dataKey="name" tick={{ fill: '#64748b', fontSize: 12 }} />
                        <YAxis tick={{ fill: '#64748b', fontSize: 12 }} />
                        <Tooltip
                            contentStyle={{
                                backgroundColor: '#fff',
                                border: '1px solid #e2e8f0',
                                borderRadius: '8px',
                                boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
                            }}
                        />
                        <Bar dataKey="trabajos" fill="url(#colorGradient1)" radius={[8, 8, 0, 0]} />
                        <defs>
                            <linearGradient id="colorGradient1" x1="0" y1="0" x2="0" y2="1">
                                <stop offset="0%" stopColor="#667eea" />
                                <stop offset="100%" stopColor="#764ba2" />
                            </linearGradient>
                        </defs>
                    </BarChart>
                </ResponsiveContainer>
            </div>

            <div className="chart-card">
                <h3>Días más trabajados (mes)</h3>
                <ResponsiveContainer width="100%" height={280}>
                    <LineChart data={lineData} margin={{ top: 20, right: 20, left: 0, bottom: 10 }}>
                        <XAxis dataKey="fecha" tick={{ fill: '#64748b', fontSize: 12 }} />
                        <YAxis tick={{ fill: '#64748b', fontSize: 12 }} />
                        <Tooltip
                            contentStyle={{
                                backgroundColor: '#fff',
                                border: '1px solid #e2e8f0',
                                borderRadius: '8px',
                                boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
                            }}
                        />
                        <Line
                            type="monotone"
                            dataKey="valor"
                            stroke="url(#colorGradient2)"
                            strokeWidth={3}
                            dot={{ fill: '#43e97b', r: 6 }}
                            activeDot={{ r: 8 }}
                        />
                        <defs>
                            <linearGradient id="colorGradient2" x1="0" y1="0" x2="1" y2="0">
                                <stop offset="0%" stopColor="#43e97b" />
                                <stop offset="100%" stopColor="#38f9d7" />
                            </linearGradient>
                        </defs>
                    </LineChart>
                </ResponsiveContainer>
            </div>

            <div className="chart-card">
                <h3>Repuestos más usados</h3>
                <ResponsiveContainer width="100%" height={280}>
                    <PieChart>
                        <Pie
                            data={pieData}
                            dataKey="value"
                            nameKey="name"
                            cx="50%"
                            cy="50%"
                            outerRadius={90}
                            label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                            labelLine={false}
                        >
                            {pieData.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Tooltip
                            contentStyle={{
                                backgroundColor: '#fff',
                                border: '1px solid #e2e8f0',
                                borderRadius: '8px',
                                boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
                            }}
                        />
                        <Legend
                            wrapperStyle={{ paddingTop: '20px' }}
                            iconType="circle"
                        />
                    </PieChart>
                </ResponsiveContainer>
            </div>

            <div className="charts-row-full">
                <div className="chart-card chart-full-width">
                <h3>Citas por estado</h3>
                    <ResponsiveContainer width="100%" height={280}>
                    <PieChart>
                            <Pie
                                data={pieStatusData}
                                dataKey="value"
                                nameKey="name"
                                cx="50%"
                                cy="50%"
                                outerRadius={90}
                                label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                                labelLine={false}
                            >
                            {pieStatusData.map((entry, index) => (
                                    <Cell
                                        key={`cell-${index}`}
                                        fill={STATUS_COLORS[entry.name] || COLORS[index % COLORS.length]}
                                    />
                            ))}
                        </Pie>
                            <Tooltip
                                contentStyle={{
                                    backgroundColor: '#fff',
                                    border: '1px solid #e2e8f0',
                                    borderRadius: '8px',
                                    boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
                                }}
                            />
                            <Legend
                                wrapperStyle={{ paddingTop: '20px' }}
                                iconType="circle"
                            />
                    </PieChart>
                </ResponsiveContainer>
            </div>

                <div className="chart-card chart-full-width">
                <h3>Citas por mes</h3>
                    <ResponsiveContainer width="100%" height={280}>
                        <BarChart data={barMonthData} margin={{ top: 20, right: 20, left: 0, bottom: 10 }}>
                            <XAxis dataKey="name" tick={{ fill: '#64748b', fontSize: 12 }} />
                            <YAxis tick={{ fill: '#64748b', fontSize: 12 }} />
                            <Tooltip
                                contentStyle={{
                                    backgroundColor: '#fff',
                                    border: '1px solid #e2e8f0',
                                    borderRadius: '8px',
                                    boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
                                }}
                            />
                            <Bar dataKey="value" fill="url(#colorGradient3)" radius={[8, 8, 0, 0]} />
                            <defs>
                                <linearGradient id="colorGradient3" x1="0" y1="0" x2="0" y2="1">
                                    <stop offset="0%" stopColor="#f093fb" />
                                    <stop offset="100%" stopColor="#f5576c" />
                                </linearGradient>
                            </defs>
                    </BarChart>
                </ResponsiveContainer>
                </div>
            </div>
        </div>
    )
}
