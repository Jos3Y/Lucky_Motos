@echo off
chcp 65001 >nul
echo ====================================
echo   DESPLEGAR FRONTEND EN VERCEL
echo ====================================
echo.

cd frontend

echo [1/3] Verificando Vercel CLI...
vercel --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Instalando Vercel CLI...
    call npm install -g vercel
)

echo.
echo [2/3] Autenticando con Vercel...
echo Si no tienes cuenta, se abrirá el navegador para crear una
vercel login

echo.
echo [3/3] Desplegando a Vercel...
echo.
echo IMPORTANTE: Cuando te pregunte:
echo - Set up and deploy? Y
echo - Which scope? (selecciona tu cuenta)
echo - Link to existing project? N
echo - Project name? (presiona Enter para usar el nombre por defecto)
echo - Directory? ./
echo - Override settings? N
echo.
pause

vercel --prod

echo.
echo ====================================
echo   DESPLIEGUE COMPLETADO
echo ====================================
echo.
echo Tu proyecto está disponible en la URL que Vercel te mostró
echo.
cd ..
pause

