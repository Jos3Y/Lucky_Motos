@echo off
echo ====================================
echo   Subir Proyecto a GitHub
echo ====================================
echo.

REM Verificar si Git está instalado
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Git no está instalado
    echo Descarga Git de: https://git-scm.com/download/win
    pause
    exit /b 1
)

echo [1/5] Verificando estado de Git...
if not exist ".git" (
    echo Inicializando repositorio Git...
    git init
    if %errorlevel% neq 0 (
        echo ERROR: No se pudo inicializar Git
        pause
        exit /b 1
    )
)

echo.
echo [2/5] Agregando archivos...
git add .
if %errorlevel% neq 0 (
    echo ERROR: No se pudieron agregar los archivos
    pause
    exit /b 1
)

echo.
echo [3/5] Verificando cambios...
git status

echo.
echo [4/5] ¿Deseas hacer commit de los cambios? (S/N)
set /p respuesta="> "
if /i "%respuesta%"=="S" (
    set /p mensaje="Ingresa el mensaje del commit (o presiona Enter para usar mensaje por defecto): "
    if "!mensaje!"=="" set mensaje="Actualización del proyecto"
    git commit -m "!mensaje!"
    if %errorlevel% neq 0 (
        echo ERROR: No se pudo hacer commit
        pause
        exit /b 1
    )
    echo Commit realizado exitosamente
) else (
    echo Commit cancelado
    pause
    exit /b 0
)

echo.
echo [5/5] Configuración del repositorio remoto
echo.
echo Si ya tienes un repositorio en GitHub, ingresa la URL:
echo Ejemplo: https://github.com/TU_USUARIO/sistemamotoservice.git
echo.
set /p repo_url="URL del repositorio (o presiona Enter para saltar): "
if not "!repo_url!"=="" (
    git remote remove origin 2>nul
    git remote add origin "!repo_url!"
    echo.
    echo Subiendo cambios a GitHub...
    git branch -M main
    git push -u origin main
    if %errorlevel% equ 0 (
        echo.
        echo ====================================
        echo   ¡Proyecto subido exitosamente!
        echo ====================================
    ) else (
        echo.
        echo ERROR: No se pudo subir a GitHub
        echo Verifica:
        echo 1. Que la URL del repositorio sea correcta
        echo 2. Que tengas permisos para escribir en el repositorio
        echo 3. Que hayas configurado tu usuario de Git:
        echo    git config --global user.name "Tu Nombre"
        echo    git config --global user.email "tu@email.com"
    )
) else (
    echo.
    echo Para conectar con GitHub, ejecuta:
    echo git remote add origin https://github.com/TU_USUARIO/sistemamotoservice.git
    echo git branch -M main
    echo git push -u origin main
)

echo.
pause

