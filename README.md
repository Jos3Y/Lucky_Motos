# 🏍️ Sistema Lucky Motos - Sistema de Gestión de Servicios para Motos

Sistema web completo para la gestión de servicios de motocicletas, desarrollado con **Spring Boot** (Backend) y **React + Vite** (Frontend).

## 📋 Características

- ✅ Gestión de citas y servicios
- ✅ Gestión de técnicos y especialidades
- ✅ Gestión de repuestos
- ✅ Gestión de clientes/socios
- ✅ Sistema de autenticación con JWT
- ✅ Dashboard con reportes y estadísticas
- ✅ Subida de comprobantes
- ✅ Sistema de roles y permisos

## 🛠️ Tecnologías

### Backend
- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **MySQL**
- **Maven**

### Frontend
- **React 18**
- **Vite**
- **Axios**
- **React Router**
- **Recharts** (Gráficos)
- **SweetAlert2**

## 📦 Requisitos Previos

- **Java 17** o superior
- **Maven 3.6+**
- **Node.js 16+** y **npm**
- **MySQL 5.7+** o **MySQL 8.0+**
- **XAMPP** (opcional, para MySQL)

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/TU_USUARIO/sistemamotoservice.git
cd sistemamotoservice
```

### 2. Configurar la Base de Datos

1. Inicia MySQL (XAMPP o servicio de MySQL)
2. Crea la base de datos:
```sql
CREATE DATABASE lucky_motos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Importa el script SQL:
```bash
mysql -u root -p lucky_motos < lucky_motos\ \(4\).sql
```

O desde MySQL Workbench/phpMyAdmin, importa el archivo `lucky_motos (4).sql`

### 3. Configurar el Backend

1. Copia el archivo de configuración de ejemplo:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

2. Edita `src/main/resources/application.properties` y configura:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lucky_motos?useSSL=false&serverTimezone=UTC
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

### 4. Instalar Dependencias del Frontend

```bash
cd frontend
npm install
cd ..
```

## ▶️ Ejecutar el Proyecto

### Opción 1: Scripts Automáticos (Windows)

1. **Iniciar Backend:**
```bash
.\iniciar-backend.bat
```

2. **Iniciar Frontend** (en otra terminal):
```bash
.\iniciar-frontend.bat
```

### Opción 2: Manual

#### Backend:
```bash
mvn clean compile
mvn spring-boot:run
```

#### Frontend:
```bash
cd frontend
npm run dev
```

## 🌐 Acceso a la Aplicación

- **Frontend:** http://localhost:3002
- **Backend API:** http://localhost:8081
- **API Base:** http://localhost:8081/api

## 📁 Estructura del Proyecto

```
sistemamotoservice/
├── frontend/              # Aplicación React
│   ├── src/
│   │   ├── components/   # Componentes reutilizables
│   │   ├── pages/        # Páginas de la aplicación
│   │   ├── services/    # Servicios API
│   │   └── context/     # Context API
│   └── package.json
├── src/
│   └── main/
│       ├── java/         # Código fuente Java
│       └── resources/    # Configuración y recursos
├── lucky_motos (4).sql   # Script de base de datos
├── pom.xml               # Configuración Maven
└── README.md
```

## 🔐 Credenciales por Defecto

⚠️ **IMPORTANTE:** Cambia las credenciales por defecto en producción.

## 📝 Scripts Disponibles

- `iniciar-backend.bat` - Inicia el servidor Spring Boot
- `iniciar-frontend.bat` - Inicia el servidor de desarrollo Vite
- `verificar-servicios.bat` - Verifica el estado de los servicios
- `reiniciar.bat` - Reinicia todos los servicios
- `detener.bat` - Detiene todos los servicios

## 🐛 Solución de Problemas

### Error de Compilación
Si encuentras errores de compilación relacionados con Java:
```bash
# Verifica la versión de Java
java -version

# Configura JAVA_HOME si es necesario
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
```

### Error de Conexión a MySQL
- Verifica que MySQL esté corriendo
- Verifica las credenciales en `application.properties`
- Verifica que la base de datos `lucky_motos` exista

### Puerto en Uso
Si los puertos 8081 o 3002 están en uso:
- Backend: Cambia `server.port` en `application.properties`
- Frontend: Cambia el puerto en `vite.config.js`

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 👥 Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 🌐 Desplegar Online (Producción)

Para publicar el sistema online y acceder desde cualquier navegador:

### Opción Rápida (15 minutos)
Lee el archivo **[DESPLIEGUE_RAPIDO.md](DESPLIEGUE_RAPIDO.md)** para instrucciones paso a paso.

### Guía Completa
Lee el archivo **[GUIA_DESPLIEGUE_ONLINE.md](GUIA_DESPLIEGUE_ONLINE.md)** para una guía detallada.

### Servicios Recomendados (Gratis)
- **Render.com** - Backend + Base de datos
- **Vercel** o **Netlify** - Frontend
- **PlanetScale** - Base de datos MySQL
- **100% Gratis** para proyectos académicos y demos

### URLs de Ejemplo (después del despliegue)
- Frontend: `https://tu-proyecto.vercel.app`
- Backend: `https://sistemamotos-backend.onrender.com`
- API: `https://sistemamotos-backend.onrender.com/api`

## 📧 Contacto

Para preguntas o soporte, abre un issue en el repositorio.

---

⭐ Si este proyecto te fue útil, ¡dale una estrella!

