@echo off
echo ====================================
echo   Iniciando Sistema Lucky Motos
echo ====================================
echo.
echo Compilando proyecto...
call mvn clean compile
if %errorlevel% neq 0 (
    echo Error al compilar el proyecto
    pause
    exit /b %errorlevel%
)
echo.
echo Iniciando aplicacion Spring Boot...
echo La aplicacion estara disponible en: http://localhost:8080/login
echo.
call mvn spring-boot:run
pause

