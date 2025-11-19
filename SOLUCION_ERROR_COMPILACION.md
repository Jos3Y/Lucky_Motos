# Solución: Error de Compilación en Maven

## 🔍 Diagnóstico del Error

El error muestra:
```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.8.1:compile
```

Esto indica un **error de compilación** pero no muestra los detalles. Sigue estos pasos:

---

## ✅ Paso 1: Verificar Versión de Java

El proyecto requiere **Java 17 o superior**.

```powershell
java -version
```

**Debe mostrar:**
```
java version "17.x.x" o superior
```

**Si muestra versión menor a 17:**
- Instalar Java 17 JDK desde: https://adoptium.net/
- Configurar JAVA_HOME en variables de entorno

---

## ✅ Paso 2: Verificar Carpeta Correcta

**IMPORTANTE:** El proyecto debe estar en:
```
C:\xampp\htdocs\sistemamotoservice
```

**NO en:**
```
C:\xampp\htdocs\Lucky_Motos  ❌
```

**Solución:**
```powershell
# Verificar ubicación actual
pwd

# Si estás en Lucky_Motos, cambiar a la carpeta correcta
cd C:\xampp\htdocs\sistemamotoservice

# Verificar que existe pom.xml
dir pom.xml
```

---

## ✅ Paso 3: Ver Errores Detallados

Ejecutar con más detalle para ver qué archivos tienen errores:

```powershell
cd C:\xampp\htdocs\sistemamotoservice
mvn clean compile -DskipTests -e -X
```

O más simple:
```powershell
mvn clean compile -DskipTests -e
```

**El flag `-e` muestra el stack trace completo** con los errores específicos.

---

## ✅ Paso 4: Soluciones Comunes

### Solución A: Limpiar y Recompilar

```powershell
cd C:\xampp\htdocs\sistemamotoservice

# Limpiar completamente
mvn clean

# Recompilar
mvn compile -DskipTests
```

### Solución B: Verificar Dependencias

```powershell
# Descargar todas las dependencias
mvn dependency:resolve

# Limpiar y recompilar
mvn clean compile -DskipTests
```

### Solución C: Actualizar Maven

```powershell
# Actualizar proyecto Maven
mvn clean install -U -DskipTests
```

---

## ✅ Paso 5: Verificar Archivos de Configuración

### Verificar pom.xml existe y está completo

```powershell
# Verificar que pom.xml existe
dir pom.xml

# Verificar estructura del proyecto
dir src\main\java
```

### Verificar application.properties

```powershell
# Verificar configuración de base de datos
type src\main\resources\application.properties
```

**Debe tener:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lucky_motos
spring.datasource.username=root
spring.datasource.password=
```

---

## ✅ Paso 6: Verificar Java en VS Code

En VS Code:
1. Abrir archivo `.java` cualquiera
2. Verificar que VS Code detecta Java correctamente
3. Si no detecta, instalar extensión "Extension Pack for Java"

---

## 🔧 Comandos de Diagnóstico Completo

Ejecuta estos comandos en orden y comparte los resultados:

```powershell
# 1. Verificar Java
java -version

# 2. Verificar Maven
mvn -version

# 3. Verificar ubicación
cd C:\xampp\htdocs\sistemamotoservice
pwd

# 4. Verificar estructura
dir pom.xml
dir src\main\java

# 5. Limpiar
mvn clean

# 6. Compilar con detalles de error
mvn compile -DskipTests -e
```

---

## 📋 Errores Comunes y Soluciones

### Error: "package does not exist"
**Causa:** Dependencias no descargadas
**Solución:**
```powershell
mvn dependency:resolve
mvn clean compile -DskipTests
```

### Error: "cannot find symbol"
**Causa:** Clases faltantes o imports incorrectos
**Solución:** Verificar que todos los archivos `.java` estén en la estructura correcta

### Error: "source release 17 requires target release 17"
**Causa:** Java incorrecto o configuración Maven
**Solución:**
```powershell
# Verificar JAVA_HOME
echo $env:JAVA_HOME

# Si está vacío, configurar:
# Variables de Entorno → JAVA_HOME → C:\Program Files\Java\jdk-17
```

### Error: "maven-compiler-plugin"
**Causa:** Plugin Maven desactualizado
**Solución:**
```powershell
mvn clean
mvn compile -DskipTests -U
```

---

## 🚀 Comandos de Ejecución Correctos

Una vez resuelto el error:

```powershell
# Terminal 1 - Backend
cd C:\xampp\htdocs\sistemamotoservice
mvn clean package -DskipTests
mvn spring-boot:run -DskipTests

# Terminal 2 - Frontend (nueva terminal)
cd C:\xampp\htdocs\sistemamotoservice\frontend
npm install
npm run dev
```

---

## 📞 Si el Problema Persiste

Comparte el resultado completo de:
```powershell
mvn clean compile -DskipTests -e
```

Esto mostrará **exactamente qué archivo y qué línea tiene el error**.

