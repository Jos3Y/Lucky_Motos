# Carpeta de Imágenes

Esta carpeta contiene todas las imágenes utilizadas en el proyecto.

## Estructura

- **logos/**: Logos y marcas del sistema
- **icons/**: Iconos e imágenes pequeñas
- **photos/**: Fotografías y imágenes grandes

## Uso en HTML/Thymeleaf

Para usar imágenes en tus vistas, usa la siguiente ruta:

```html
<!-- En Thymeleaf -->
<img th:src="@{/images/logos/logo.png}" alt="Logo">

<!-- En HTML estático -->
<img src="/images/logos/logo.png" alt="Logo">
```

## Formatos recomendados

- **Logos**: PNG con fondo transparente
- **Iconos**: SVG o PNG pequeño
- **Fotos**: JPG o PNG según necesidad

