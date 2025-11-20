# 🌐 Guía Completa: Desplegar Proyecto Online

Esta guía te ayudará a publicar tu proyecto en internet para que sea accesible desde cualquier navegador.

---

## 📊 Opciones de Hosting Gratuito

### Backend (Spring Boot)
| Servicio | Gratis | Base de Datos | Notas |
|----------|--------|---------------|-------|
| **Render.com** | ✅ | PostgreSQL/MySQL | Se "duerme" después de 15 min |
| **Railway.app** | ✅ | MySQL/PostgreSQL | Más rápido, mejor para demos |
| **Heroku** | ⚠️ | PostgreSQL | Ya no tiene plan gratis |
| **Fly.io** | ✅ | PostgreSQL | Buena opción alternativa |

### Frontend (React)
| Servicio | Gratis | Notas |
|----------|--------|-------|
| **Vercel** | ✅ | Excelente, muy rápido |
| **Netlify** | ✅ | También muy bueno |
| **GitHub Pages** | ✅ | Solo para sitios estáticos |

### Base de Datos
| Servicio | Tipo | Gratis | Notas |
|----------|------|--------|-------|
| **PlanetScale** | MySQL | ✅ | Excelente para MySQL |
| **Supabase** | PostgreSQL | ✅ | Muy completo |
| **Railway** | MySQL/PostgreSQL | ✅ | Incluido con el hosting |

---

## 🎯 Opción Recomendada: Render + Vercel + PlanetScale

### Ventajas
- ✅ 100% Gratis
- ✅ Fácil de configurar
- ✅ Actualización automática desde GitHub
- ✅ URLs profesionales

---

## PASO A PASO DETALLADO

### 1️⃣ Preparar el Proyecto para Producción

#### A. Crear `application-prod.properties`

Crea el archivo `src/main/resources/application-prod.properties`:

```properties
spring.application.name=sistemalucky
server.port=${PORT:8081}

# Base de datos de producción (se configurará en Render)
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT
jwt.secret=${JWT_SECRET:una_clave_larga_y_segura_de_mas_de_32_bytes_123}
jwt.expiration=3600000

# Timezone
spring.jackson.time-zone=America/Lima
spring.jpa.properties.hibernate.jdbc.time_zone=America/Lima

# CORS - IMPORTANTE para producción
# Se configurará en código Java

# Archivos
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
app.upload.dir=/tmp/comprobantes
```

#### B. Configurar CORS para Producción

Busca el archivo de configuración de seguridad y asegúrate de permitir tu dominio de Vercel.

#### C. Actualizar `vite.config.js` para Producción

```javascript
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3002,
    proxy: {
      '/api': {
        target: process.env.VITE_API_URL || 'http://localhost:8081',
        changeOrigin: true,
      }
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: false
  }
})
```

---

### 2️⃣ Crear Base de Datos en PlanetScale

1. Ve a https://planetscale.com
2. Crea cuenta (gratis con GitHub)
3. Crea una nueva base de datos:
   - Nombre: `lucky_motos`
   - Plan: **Free**
4. Una vez creada:
   - Ve a "Connect"
   - Copia la **"Connection string"**
   - Ejemplo: `mysql://usuario:password@host:3306/lucky_motos`

5. Importa el script SQL:
   - Ve a "Console" en PlanetScale
   - Ejecuta el contenido de `lucky_motos (4).sql`

---

### 3️⃣ Desplegar Backend en Render

1. **Crear cuenta**: https://render.com (con GitHub)

2. **Nuevo Web Service**:
   - Conecta tu repositorio de GitHub
   - Selecciona `sistemamotoservice`

3. **Configuración**:
   ```
   Name: sistemamotos-backend
   Environment: Java
   Region: Oregon (o el más cercano)
   Branch: main
   Root Directory: (dejar vacío)
   Build Command: mvn clean install -DskipTests
   Start Command: java -jar target/sistemamotoservice-0.0.1-SNAPSHOT.war
   ```

4. **Variables de Entorno**:
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=jdbc:mysql://TU_HOST:3306/lucky_motos?useSSL=true
   DB_USERNAME=TU_USUARIO
   DB_PASSWORD=TU_CONTRASEÑA
   JWT_SECRET=una_clave_secreta_muy_larga_y_segura_123456789
   PORT=10000
   JAVA_OPTS=-Xmx512m -Xms256m
   ```

5. **Plan**: Free

6. Haz clic en **"Create Web Service"**

7. Espera a que termine el build (5-10 minutos)

---

### 4️⃣ Desplegar Frontend en Vercel

1. **Crear cuenta**: https://vercel.com (con GitHub)

2. **Nuevo Proyecto**:
   - Importa `sistemamotoservice`
   - Framework: Vite

3. **Configuración**:
   ```
   Root Directory: frontend
   Build Command: npm run build
   Output Directory: dist
   Install Command: npm install
   ```

4. **Variables de Entorno**:
   ```
   VITE_API_URL=https://sistemamotos-backend.onrender.com/api
   ```

5. Haz clic en **"Deploy"**

6. Espera a que termine (2-3 minutos)

---

### 5️⃣ Configurar CORS en el Backend

Necesitas permitir que tu frontend de Vercel se conecte al backend. Busca el archivo de configuración de seguridad y añade:

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "https://tu-proyecto.vercel.app",
            "http://localhost:3002"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## 🔗 URLs Finales

Después del despliegue tendrás:

- **Frontend**: `https://tu-proyecto.vercel.app`
- **Backend**: `https://sistemamotos-backend.onrender.com`
- **API**: `https://sistemamotos-backend.onrender.com/api`

---

## 📝 Actualizar el Proyecto

Cada vez que hagas cambios:

1. **Sube a GitHub**:
   ```bash
   git add .
   git commit -m "Descripción de cambios"
   git push
   ```

2. **Render y Vercel se actualizan automáticamente** (tarda 2-5 minutos)

---

## ⚠️ Limitaciones del Plan Gratuito

### Render
- ⏱️ Se "duerme" después de 15 minutos de inactividad
- 🐌 Primera petición puede tardar 30-60 segundos
- 💾 512 MB RAM
- ✅ Perfecto para demos y proyectos pequeños

### Vercel
- ✅ Muy rápido
- ✅ Sin limitaciones importantes para demos
- ✅ CDN global

### PlanetScale
- 💾 5 GB de almacenamiento
- ✅ Suficiente para demos

---

## 🆘 Solución de Problemas

### Backend no inicia
1. Revisa los logs en Render
2. Verifica las variables de entorno
3. Asegúrate de que la base de datos sea accesible

### Frontend no conecta
1. Verifica `VITE_API_URL` en Vercel
2. Revisa CORS en el backend
3. Verifica que el backend esté despierto

### Base de datos no conecta
1. Verifica la connection string
2. Asegúrate de que el host permita conexiones externas
3. Revisa usuario y contraseña

---

## 🎉 ¡Listo!

Tu proyecto está online y accesible desde cualquier navegador. Puedes compartir el enlace de Vercel para demostraciones.

---

## 📚 Recursos Adicionales

- [Documentación de Render](https://render.com/docs)
- [Documentación de Vercel](https://vercel.com/docs)
- [Documentación de PlanetScale](https://planetscale.com/docs)

