@echo off
echo ====================================
echo   Reiniciando Sistema Lucky Motos
echo ====================================
echo.

echo [1/4] Deteniendo procesos existentes...
taskkill /F /IM java.exe /T 2>nul
taskkill /F /IM node.exe /T 2>nul
timeout /t 2 /nobreak >nul
echo.

echo [2/4] Verificando puertos...
netstat -ano | findstr :3002 >nul
if %errorlevel% equ 0 (
    echo Puerto 3002 aun en uso, intentando liberar...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3002') do taskkill /F /PID %%a 2>nul
)

netstat -ano | findstr :8081 >nul
if %errorlevel% equ 0 (
    echo Puerto 8081 aun en uso, intentando liberar...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081') do taskkill /F /PID %%a 2>nul
)

timeout /t 3 /nobreak >nul
echo.

echo [3/4] Iniciando Backend (puerto 8081)...
start "Backend - Lucky Motos" cmd /k "cd /d %~dp0 && echo Iniciando Backend... && mvn spring-boot:run"
timeout /t 5 /nobreak >nul
echo.

echo [4/4] Iniciando Frontend (puerto 3002)...
start "Frontend - Lucky Motos" cmd /k "cd /d %~dp0\frontend && echo Iniciando Frontend... && npm run dev"
echo.

echo ====================================
echo   Proyecto Reiniciado!
echo ====================================
echo.
echo Backend:  http://localhost:8081
echo Frontend: http://localhost:3002
echo.
echo Abre tu navegador en: http://localhost:3002/login
echo.
echo Presiona cualquier tecla para cerrar esta ventana...
pause >nul

