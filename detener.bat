@echo off
echo ====================================
echo   Deteniendo Sistema Lucky Motos
echo ====================================
echo.

echo Deteniendo procesos Java (Backend)...
taskkill /F /IM java.exe /T 2>nul
if %errorlevel% equ 0 (
    echo Backend detenido.
) else (
    echo No se encontraron procesos Java corriendo.
)
echo.

echo Deteniendo procesos Node (Frontend)...
taskkill /F /IM node.exe /T 2>nul
if %errorlevel% equ 0 (
    echo Frontend detenido.
) else (
    echo No se encontraron procesos Node corriendo.
)
echo.

echo Liberando puertos...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3002') do taskkill /F /PID %%a 2>nul
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081') do taskkill /F /PID %%a 2>nul
echo.

echo ====================================
echo   Proyecto Detenido
echo ====================================
echo.
echo Presiona cualquier tecla para cerrar...
pause >nul

