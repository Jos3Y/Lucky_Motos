# 🚀 Despliegue Rápido - Proyecto Online en 15 Minutos

## 📋 Resumen
- **GitHub**: Código fuente (gratis)
- **Render.com**: Backend + Base de datos (gratis)
- **Vercel**: Frontend (gratis)
- **Resultado**: Tu proyecto accesible desde cualquier navegador 🌐

---

## PARTE 1: Subir a GitHub (5 minutos)

### Paso 1: Crear cuenta en GitHub
1. Ve a https://github.com/signup
2. Crea tu cuenta (es gratis)

### Paso 2: Crear repositorio
1. Inicia sesión en GitHub
2. Haz clic en **"+"** → **"New repository"**
3. Nombre: `sistemamotoservice`
4. Marca **"Public"** (para que sea visible)
5. **NO marques** "Initialize with README"
6. Haz clic en **"Create repository"**

### Paso 3: Subir código desde tu PC

Abre PowerShell en la carpeta del proyecto y ejecuta:

```powershell
# Si es la primera vez, configura Git
git config --global user.name "Tu Nombre"
git config --global user.email "tu@email.com"

# Inicializa Git
git init

# Agrega todos los archivos
git add .

# Haz el primer commit
git commit -m "Initial commit: Sistema Lucky Motos"

# Conecta con GitHub (reemplaza TU_USUARIO)
git remote add origin https://github.com/TU_USUARIO/sistemamotoservice.git

# Sube el código
git branch -M main
git push -u origin main
```

**Nota**: Te pedirá usuario y contraseña. Si tienes 2FA, usa un **Personal Access Token** como contraseña.

### ✅ Listo Parte 1
Tu código ya está en: `https://github.com/TU_USUARIO/sistemamotoservice`

---

## PARTE 2: Desplegar Backend en Render.com (10 minutos)

### Paso 1: Crear cuenta en Render
1. Ve a https://render.com
2. Haz clic en **"Get Started for Free"**
3. Conecta con tu cuenta de GitHub

### Paso 2: Crear Base de Datos MySQL
1. En Render, haz clic en **"New +"** → **"PostgreSQL"** (o MySQL si está disponible)
2. Nombre: `lucky-motos-db`
3. Plan: **Free**
4. Haz clic en **"Create Database"**
5. **Copia la "Internal Database URL"** (la necesitarás después)

### Paso 3: Desplegar Backend
1. En Render, haz clic en **"New +"** → **"Web Service"**
2. Conecta tu repositorio de GitHub
3. Selecciona `sistemamotoservice`
4. Configuración:
   - **Name**: `sistemamotos-backend`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean install -DskipTests`
   - **Start Command**: `java -jar target/sistemamotoservice-0.0.1-SNAPSHOT.war`
   - **Plan**: **Free**

5. **Variables de Entorno** (añade estas):
   ```
   JAVA_HOME=/usr/lib/jvm/java-17-openjdk
   SPRING_PROFILES_ACTIVE=prod
   SPRING_DATASOURCE_URL=jdbc:mysql://TU_HOST:3306/TU_DB?useSSL=true
   SPRING_DATASOURCE_USERNAME=TU_USUARIO
   SPRING_DATASOURCE_PASSWORD=TU_CONTRASEÑA
   JAVA_OPTS=-Xmx512m -Xms256m
   ```

6. Haz clic en **"Create Web Service"**

### ✅ Backend desplegado
URL: `https://sistemamotos-backend.onrender.com`

---

## PARTE 3: Desplegar Frontend en Vercel (5 minutos)

### Paso 1: Crear cuenta en Vercel
1. Ve a https://vercel.com
2. Haz clic en **"Sign Up"**
3. Conecta con GitHub

### Paso 2: Desplegar Frontend
1. Haz clic en **"Add New..."** → **"Project"**
2. Importa tu repositorio `sistemamotoservice`
3. Configuración:
   - **Framework Preset**: Vite
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`

4. **Variables de Entorno**:
   ```
   VITE_API_URL=https://sistemamotos-backend.onrender.com/api
   ```

5. Haz clic en **"Deploy"**

### ✅ Frontend desplegado
URL: `https://tu-proyecto.vercel.app`

---

## 🔗 Acceso Final

- **Frontend (Demo)**: `https://tu-proyecto.vercel.app`
- **Backend API**: `https://sistemamotos-backend.onrender.com/api`
- **GitHub**: `https://github.com/TU_USUARIO/sistemamotoservice`

---

## ⚠️ Notas Importantes

1. **Render Free**: El servicio puede "dormir" después de 15 min de inactividad. La primera petición puede tardar ~30 segundos en despertar.

2. **Base de Datos**: Si Render no tiene MySQL gratis, usa:
   - **PlanetScale** (MySQL gratis): https://planetscale.com
   - **Railway** (MySQL gratis): https://railway.app

3. **Actualizar Código**: Cada vez que hagas `git push`, Render y Vercel se actualizan automáticamente.

---

## 🆘 Problemas Comunes

### Backend no inicia
- Verifica las variables de entorno
- Revisa los logs en Render
- Asegúrate de que la base de datos esté accesible

### Frontend no conecta con Backend
- Verifica `VITE_API_URL` en Vercel
- Asegúrate de que el backend esté despierto
- Revisa CORS en el backend

---

¡Listo! Tu proyecto está online y accesible desde cualquier navegador 🎉

