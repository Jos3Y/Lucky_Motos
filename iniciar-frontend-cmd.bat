@echo off
echo ====================================
echo   Iniciando Frontend (React/Vite)
echo ====================================
echo.
echo Puerto: 3002
echo.
cd /d "%~dp0\frontend"
echo Verificando dependencias...
if not exist "node_modules" (
    echo Instalando dependencias...
    call npm.cmd install
)
echo.
echo Iniciando servidor de desarrollo...
echo El frontend estara disponible en: http://localhost:3002
echo.
echo IMPORTANTE: El backend debe estar corriendo en: http://localhost:8081
echo.
call npm.cmd run dev
pause

