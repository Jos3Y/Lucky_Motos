import { useState } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import './Layout.css'

const Layout = ({ children }) => {
  const { user, logout, hasRole, hasAnyRole } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [sidebarOpen, setSidebarOpen] = useState(true)

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const menuItems = [
    {
      path: '/dashboard',
      label: 'Dashboard',
      icon: '⏰',
      roles: ['ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE']
    },
    {
      path: '/citas',
      label: 'Citas',
      icon: '📅',
      roles: ['ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE']
    },
    {
      path: '/repuestos',
      label: 'Repuestos',
      icon: '📦',
      roles: ['ADMIN', 'RECEPCIONISTA', 'TECNICO']
    },
    {
      path: '/tecnicos',
      label: 'Técnicos',
      icon: '👷',
      roles: ['ADMIN', 'RECEPCIONISTA', 'TECNICO']
    },
    {
      path: '/especialidades',
      label: 'Especialidades',
      icon: '🛠️',
      roles: ['ADMIN', 'RECEPCIONISTA']
    },
    {
      path: '/usuarios',
      label: 'Usuarios',
      icon: '👤',
      roles: ['ADMIN']
    },
    {
      path: '/reportes',
      label: 'Reportes',
      icon: '📊',
      roles: ['ADMIN', 'RECEPCIONISTA']
    }
  ]

  const filteredMenuItems = menuItems.filter(item => 
    hasAnyRole(item.roles)
  )

  const getInitials = (name) => {
    if (!name) return 'U'
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)
  }

  return (
    <div className="layout-container">
      {/* Sidebar */}
      <aside className={`sidebar ${sidebarOpen ? 'open' : 'closed'}`}>
        <div className="sidebar-header">
          <h2>Lucky Motos</h2>
          <button 
            className="sidebar-toggle"
            onClick={() => setSidebarOpen(!sidebarOpen)}
          >
            {sidebarOpen ? '◀' : '▶'}
          </button>
        </div>
        
        <nav className="sidebar-nav">
          {filteredMenuItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`nav-item ${location.pathname === item.path ? 'active' : ''}`}
            >
              <span className="nav-icon">{item.icon}</span>
              {sidebarOpen && <span className="nav-label">{item.label}</span>}
            </Link>
          ))}
        </nav>

        <div className="sidebar-footer">
          <Link to="/ajustes" className="nav-item">
            <span className="nav-icon">⚙️</span>
            {sidebarOpen && <span className="nav-label">Ajustes</span>}
          </Link>
          <button onClick={handleLogout} className="nav-item logout-btn">
            <span className="nav-icon">🚪</span>
            {sidebarOpen && <span className="nav-label">Cerrar sesión</span>}
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <div className="main-content">
        {/* Header */}
        <header className="main-header">
          <div className="header-user">
            <div className="user-avatar">
              {getInitials(user?.correo || 'Usuario')}
            </div>
            <div className="user-info">
              <span className="user-name">{user?.correo || 'Usuario'}</span>
              <span className="user-role">
                {user?.roles?.join(', ') || 'Sin rol'}
              </span>
            </div>
          </div>
        </header>

        {/* Page Content */}
        <main className="page-content">
          {children}
        </main>
      </div>
    </div>
  )
}

export default Layout

