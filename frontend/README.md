# Lucky Motos - Frontend React

Frontend de la aplicación Lucky Motos construido con React y Vite.

## Requisitos

- Node.js 18+ 
- npm o yarn

## Instalación

```bash
cd frontend
npm install
```

## Desarrollo

Para ejecutar el frontend en modo desarrollo:

```bash
npm run dev
```

El frontend estará disponible en `http://localhost:3000`

**Nota:** El backend Spring Boot debe estar corriendo en `http://localhost:8080` para que el proxy funcione correctamente.

## Build para Producción

Para compilar el frontend y copiarlo a `src/main/resources/static`:

```bash
npm run build
```

Esto generará los archivos estáticos que Spring Boot servirá.

## Estructura

```
frontend/
├── src/
│   ├── pages/          # Componentes de páginas
│   │   ├── Login.jsx
│   │   ├── Login.css
│   │   ├── Register.jsx
│   │   └── Register.css
│   ├── App.jsx         # Componente principal
│   ├── main.jsx        # Punto de entrada
│   └── index.css       # Estilos globales
├── package.json
└── vite.config.js
```

## Integración con Spring Boot

- En desarrollo: React corre en puerto 3000 con proxy a Spring Boot (puerto 8080)
- En producción: React se compila y se copia a `src/main/resources/static` para ser servido por Spring Boot

