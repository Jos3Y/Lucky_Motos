# 🔄 Cómo Detener y Reiniciar el Proyecto

## 🛑 Detener el Proyecto

### Opción 1: Desde las Terminales (Recomendado)

**En cada terminal donde está corriendo:**

1. **Presiona `Ctrl + C`** en la terminal del backend
2. **Presiona `Ctrl + C`** en la terminal del frontend
3. Espera a que se detengan completamente

### Opción 2: Cerrar las Terminales

Simplemente cierra las ventanas de terminal donde están corriendo los procesos.

### Opción 3: Matar Procesos por Puerto (Si no responde)

**En PowerShell (como Administrador):**

```powershell
# Detener proceso en puerto 3002 (Frontend)
Get-NetTCPConnection -LocalPort 3002 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }

# Detener proceso en puerto 8081 (Backend)
Get-NetTCPConnection -LocalPort 8081 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }
```

**O usando netstat:**

```powershell
# Ver procesos en los puertos
netstat -ano | findstr :3002
netstat -ano | findstr :8081

# Matar proceso (reemplaza PID con el número que aparece)
taskkill /PID <PID> /F
```

## ✅ Verificar que Están Detenidos

```powershell
# Verificar puerto 3002
netstat -ano | findstr :3002
# No debe mostrar nada

# Verificar puerto 8081
netstat -ano | findstr :8081
# No debe mostrar nada
```

## 🚀 Reiniciar el Proyecto

### Paso 1: Iniciar Backend

**Terminal 1:**
```powershell
cd C:\xampp\htdocs\sistemamotoservice
mvn spring-boot:run
```

O usar el script:
```powershell
.\iniciar-backend.bat
```

**Espera a ver:**
```
Tomcat started on port(s): 8081 (http)
```

### Paso 2: Iniciar Frontend

**Terminal 2 (Nueva terminal):**
```powershell
cd C:\xampp\htdocs\sistemamotoservice\frontend
npm run dev
```

O usar el script:
```powershell
.\iniciar-frontend.bat
```

**Espera a ver:**
```
Local:   http://localhost:3002/
```

### Paso 3: Acceder

Abre tu navegador:
```
http://localhost:3002/login
```

## 🔄 Reinicio Rápido (Script)

Puedes crear un script `reiniciar.bat`:

```batch
@echo off
echo Deteniendo procesos...
taskkill /F /IM java.exe /T 2>nul
taskkill /F /IM node.exe /T 2>nul
timeout /t 2 /nobreak >nul
echo.
echo Iniciando backend...
start "Backend" cmd /k "cd /d %~dp0 && mvn spring-boot:run"
timeout /t 5 /nobreak >nul
echo.
echo Iniciando frontend...
start "Frontend" cmd /k "cd /d %~dp0\frontend && npm run dev"
echo.
echo Proyecto reiniciado!
echo Backend: http://localhost:8081
echo Frontend: http://localhost:3002
pause
```

## 📝 Checklist de Reinicio

- [ ] Detener backend (Ctrl+C o cerrar terminal)
- [ ] Detener frontend (Ctrl+C o cerrar terminal)
- [ ] Verificar que los puertos están libres
- [ ] Iniciar backend (Terminal 1)
- [ ] Esperar a que backend esté listo
- [ ] Iniciar frontend (Terminal 2)
- [ ] Esperar a que frontend esté listo
- [ ] Abrir navegador en http://localhost:3002

## ⚠️ Problemas Comunes

### "Puerto ya en uso"
```powershell
# Matar proceso en puerto específico
Get-NetTCPConnection -LocalPort 3002 | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }
Get-NetTCPConnection -LocalPort 8081 | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }
```

### "No se detiene con Ctrl+C"
1. Cierra la terminal
2. O usa el comando para matar el proceso por puerto

### "Error al reiniciar"
1. Espera 5-10 segundos después de detener
2. Verifica que los puertos estén libres
3. Vuelve a iniciar

