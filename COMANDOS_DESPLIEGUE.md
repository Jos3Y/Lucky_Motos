# 🚀 Comandos para Desplegar desde GitHub

## ✅ Tu Repositorio
**GitHub:** https://github.com/Jos3Y/Lucky_Motos

---

## 📋 OPCIÓN 1: Render.com (Backend) - Desde GitHub

### Paso 1: Instalar Render CLI
```bash
# Instalar Render CLI (requiere Node.js)
npm install -g render-cli

# O descarga desde: https://render.com/docs/cli
```

### Paso 2: Autenticarse
```bash
render login
```

### Paso 3: Crear Servicio desde GitHub
```bash
# Crear servicio web conectado a GitHub
render services:create web \
  --name lucky-motos-backend \
  --repo https://github.com/Jos3Y/Lucky_Motos \
  --branch main \
  --build-command "mvn clean install -DskipTests" \
  --start-command "java -jar target/sistemamotoservice-0.0.1-SNAPSHOT.war" \
  --env SPRING_PROFILES_ACTIVE=prod \
  --env DATABASE_URL=jdbc:mysql://TU_HOST:3306/lucky_motos \
  --env DB_USERNAME=TU_USUARIO \
  --env DB_PASSWORD=TU_CONTRASEÑA \
  --env JWT_SECRET=una_clave_secreta_muy_larga_123456789 \
  --env PORT=10000 \
  --plan free
```

**O desde la Web UI:**
1. Ve a https://render.com
2. "New +" → "Web Service"
3. Conecta tu repositorio de GitHub
4. Render detecta automáticamente y despliega

---

## 📋 OPCIÓN 2: Vercel (Frontend) - Desde GitHub

### Paso 1: Instalar Vercel CLI
```bash
npm install -g vercel
```

### Paso 2: Autenticarse
```bash
vercel login
```

### Paso 3: Desplegar desde GitHub
```bash
# Desde la carpeta del proyecto
cd frontend

# Desplegar
vercel --prod

# O con configuración específica
vercel --prod \
  --env VITE_API_URL=https://lucky-motos-backend.onrender.com/api
```

**O desde la Web UI (MÁS FÁCIL):**
1. Ve a https://vercel.com
2. "Add New..." → "Project"
3. Importa `Jos3Y/Lucky_Motos` desde GitHub
4. Vercel detecta automáticamente y despliega

---

## 📋 OPCIÓN 3: Railway.app (Todo en uno)

### Paso 1: Instalar Railway CLI
```bash
# Windows (PowerShell)
iwr https://railway.app/install.sh | iex

# O descarga desde: https://railway.app/cli
```

### Paso 2: Autenticarse
```bash
railway login
```

### Paso 3: Inicializar Proyecto
```bash
railway init
```

### Paso 4: Conectar con GitHub
```bash
railway link
```

### Paso 5: Desplegar
```bash
railway up
```

---

## 📋 OPCIÓN 4: GitHub Actions (Automatización)

### Crear archivo de workflow

Crea `.github/workflows/deploy.yml`:

```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  deploy-backend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Build with Maven
        run: mvn clean install -DskipTests
      
      - name: Deploy to Render
        run: |
          # Aquí puedes usar Render API o webhook
          curl -X POST https://api.render.com/deploy/srv-xxx?key=${{ secrets.RENDER_API_KEY }}
  
  deploy-frontend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      
      - name: Install dependencies
        run: |
          cd frontend
          npm install
      
      - name: Build
        run: |
          cd frontend
          npm run build
      
      - name: Deploy to Vercel
        uses: amondnet/vercel-action@v20
        with:
          vercel-token: ${{ secrets.VERCEL_TOKEN }}
          vercel-org-id: ${{ secrets.VERCEL_ORG_ID }}
          vercel-project-id: ${{ secrets.VERCEL_PROJECT_ID }}
          working-directory: ./frontend
```

---

## 🎯 MÉTODO MÁS FÁCIL: Web UI (Recomendado)

### Render.com (Backend)
```bash
# 1. Ve a https://render.com en tu navegador
# 2. Inicia sesión con GitHub
# 3. "New +" → "Web Service"
# 4. Selecciona tu repositorio: Jos3Y/Lucky_Motos
# 5. Render detecta automáticamente Java/Spring Boot
# 6. Configura variables de entorno
# 7. "Create Web Service"
# ✅ Se despliega automáticamente desde GitHub
```

### Vercel (Frontend)
```bash
# 1. Ve a https://vercel.com en tu navegador
# 2. Inicia sesión con GitHub
# 3. "Add New..." → "Project"
# 4. Importa: Jos3Y/Lucky_Motos
# 5. Configura:
#    - Root Directory: frontend
#    - Framework: Vite
# 6. "Deploy"
# ✅ Se despliega automáticamente desde GitHub
```

---

## 🔄 Actualización Automática

Una vez conectado a GitHub, cada `git push` despliega automáticamente:

```bash
# Hacer cambios
git add .
git commit -m "Nuevas funcionalidades"
git push origin main

# ✅ Render y Vercel se actualizan automáticamente (2-5 min)
```

---

## 📝 Comandos Útiles

### Ver estado del despliegue
```bash
# Render
render services:list

# Vercel
vercel ls

# Railway
railway status
```

### Ver logs
```bash
# Render
render services:logs lucky-motos-backend

# Vercel
vercel logs

# Railway
railway logs
```

### Variables de entorno
```bash
# Render
render env:set KEY=value --service lucky-motos-backend

# Vercel
vercel env add VITE_API_URL production
```

---

## 🎉 Resultado Final

Después del despliegue tendrás:

- **Frontend:** `https://tu-proyecto.vercel.app`
- **Backend:** `https://lucky-motos-backend.onrender.com`
- **Actualización:** Automática con cada `git push`

---

## ⚡ Comando Rápido (Todo en uno)

```bash
# 1. Subir cambios a GitHub
git add .
git commit -m "Preparar para despliegue"
git push origin main

# 2. Ir a Render.com y conectar repositorio (una vez)
# 3. Ir a Vercel.com y conectar repositorio (una vez)

# ✅ Listo! Cada push despliega automáticamente
```

---

**💡 Recomendación:** Usa la Web UI (navegador) la primera vez, es más fácil. Después todo se actualiza automáticamente con `git push`.

