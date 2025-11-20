@echo off
chcp 65001 >nul
echo ====================================
echo   SUBIR PROYECTO A GITHUB
echo ====================================
echo.

REM Verificar Git
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Git no está instalado
    echo.
    echo Descarga Git de: https://git-scm.com/download/win
    pause
    exit /b 1
)

echo ✅ Git encontrado
echo.

REM Configurar Git (si es primera vez)
echo ¿Es la primera vez que usas Git en esta PC? (S/N)
set /p primera_vez="> "
if /i "%primera_vez%"=="S" (
    echo.
    set /p git_nombre="Ingresa tu nombre: "
    set /p git_email="Ingresa tu email: "
    git config --global user.name "%git_nombre%"
    git config --global user.email "%git_email%"
    echo ✅ Git configurado
    echo.
)

REM Inicializar repositorio
if not exist ".git" (
    echo 📦 Inicializando repositorio Git...
    git init
    echo ✅ Repositorio inicializado
    echo.
)

REM Agregar archivos
echo 📝 Agregando archivos...
git add .
echo ✅ Archivos agregados
echo.

REM Mostrar estado
echo 📊 Estado actual:
git status --short
echo.

REM Commit
echo 💾 ¿Deseas hacer commit? (S/N)
set /p hacer_commit="> "
if /i not "%hacer_commit%"=="S" (
    echo ❌ Commit cancelado
    pause
    exit /b 0
)

set /p mensaje_commit="Mensaje del commit (o Enter para 'Actualización'): "
if "%mensaje_commit%"=="" set mensaje_commit=Actualización del proyecto

git commit -m "%mensaje_commit%"
if %errorlevel% neq 0 (
    echo ❌ Error al hacer commit
    pause
    exit /b 1
)

echo ✅ Commit realizado
echo.

REM Conectar con GitHub
echo 🔗 Configuración de GitHub
echo.
echo ¿Ya tienes un repositorio creado en GitHub? (S/N)
set /p tiene_repo="> "

if /i "%tiene_repo%"=="S" (
    echo.
    echo Ingresa la URL de tu repositorio:
    echo Ejemplo: https://github.com/TU_USUARIO/sistemamotoservice.git
    set /p repo_url="URL: "
    
    if not "%repo_url%"=="" (
        REM Remover origin si existe
        git remote remove origin 2>nul
        
        REM Agregar origin
        git remote add origin "%repo_url%"
        echo ✅ Repositorio remoto configurado
        echo.
        
        REM Subir
        echo 🚀 Subiendo a GitHub...
        git branch -M main 2>nul
        git push -u origin main
        
        if %errorlevel% equ 0 (
            echo.
            echo ====================================
            echo   ✅ ¡PROYECTO SUBIDO EXITOSAMENTE!
            echo ====================================
            echo.
            echo Tu proyecto está en: %repo_url%
            echo.
        ) else (
            echo.
            echo ❌ Error al subir. Posibles causas:
            echo 1. URL incorrecta
            echo 2. No tienes permisos
            echo 3. Necesitas autenticarte
            echo.
            echo Si tienes 2FA, necesitas un Personal Access Token
            echo Crea uno en: https://github.com/settings/tokens
            echo.
        )
    )
) else (
    echo.
    echo 📋 Pasos para crear el repositorio:
    echo.
    echo 1. Ve a https://github.com/new
    echo 2. Nombre: sistemamotoservice
    echo 3. Marca "Public"
    echo 4. NO marques "Initialize with README"
    echo 5. Haz clic en "Create repository"
    echo.
    echo Luego ejecuta este script de nuevo y selecciona "S"
    echo.
)

echo.
pause

