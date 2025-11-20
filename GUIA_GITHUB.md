# 🚀 Guía para Publicar en GitHub

## Paso 1: Crear una Cuenta en GitHub (si no tienes)

1. Ve a https://github.com
2. Haz clic en "Sign up"
3. Completa el formulario y crea tu cuenta (es **GRATIS**)

## Paso 2: Crear un Nuevo Repositorio

1. Inicia sesión en GitHub
2. Haz clic en el botón **"+"** en la esquina superior derecha
3. Selecciona **"New repository"**
4. Completa:
   - **Repository name:** `sistemamotoservice` (o el nombre que prefieras)
   - **Description:** "Sistema de gestión de servicios para motos"
   - **Visibility:** Público (para que todos puedan verlo) o Privado
   - **NO marques** "Initialize with README" (ya tenemos uno)
5. Haz clic en **"Create repository"**

## Paso 3: Preparar el Proyecto Local

### 3.1. Verificar que Git esté instalado

Abre PowerShell o CMD y ejecuta:
```bash
git --version
```

Si no está instalado, descárgalo de: https://git-scm.com/download/win

### 3.2. Inicializar Git en tu proyecto

```bash
cd C:\xampp\htdocs\sistemamotoservice
git init
```

### 3.3. Agregar todos los archivos

```bash
git add .
```

### 3.4. Hacer el primer commit

```bash
git commit -m "Initial commit: Sistema Lucky Motos"
```

## Paso 4: Conectar con GitHub

### 4.1. Agregar el repositorio remoto

Reemplaza `TU_USUARIO` con tu nombre de usuario de GitHub:

```bash
git remote add origin https://github.com/TU_USUARIO/sistemamotoservice.git
```

### 4.2. Verificar la conexión

```bash
git remote -v
```

## Paso 5: Subir el Código

### 5.1. Subir a GitHub

```bash
git branch -M main
git push -u origin main
```

Te pedirá tu usuario y contraseña de GitHub. Si tienes autenticación de dos factores, necesitarás un **Personal Access Token**.

### 5.2. Crear un Personal Access Token (si es necesario)

1. Ve a GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Haz clic en "Generate new token"
3. Dale un nombre (ej: "sistemamotos")
4. Selecciona los permisos: `repo` (todos)
5. Haz clic en "Generate token"
6. **Copia el token** (solo se muestra una vez)
7. Úsalo como contraseña cuando Git te lo pida

## Paso 6: Verificar en GitHub

1. Ve a tu repositorio en GitHub: `https://github.com/TU_USUARIO/sistemamotoservice`
2. Deberías ver todos tus archivos

## 📝 Sobre la Base de Datos

✅ **SÍ, puedes incluir el script SQL** (`lucky_motos (4).sql`) en GitHub. Es seguro porque:
- Solo contiene la estructura y datos de ejemplo
- No contiene credenciales
- Otros desarrolladores pueden usarlo para configurar su propia base de datos

❌ **NO subas:**
- Archivos de configuración con contraseñas reales (`application.properties` con credenciales)
- Archivos compilados (`target/`, `node_modules/`)
- Archivos de comprobantes personales

## 🔄 Actualizar el Repositorio (cuando hagas cambios)

```bash
git add .
git commit -m "Descripción de los cambios"
git push
```

## 📋 Comandos Útiles

```bash
# Ver el estado de los archivos
git status

# Ver los cambios
git diff

# Ver el historial
git log

# Crear una nueva rama
git checkout -b nombre-rama

# Cambiar de rama
git checkout main
```

## 🌐 Compartir el Enlace

Una vez publicado, puedes compartir el enlace:
```
https://github.com/TU_USUARIO/sistemamotoservice
```

## ⚠️ Notas Importantes

1. **Nunca subas contraseñas o información sensible**
2. El archivo `.gitignore` ya está configurado para excluir archivos sensibles
3. Si accidentalmente subiste algo sensible, cámbialo inmediatamente
4. Puedes hacer el repositorio privado si no quieres que sea público

## 🆘 Problemas Comunes

### Error: "remote origin already exists"
```bash
git remote remove origin
git remote add origin https://github.com/TU_USUARIO/sistemamotoservice.git
```

### Error: "failed to push"
```bash
git pull origin main --allow-unrelated-histories
git push -u origin main
```

---

¡Listo! Tu proyecto ya está en GitHub y puedes compartirlo con el mundo 🌍

