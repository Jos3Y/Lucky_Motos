@echo off
echo ====================================
echo   Diagnostico de Error de Compilacion
echo ====================================
echo.

echo [1/5] Verificando Java...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java no encontrado
    pause
    exit /b 1
)
echo.

echo [2/5] Verificando Maven...
mvn -version
if %errorlevel% neq 0 (
    echo ERROR: Maven no encontrado
    pause
    exit /b 1
)
echo.

echo [3/5] Verificando estructura del proyecto...
if not exist "pom.xml" (
    echo ERROR: pom.xml no encontrado
    pause
    exit /b 1
)
if not exist "src\main\java" (
    echo ERROR: Directorio src\main\java no encontrado
    pause
    exit /b 1
)
echo Estructura OK
echo.

echo [4/5] Limpiando proyecto...
call mvn clean
echo.

echo [5/5] Compilando con detalles de error...
echo.
echo ====================================
echo   ERRORES DE COMPILACION:
echo ====================================
echo.
mvn compile -DskipTests -e -X 2>&1 | findstr /C:"ERROR" /C:"error" /C:"cannot find symbol" /C:"package" /C:"does not exist" /C:"cannot resolve"
echo.
echo ====================================
echo   Si no aparecen errores arriba,
echo   ejecuta: mvn compile -DskipTests -e
echo   y copia TODO el output
echo ====================================
pause

