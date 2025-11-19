@echo off
echo ====================================
echo   Verificando Servicios del Proyecto
echo ====================================
echo.

echo [1/4] Verificando Backend (Puerto 8081)...
netstat -ano | findstr :8081 >nul
if %errorlevel% equ 0 (
    echo ✅ Backend CORRIENDO en puerto 8081
    netstat -ano | findstr :8081
) else (
    echo ❌ Backend NO está corriendo en puerto 8081
)
echo.

echo [2/4] Verificando Frontend (Puerto 3002)...
netstat -ano | findstr :3002 >nul
if %errorlevel% equ 0 (
    echo ✅ Frontend CORRIENDO en puerto 3002
    netstat -ano | findstr :3002
) else (
    echo ❌ Frontend NO está corriendo en puerto 3002
)
echo.

echo [3/4] Verificando MySQL (Puerto 3306)...
netstat -ano | findstr :3306 >nul
if %errorlevel% equ 0 (
    echo ✅ MySQL CORRIENDO en puerto 3306
) else (
    echo ❌ MySQL NO está corriendo en puerto 3306
)
echo.

echo [4/4] Probando conexión al Backend...
curl -s http://localhost:8081/api/ 2>nul >nul
if %errorlevel% equ 0 (
    echo ✅ Backend responde correctamente
) else (
    echo ⚠️  No se pudo conectar al backend (puede ser normal si requiere autenticación)
)
echo.

echo ====================================
echo   Resumen
echo ====================================
echo.
echo Backend:  http://localhost:8081
echo Frontend: http://localhost:3002
echo MySQL:    localhost:3306
echo.
echo Para probar en el navegador:
echo 1. Abre: http://localhost:3002/login
echo 2. Si ves la página de login, todo está funcionando
echo.
pause

