# 🔌 Configuración de Puertos - Lucky Motos

## 📍 URLs Correctas

### ✅ ACCEDER AQUÍ (Frontend):
```
http://localhost:3002/login
http://localhost:3002/dashboard
http://localhost:3002/citas
http://localhost:3002/reportes
http://localhost:3002/especialidades
```

### ❌ NO ACCEDER AQUÍ (Backend):
```
http://localhost:8081/dashboard  ← ❌ NO FUNCIONA
http://localhost:8081/login      ← ❌ NO EXISTE
```

El puerto 8081 es solo para las APIs del backend. El frontend (puerto 3002) es quien consume esas APIs.

## 🚀 Cómo Iniciar el Proyecto

### Opción 1: Scripts Automáticos (Recomendado)

**Terminal 1 - Backend:**
```powershell
.\iniciar-backend.bat
```

**Terminal 2 - Frontend:**
```powershell
.\iniciar-frontend.bat
```

### Opción 2: Comandos Manuales

**Terminal 1 - Backend:**
```powershell
cd C:\xampp\htdocs\sistemamotoservice
mvn clean compile -DskipTests
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```powershell
cd C:\xampp\htdocs\sistemamotoservice\frontend
npm run dev
```

## 🔍 Verificación

### ✅ Backend Corriendo:
- Terminal muestra: `Tomcat started on port(s): 8081 (http)`
- Puedes probar: `http://localhost:8081/api/...` (debe responder JSON)

### ✅ Frontend Corriendo:
- Terminal muestra: `Local: http://localhost:3002/`
- Abre navegador: `http://localhost:3002/login`

## ⚙️ Configuración Actual

| Servicio | Puerto | Archivo de Configuración |
|----------|--------|---------------------------|
| Frontend (Vite) | **3002** | `frontend/vite.config.js` |
| Backend (Spring Boot) | **8081** | `src/main/resources/application.properties` |

## 🐛 Problemas Comunes

### "No puedo acceder a http://localhost:3002"
1. Verifica que el frontend esté corriendo (Terminal 2)
2. Verifica que no haya otro proceso usando el puerto 3002
3. Revisa la consola del navegador (F12)

### "Error de conexión con el backend"
1. Verifica que el backend esté corriendo en puerto 8081
2. Verifica que MySQL esté corriendo en XAMPP
3. Revisa los logs del backend

### "En mi otra PC accedo a 8081/dashboard"
**Esto es incorrecto.** Debes:
1. Iniciar el frontend: `cd frontend` → `npm run dev`
2. Acceder a: `http://localhost:3002/dashboard`

## 📝 Resumen

- **Frontend**: Puerto 3002 ← **AQUÍ ACCEDES**
- **Backend**: Puerto 8081 ← Solo APIs, no acceder directamente
- **MySQL**: Puerto 3306 ← Base de datos

