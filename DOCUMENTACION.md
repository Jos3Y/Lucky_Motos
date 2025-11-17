# Lucky Motos – Guía Rápida de Puesta en Marcha

Este documento resume **qué hace el sistema** y **cómo levantarlo desde cero** en el mismo entorno donde lo estamos desarrollando (Windows + XAMPP + Maven + Node). Síguelo tal cual y tendrás backend, base de datos y frontend ejecutándose.

---

## 1. ¿Qué es este proyecto?

Sistema web integral para el taller **Lucky Motos** que cubre:

- **Autenticación JWT** (admin / recepcionista / técnico).
- **Dashboard + Citas**: creación, edición, pagos simulados, adjuntos de comprobantes, agenda visual.
- **Repuestos**: CRUD completo.
- **Técnicos**: agenda interactiva, horarios por defecto, estados y disponibilidad.
- **Especialidades (tipos de servicio)**: catálogo editable desde la UI.
- **Reportes**: tablas dinámicas y exportación a Excel (POI) para técnicos, días y repuestos.
- **Usuarios (socios)**: módulo exclusivo de admin para crear/editar/eliminar cuentas internas.

Tecnologías principales:

| Capa        | Tecnología                                        |
|-------------|---------------------------------------------------|
| Backend     | Spring Boot 3.5 + Maven, JPA (MySQL) + JWT        |
| Frontend    | React 18 + Vite, SweetAlert2                      |
| Base de datos | MySQL (XAMPP)                                   |
| Build / Otros| Apache POI (reportes), MapStruct, Lombok         |

---

## 2. Requisitos previos

1. **XAMPP** (MySQL activo en `localhost:3306`).
2. **Java 17** (Adoptium recomendado, ya configurado en la máquina).
3. **Maven 3.9+** (en `PATH`).
4. **Node.js 18+** (para el frontend).
5. Variables de entorno con acceso a `mysql`, `mvn` y `npm`.

---

## 3. Preparar base de datos

1. Inicia MySQL desde XAMPP.
2. Crea la base (si no existe) y llena datos de ejemplo:

```powershell
cd C:\xampp\htdocs\sistemamotoservice
mysql -u root -e "CREATE DATABASE IF NOT EXISTS lucky_motos CHARACTER SET utf8mb4"
mysql -u root lucky_motos < lucky_motos\ \(1\).sql
```

> El backend usa `spring.jpa.hibernate.ddl-auto=update`, así que con la primera corrida se ajustan tablas pendientes.

---

## 4. Levantar el backend (Spring Boot)

```powershell
cd C:\xampp\htdocs\sistemamotoservice
# detiene procesos Java viejos (opcional)
Get-Process -Name java -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
mvn spring-boot:run -DskipTests
```

Detalles:
- Puerto por defecto: **8080**.
- Usuario admin de fábrica: `admin@luckymotos.com / admin123`.
- Logs en `logs/app.out.log`.

> Si quieres solo compilar: `mvn clean package -DskipTests` y luego `java -jar target\sistemaagua-0.0.1-SNAPSHOT.war` (también funciona porque es WAR ejecutable).

---

## 5. Levantar el frontend (React/Vite)

```powershell
cd C:\xampp\htdocs\sistemamotoservice\frontend
npm install   # primera vez
npm run dev   # Vite en puerto 3002
```

- La URL por defecto: [http://localhost:3002](http://localhost:3002).
- El proxy de Vite enruta `/api` al backend.

> Para build de producción: `npm run build` y servir el contenido de `frontend/dist` (si se quisiera integrar con Spring estático).

---

## 6. Flujo resumido de uso

1. Ingresa con `admin@luckymotos.com / admin123`.
2. Desde el menú lateral puedes:
   - `Citas`: CRUD completo + agenda + pagos simulados y comprobantes.
   - `Repuestos`: inventario.
   - `Técnicos`: horarios, agenda modal, reset masivo.
   - `Especialidades`: usa la tabla `tipo_servicio` para gestionar servicios.
   - `Usuarios`: crear/editar/eliminar socios internos (solo admin).
   - `Reportes`: consulta de métricas y botón “Descargar Excel” (usa `/api/reportes/resumen` y `/api/reportes/export/excel`).
3. Técnicos tienen vista restringida (solo citas del día y cambios de estado).

---

## 7. Comandos útiles

| Acción                                | Comando                                                                 |
|---------------------------------------|-------------------------------------------------------------------------|
| Limpiar y recompilar backend          | `mvn clean package -DskipTests`                                        |
| Ejecutar backend                      | `mvn spring-boot:run -DskipTests`                                      |
| Reiniciar backend limpio              | `Get-Process java … stop` + `mvn spring-boot:run -DskipTests`          |
| Sembrar datos manualmente             | `mysql -u root lucky_motos < lucky_motos\ \(1\).sql`                   |
| Logs en vivo                          | `Get-Content logs\app.out.log -Wait`                                   |
| Frontend dev                          | `npm run dev` (en `frontend/`)                                          |
| Frontend build                        | `npm run build`                                                         |

---

## 8. Estructura relevante

- `src/main/java/com/motos/jass/sistemalucky/...`
  - `auth`: login, JWT.
  - `cita`: entidades/servicio para citas y adjuntos.
  - `reportes`: agregaciones + exportador Excel.
  - `tiposervicio`: CRUD de especialidades.
  - `socio`: usuarios internos.
- `frontend/src/pages`
  - `Citas.jsx`, `Repuestos.jsx`, `Tecnicos.jsx`, `Especialidades.jsx`, `Usuarios.jsx`, `Reportes.jsx`.

---

## 9. Notas finales / Troubleshooting

- **Error 403**: revisa el token en localStorage y vuelve a iniciar sesión.
- **Agenda sin datos**: asegúrate de haber ejecutado los scripts SQL iniciales.
- **Exportar Excel falla**: confirma que el backend corra y que Apache POI esté en dependencia (`poi-ooxml`).
- **Usuarios**: solo admin ve el menú; si no aparece, revisa roles devueltos en `/auth/login`.
- **Logs**: si el server no levanta, siempre revisa `logs/app.out.log` antes de relanzar.

---

Con esto tienes la guía completa para poner el sistema en marcha “de inicio a fin”, incluyendo los comandos exactos y contexto funcional. Cualquier ajuste o paso adicional que necesites documentar, dímelo y lo agrego. 

