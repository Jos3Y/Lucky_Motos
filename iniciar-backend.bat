@echo off
echo ====================================
echo   Iniciando Backend (Spring Boot)
echo ====================================
echo.
echo Puerto: 8081
echo.
cd /d "%~dp0"
echo Compilando proyecto...
call mvn clean compile -DskipTests
if %errorlevel% neq 0 (
    echo Error al compilar el proyecto
    pause
    exit /b %errorlevel%
)
echo.
echo Iniciando aplicacion Spring Boot...
echo El backend estara disponible en: http://localhost:8081
echo.
echo IMPORTANTE: El frontend debe estar en: http://localhost:3002
echo.
call mvn spring-boot:run
pause

