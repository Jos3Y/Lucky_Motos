# 🚀 Cómo Ejecutar el Proyecto Lucky Motos

## ⚠️ IMPORTANTE: URLs Correctas

- **Frontend (React)**: `http://localhost:3002` ← **AQUÍ DEBES ACCEDER**
- **Backend (Spring Boot)**: `http://localhost:8081` ← Solo para APIs, no acceder directamente

## 📋 Configuración de Puertos

- **Frontend (Vite)**: Puerto `3002`
- **Backend (Spring Boot)**: Puerto `8081`

## 🔧 Pasos para Ejecutar el Proyecto

### 1. Verificar Versiones (Primera vez)

```powershell
# Verificar Java
java -version
# Debe ser Java 17 o superior

# Verificar Maven
mvn -version
# Debe mostrar Maven instalado

# Verificar Node.js
node --version
# Debe ser Node 18 o superior

# Verificar npm
npm --version
```

### 2. Instalar Dependencias del Frontend (Primera vez)

```powershell
cd C:\xampp\htdocs\sistemamotoservice\frontend
npm install
```

### 3. Ejecutar el Backend (Terminal 1)

```powershell
cd C:\xampp\htdocs\sistemamotoservice
mvn clean compile -DskipTests
mvn spring-boot:run
```

**Espera a ver este mensaje:**
```
Tomcat started on port(s): 8081 (http)
```

### 4. Ejecutar el Frontend (Terminal 2 - Nueva ventana)

```powershell
cd C:\xampp\htdocs\sistemamotoservice\frontend
npm run dev
```

**Espera a ver este mensaje:**
```
  VITE v5.x.x  ready in xxx ms

  ➜  Local:   http://localhost:3002/
  ➜  Network: use --host to expose
```

### 5. Acceder a la Aplicación

Abre tu navegador y ve a:

```
http://localhost:3002/login
```

O directamente al dashboard (si ya estás autenticado):

```
http://localhost:3002/dashboard
```

## ❌ ERROR COMÚN

**NO accedas a:**
- ❌ `http://localhost:8081/dashboard` ← Esto es el backend, no funciona así
- ❌ `http://localhost:8081/login` ← Esto no existe

**SÍ accede a:**
- ✅ `http://localhost:3002/dashboard` ← Frontend correcto
- ✅ `http://localhost:3002/login` ← Frontend correcto

## 🔍 Verificar que Todo Está Corriendo

### Backend corriendo:
- Debes ver en la terminal: `Tomcat started on port(s): 8081 (http)`
- Puedes verificar: `http://localhost:8081/api/...` (debe responder)

### Frontend corriendo:
- Debes ver en la terminal: `Local: http://localhost:3002/`
- Puedes verificar: `http://localhost:3002` (debe mostrar la aplicación)

## 🛠️ Solución de Problemas

### El frontend no carga
1. Verifica que el backend esté corriendo en puerto 8081
2. Verifica que el frontend esté corriendo en puerto 3002
3. Revisa la consola del navegador (F12) para ver errores

### Error de conexión con el backend
1. Verifica que el backend esté corriendo: `http://localhost:8081`
2. Verifica que MySQL esté corriendo en XAMPP
3. Revisa los logs del backend en la terminal

### Puerto 3002 ocupado
Si el puerto 3002 está ocupado, puedes cambiarlo en `frontend/vite.config.js`:
```javascript
server: {
  port: 3003, // Cambia a otro puerto
  // ...
}
```

### Puerto 8081 ocupado
Si el puerto 8081 está ocupado, cambia en `src/main/resources/application.properties`:
```properties
server.port=8082
```
Y actualiza el proxy en `frontend/vite.config.js`:
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8082', // Actualiza aquí
    // ...
  }
}
```

## 📝 Resumen Rápido

1. **Terminal 1**: `cd C:\xampp\htdocs\sistemamotoservice` → `mvn spring-boot:run`
2. **Terminal 2**: `cd C:\xampp\htdocs\sistemamotoservice\frontend` → `npm run dev`
3. **Navegador**: `http://localhost:3002/login`

## ✅ Checklist

- [ ] Backend corriendo en puerto 8081
- [ ] Frontend corriendo en puerto 3002
- [ ] MySQL corriendo en XAMPP
- [ ] Accediendo a `http://localhost:3002` (NO a 8081)

