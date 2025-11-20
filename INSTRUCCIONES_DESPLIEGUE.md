# 🚀 INSTRUCCIONES RÁPIDAS PARA DESPLEGAR ONLINE

## ✅ Tu Repositorio en GitHub
**URL:** https://github.com/Jos3Y/Lucky_Motos

---

## 📋 PASOS PARA DESPLEGAR (15 minutos)

### 1️⃣ BASE DE DATOS - PlanetScale (5 min)

1. Ve a https://planetscale.com
2. Crea cuenta con GitHub
3. Crea nueva base de datos:
   - Nombre: `lucky_motos`
   - Plan: **Free**
4. Ve a "Connect" y copia la **Connection String**
5. En "Console", ejecuta el contenido de `lucky_motos (4).sql`

**Ejemplo de Connection String:**
```
mysql://usuario:password@host:3306/lucky_motos
```

---

### 2️⃣ BACKEND - Render.com (5 min)

1. Ve a https://render.com
2. Inicia sesión con GitHub
3. Haz clic en **"New +"** → **"Web Service"**
4. Conecta tu repositorio: `Jos3Y/Lucky_Motos`
5. Configuración:
   ```
   Name: lucky-motos-backend
   Environment: Java
   Branch: main
   Root Directory: (vacío)
   Build Command: mvn clean install -DskipTests
   Start Command: java -jar target/sistemamotoservice-0.0.1-SNAPSHOT.war
   Plan: Free
   ```

6. **Variables de Entorno** (añade estas):
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=jdbc:mysql://TU_HOST:3306/lucky_motos?useSSL=true&serverTimezone=UTC
   DB_USERNAME=TU_USUARIO_DE_PLANETSCALE
   DB_PASSWORD=TU_CONTRASEÑA_DE_PLANETSCALE
   JWT_SECRET=una_clave_secreta_muy_larga_y_segura_12345678901234567890
   PORT=10000
   JAVA_OPTS=-Xmx512m -Xms256m
   CORS_ALLOWED_ORIGINS=https://tu-proyecto.vercel.app
   ```

7. Haz clic en **"Create Web Service"**
8. Espera 5-10 minutos para que compile

**URL del Backend:** `https://lucky-motos-backend.onrender.com`

---

### 3️⃣ FRONTEND - Vercel (5 min)

1. Ve a https://vercel.com
2. Inicia sesión con GitHub
3. Haz clic en **"Add New..."** → **"Project"**
4. Importa: `Jos3Y/Lucky_Motos`
5. Configuración:
   ```
   Framework Preset: Vite
   Root Directory: frontend
   Build Command: npm run build
   Output Directory: dist
   Install Command: npm install
   ```

6. **Variables de Entorno**:
   ```
   VITE_API_URL=https://lucky-motos-backend.onrender.com/api
   VITE_BUILD_OUT_DIR=dist
   ```

7. Haz clic en **"Deploy"**
8. Espera 2-3 minutos

**URL del Frontend:** `https://tu-proyecto.vercel.app` (o el nombre que Vercel te asigne)

---

## 🔗 URLs FINALES

Después del despliegue tendrás:

- **Frontend (Demo):** `https://tu-proyecto.vercel.app`
- **Backend API:** `https://lucky-motos-backend.onrender.com/api`
- **GitHub:** https://github.com/Jos3Y/Lucky_Motos

---

## ⚠️ IMPORTANTE

1. **CORS**: Después de desplegar el frontend, actualiza `CORS_ALLOWED_ORIGINS` en Render con la URL real de Vercel

2. **Primera petición**: Render puede tardar 30-60 segundos en "despertar" si está inactivo

3. **Base de datos**: Asegúrate de importar el script SQL completo en PlanetScale

---

## 🔄 ACTUALIZAR CÓDIGO

Cada vez que hagas cambios:

```bash
git add .
git commit -m "Descripción"
git push
```

Render y Vercel se actualizan automáticamente.

---

## 🆘 PROBLEMAS COMUNES

### Backend no inicia
- Revisa los logs en Render
- Verifica las variables de entorno
- Asegúrate de que la base de datos sea accesible

### Frontend no conecta
- Verifica `VITE_API_URL` en Vercel
- Actualiza `CORS_ALLOWED_ORIGINS` en Render con la URL de Vercel
- Espera 30-60 segundos (Render puede estar "dormido")

---

¡Listo! Tu proyecto estará online y accesible desde cualquier navegador 🌐

