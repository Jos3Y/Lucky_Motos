import { Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import Layout from './components/Layout'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Citas from './pages/Citas'
import Repuestos from './pages/Repuestos'
import Tecnicos from './pages/Tecnicos'
import Reportes from './pages/Reportes'
import Especialidades from './pages/Especialidades'
import Usuarios from './pages/Usuarios'

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
        {/* Rutas protegidas */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Layout>
                <Dashboard />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/citas"
          element={
            <ProtectedRoute>
              <Layout>
                <Citas />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/repuestos"
          element={
            <ProtectedRoute requiredRoles={['ADMIN', 'RECEPCIONISTA', 'TECNICO']}>
              <Layout>
                <Repuestos />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/tecnicos"
          element={
            <ProtectedRoute requiredRoles={['ADMIN', 'RECEPCIONISTA', 'TECNICO']}>
              <Layout>
                <Tecnicos />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/reportes"
          element={
            <ProtectedRoute requiredRoles={['ADMIN', 'RECEPCIONISTA']}>
              <Layout>
                <Reportes />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/especialidades"
          element={
            <ProtectedRoute requiredRoles={['ADMIN', 'RECEPCIONISTA']}>
              <Layout>
                <Especialidades />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/usuarios"
          element={
            <ProtectedRoute requiredRoles={['ADMIN']}>
              <Layout>
                <Usuarios />
              </Layout>
            </ProtectedRoute>
          }
        />
      </Routes>
    </AuthProvider>
  )
}

export default App

