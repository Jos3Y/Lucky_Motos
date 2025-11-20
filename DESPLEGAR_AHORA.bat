@echo off
chcp 65001 >nul
echo ====================================
echo   DESPLEGAR PROYECTO DESDE GITHUB
echo ====================================
echo.

echo Tu repositorio: https://github.com/Jos3Y/Lucky_Motos
echo.

echo ====================================
echo   OPCIÓN 1: DESDE NAVEGADOR (FÁCIL)
echo ====================================
echo.
echo 1. BACKEND (Render.com):
echo    - Ve a: https://render.com
echo    - Inicia sesión con GitHub
echo    - "New +" ^> "Web Service"
echo    - Conecta: Jos3Y/Lucky_Motos
echo    - Render detecta automáticamente
echo.
echo 2. FRONTEND (Vercel.com):
echo    - Ve a: https://vercel.com
echo    - Inicia sesión con GitHub
echo    - "Add New..." ^> "Project"
echo    - Importa: Jos3Y/Lucky_Motos
echo    - Root Directory: frontend
echo.
echo ====================================
echo   OPCIÓN 2: DESDE COMANDOS (CLI)
echo ====================================
echo.

echo ¿Tienes Node.js instalado? (S/N)
set /p tiene_node="> "

if /i "%tiene_node%"=="S" (
    echo.
    echo Instalando Vercel CLI...
    call npm install -g vercel
    echo.
    echo ¿Deseas desplegar el frontend ahora? (S/N)
    set /p desplegar="> "
    if /i "%desplegar%"=="S" (
        cd frontend
        echo.
        echo Iniciando despliegue...
        call vercel --prod
        cd ..
    )
) else (
    echo.
    echo Para usar comandos, instala Node.js primero:
    echo https://nodejs.org
    echo.
)

echo.
echo ====================================
echo   RECOMENDACIÓN
echo ====================================
echo.
echo Usa la OPCIÓN 1 (navegador) - Es más fácil
echo Una vez conectado, cada "git push" despliega automáticamente
echo.
echo Lee COMANDOS_DESPLIEGUE.md para más detalles
echo.
pause

