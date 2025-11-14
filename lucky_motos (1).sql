-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 14-11-2025 a las 06:10:31
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `lucky_motos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `arranque`
--

CREATE TABLE `arranque` (
  `estado_instalacion` date DEFAULT NULL,
  `fecha_solicitud` date DEFAULT NULL,
  `id_arranque` bigint(20) NOT NULL,
  `vivienda_id` bigint(20) DEFAULT NULL,
  `estado_arranque` varchar(255) DEFAULT NULL,
  `nro_arranque` varchar(255) DEFAULT NULL,
  `tipo_arranque` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_mantenimiento`
--

CREATE TABLE `categoria_mantenimiento` (
  `costo_base` double DEFAULT NULL,
  `estado` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id_categoria` bigint(20) NOT NULL,
  `descripcion_categoria` varchar(255) DEFAULT NULL,
  `nombre_categoria` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita`
--

CREATE TABLE `cita` (
  `fecha_cita` date NOT NULL,
  `hora_cita` time(6) NOT NULL,
  `monto_pago_inicial` decimal(10,2) DEFAULT NULL,
  `pago_inicial` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id_cita` bigint(20) NOT NULL,
  `moto_id` bigint(20) NOT NULL,
  `socio_id` bigint(20) NOT NULL,
  `tecnico_id` bigint(20) DEFAULT NULL,
  `tipo_servicio_id` bigint(20) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `codigo_cita` varchar(50) DEFAULT NULL,
  `comprobante_pago_url` varchar(500) DEFAULT NULL,
  `motivo_estado` varchar(500) DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `estado` enum('CANCELADA','COMPLETADA','CONFIRMADA','EN_PROCESO','PENDIENTE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita_repuesto`
--

CREATE TABLE `cita_repuesto` (
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `cita_id` bigint(20) NOT NULL,
  `id_cita_repuesto` bigint(20) NOT NULL,
  `repuesto_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `disponibilidad_tecnico`
--

CREATE TABLE `disponibilidad_tecnico` (
  `disponible` bit(1) NOT NULL,
  `hora_fin` time(6) NOT NULL,
  `hora_inicio` time(6) NOT NULL,
  `id_disponibilidad` bigint(20) NOT NULL,
  `tecnico_id` bigint(20) NOT NULL,
  `dia_semana` enum('DOMINGO','JUEVES','LUNES','MARTES','MIERCOLES','SABADO','VIERNES') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `instalacion`
--

CREATE TABLE `instalacion` (
  `costo_estimado` double DEFAULT NULL,
  `fecha_instlacion` date DEFAULT NULL,
  `arranque_id` bigint(20) NOT NULL,
  `id_mantenimiento` bigint(20) NOT NULL,
  `id_socio_registro` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `instalacion_socio`
--

CREATE TABLE `instalacion_socio` (
  `created_at` date DEFAULT NULL,
  `id_instalacion_socio` bigint(20) NOT NULL,
  `instalacion_id` bigint(20) NOT NULL,
  `socios_id` bigint(20) NOT NULL,
  `rol_en_instalacion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mantenimientos`
--

CREATE TABLE `mantenimientos` (
  `arranque_id` bigint(20) NOT NULL,
  `categoria_mantenimiento_id` bigint(20) NOT NULL,
  `fecha_mantenimiento` datetime(6) DEFAULT NULL,
  `id_mantenimiento` bigint(20) NOT NULL,
  `socio_registro_id` bigint(20) NOT NULL,
  `descripcion_mantenimiento` varchar(255) DEFAULT NULL,
  `tipo_mantenimiento` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mantenimiento_socio`
--

CREATE TABLE `mantenimiento_socio` (
  `created_at` datetime(6) DEFAULT NULL,
  `id_mantenimiento_socio` bigint(20) NOT NULL,
  `mantenimiento_id` bigint(20) NOT NULL,
  `socio_id` bigint(20) NOT NULL,
  `rol_en_mantenimiento` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `moto`
--

CREATE TABLE `moto` (
  `created_at` datetime(6) DEFAULT NULL,
  `id_moto` bigint(20) NOT NULL,
  `socio_id` bigint(20) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `estado` varchar(255) NOT NULL,
  `kilometraje_actual` varchar(255) DEFAULT NULL,
  `marca` varchar(255) DEFAULT NULL,
  `modelo` varchar(255) DEFAULT NULL,
  `nro_serie_motor` varchar(255) DEFAULT NULL,
  `numero_chasis` varchar(255) DEFAULT NULL,
  `placa` varchar(255) DEFAULT NULL,
  `tipo_combustible` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `repuesto`
--

CREATE TABLE `repuesto` (
  `precio` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id_repuesto` bigint(20) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `marca` varchar(100) DEFAULT NULL,
  `modelo_compatible` varchar(100) DEFAULT NULL,
  `nombre` varchar(200) NOT NULL,
  `estado` enum('AGOTADO','BAJO_STOCK','DISPONIBLE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reserva`
--

CREATE TABLE `reserva` (
  `fecha_reserva` date DEFAULT NULL,
  `hora_reserva` time(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id_reserva` bigint(20) NOT NULL,
  `moto_id` bigint(20) DEFAULT NULL,
  `registro_por` bigint(20) DEFAULT NULL,
  `socio_id` bigint(20) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `codigo_reserva` varchar(255) DEFAULT NULL,
  `comentario` text DEFAULT NULL,
  `estado_registro` enum('ACTIVO','ELIMINADO') NOT NULL,
  `estado_reserva` enum('CANCELADA','FINALIZADA','PENDIENTE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `created_at` datetime(6) DEFAULT NULL,
  `id_rol` bigint(20) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol_socio`
--

CREATE TABLE `rol_socio` (
  `created_at` datetime(6) DEFAULT NULL,
  `id_rol_socio` bigint(20) NOT NULL,
  `rol_id` bigint(20) DEFAULT NULL,
  `socio_id` bigint(20) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `rol_socio`
--

INSERT INTO `rol_socio` (`created_at`, `id_rol_socio`, `rol_id`, `socio_id`, `updated_at`, `descripcion`, `estado`) VALUES
('2025-11-14 05:59:27.000000', 1, NULL, NULL, '2025-11-14 05:59:27.000000', 'Administrador del sistema', 'ACTIVO'),
('2025-11-14 06:02:55.000000', 2, NULL, 1, '2025-11-14 06:02:55.000000', 'Administrador del sistema', 'ACTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sector`
--

CREATE TABLE `sector` (
  `id_sector` bigint(20) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `nombre_sector` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `socio`
--

CREATE TABLE `socio` (
  `estado` bit(1) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `fecha_registro` date DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id_socio` bigint(20) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `apellidos` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `dni` varchar(255) DEFAULT NULL,
  `estado_civil` varchar(255) DEFAULT NULL,
  `genero` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `socio`
--

INSERT INTO `socio` (`estado`, `fecha_nacimiento`, `fecha_registro`, `created_at`, `id_socio`, `updated_at`, `apellidos`, `contrasena`, `correo`, `dni`, `estado_civil`, `genero`, `nombre`, `telefono`) VALUES
(b'1', NULL, '2025-11-14', '2025-11-14 06:02:49.000000', 1, '2025-11-14 06:02:49.000000', 'Nuevo', '', 'admin2ee@luckymotos.com', '99999999', NULL, NULL, 'Admin', '999999999'),
(b'1', NULL, '2025-11-14', '2025-11-14 06:06:14.000000', 2, '2025-11-14 06:06:14.000000', 'Nuevo', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin2@luckymotos.com', '99999999', NULL, NULL, 'Admin', '999999999');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `socio_vivienda`
--

CREATE TABLE `socio_vivienda` (
  `created_at` date DEFAULT NULL,
  `fecha_asignacion` date DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  `id_socio_vivienda` bigint(20) NOT NULL,
  `socio_id` bigint(20) DEFAULT NULL,
  `vivienda_id` bigint(20) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `observaciones` varchar(255) DEFAULT NULL,
  `rol_en_vivienda` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tecnico`
--

CREATE TABLE `tecnico` (
  `created_at` datetime(6) DEFAULT NULL,
  `id_tecnico` bigint(20) NOT NULL,
  `socio_id` bigint(20) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `estado` enum('DISPONIBLE','INACTIVO','OCUPADO') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_servicio`
--

CREATE TABLE `tipo_servicio` (
  `duracion_estimada_minutos` int(11) DEFAULT NULL,
  `precio_base` decimal(10,2) NOT NULL,
  `id_tipo_servicio` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vivienda`
--

CREATE TABLE `vivienda` (
  `activo` bit(1) NOT NULL,
  `created_at` date DEFAULT NULL,
  `nro_familias` int(11) DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  `id_vivienda` bigint(20) NOT NULL,
  `sector_id` bigint(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `manzana` varchar(255) DEFAULT NULL,
  `numero_lote` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `arranque`
--
ALTER TABLE `arranque`
  ADD PRIMARY KEY (`id_arranque`),
  ADD KEY `FKsei4d5lqlk8rtmopn0bb436y` (`vivienda_id`);

--
-- Indices de la tabla `categoria_mantenimiento`
--
ALTER TABLE `categoria_mantenimiento`
  ADD PRIMARY KEY (`id_categoria`);

--
-- Indices de la tabla `cita`
--
ALTER TABLE `cita`
  ADD PRIMARY KEY (`id_cita`),
  ADD UNIQUE KEY `UK3fvjyjp0arx4ysbb0xipxqtei` (`codigo_cita`),
  ADD KEY `FKk26n4u0ec76o6mxopkp1fqnsc` (`socio_id`),
  ADD KEY `FKsxc4326byjc90ac4ho47543yu` (`moto_id`),
  ADD KEY `FKckf8og82b6f27507jtum0mmr9` (`tecnico_id`),
  ADD KEY `FKck4xju0ehu00wycoy4osou08b` (`tipo_servicio_id`);

--
-- Indices de la tabla `cita_repuesto`
--
ALTER TABLE `cita_repuesto`
  ADD PRIMARY KEY (`id_cita_repuesto`),
  ADD KEY `FKnkptvfupjeghtvbj0fs37ecx8` (`cita_id`),
  ADD KEY `FK4ecaayqu0rwmljtr89tysb3r0` (`repuesto_id`);

--
-- Indices de la tabla `disponibilidad_tecnico`
--
ALTER TABLE `disponibilidad_tecnico`
  ADD PRIMARY KEY (`id_disponibilidad`),
  ADD KEY `FKe6eoc9vviafd7dfiwktl24i4u` (`tecnico_id`);

--
-- Indices de la tabla `instalacion`
--
ALTER TABLE `instalacion`
  ADD PRIMARY KEY (`id_mantenimiento`),
  ADD UNIQUE KEY `UKkfk2ip0v4nc3d9cyrd8f8x6pn` (`arranque_id`),
  ADD KEY `FKhm4y00l0lio0yuhwtvbm0qlrl` (`id_socio_registro`);

--
-- Indices de la tabla `instalacion_socio`
--
ALTER TABLE `instalacion_socio`
  ADD PRIMARY KEY (`id_instalacion_socio`),
  ADD KEY `FKkg36c1hg3hkpoixtiuh68nwh9` (`instalacion_id`),
  ADD KEY `FKbv627fmjihqigx6ryi70yn7ax` (`socios_id`);

--
-- Indices de la tabla `mantenimientos`
--
ALTER TABLE `mantenimientos`
  ADD PRIMARY KEY (`id_mantenimiento`),
  ADD KEY `FK4ntc8f6926m9gq68o1ji7l4k1` (`arranque_id`),
  ADD KEY `FK3n2stgq5mlva36idsua87hs5o` (`categoria_mantenimiento_id`),
  ADD KEY `FKj3oue33x9jgf3w2igjuw8brud` (`socio_registro_id`);

--
-- Indices de la tabla `mantenimiento_socio`
--
ALTER TABLE `mantenimiento_socio`
  ADD PRIMARY KEY (`id_mantenimiento_socio`),
  ADD KEY `FK6gka80q0pvpheubn3r3ii64x5` (`mantenimiento_id`),
  ADD KEY `FKbydj8w0d20qi4cexnrg5jdwir` (`socio_id`);

--
-- Indices de la tabla `moto`
--
ALTER TABLE `moto`
  ADD PRIMARY KEY (`id_moto`),
  ADD KEY `FKejl7agvklori0ygu4jbohcgap` (`socio_id`);

--
-- Indices de la tabla `repuesto`
--
ALTER TABLE `repuesto`
  ADD PRIMARY KEY (`id_repuesto`);

--
-- Indices de la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD PRIMARY KEY (`id_reserva`),
  ADD KEY `FK2vyleyjw1ppit1cqr1adwipe4` (`moto_id`),
  ADD KEY `FKnstevvv5eyhtffgquothgobqd` (`socio_id`),
  ADD KEY `FK8jr9f0gutoq4ark2si2t7ysq7` (`registro_por`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`id_rol`);

--
-- Indices de la tabla `rol_socio`
--
ALTER TABLE `rol_socio`
  ADD PRIMARY KEY (`id_rol_socio`),
  ADD KEY `FKbecmfsp55p6kffhp4jmhpkhr7` (`rol_id`),
  ADD KEY `FKtfhwq2mxhbf14hqfmv9ko0c8c` (`socio_id`);

--
-- Indices de la tabla `sector`
--
ALTER TABLE `sector`
  ADD PRIMARY KEY (`id_sector`);

--
-- Indices de la tabla `socio`
--
ALTER TABLE `socio`
  ADD PRIMARY KEY (`id_socio`);

--
-- Indices de la tabla `socio_vivienda`
--
ALTER TABLE `socio_vivienda`
  ADD PRIMARY KEY (`id_socio_vivienda`),
  ADD KEY `FK597a8ku6g4y0bgg6shu6k9cd6` (`socio_id`),
  ADD KEY `FKmdq20p6i9kgxosbn4k7dd9g7u` (`vivienda_id`);

--
-- Indices de la tabla `tecnico`
--
ALTER TABLE `tecnico`
  ADD PRIMARY KEY (`id_tecnico`),
  ADD UNIQUE KEY `UKpjcreqxql3roif40dfka1nyfk` (`socio_id`);

--
-- Indices de la tabla `tipo_servicio`
--
ALTER TABLE `tipo_servicio`
  ADD PRIMARY KEY (`id_tipo_servicio`);

--
-- Indices de la tabla `vivienda`
--
ALTER TABLE `vivienda`
  ADD PRIMARY KEY (`id_vivienda`),
  ADD KEY `FKg3yerdl6tvvcolgvi9nbgsb2f` (`sector_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `arranque`
--
ALTER TABLE `arranque`
  MODIFY `id_arranque` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `categoria_mantenimiento`
--
ALTER TABLE `categoria_mantenimiento`
  MODIFY `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cita`
--
ALTER TABLE `cita`
  MODIFY `id_cita` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cita_repuesto`
--
ALTER TABLE `cita_repuesto`
  MODIFY `id_cita_repuesto` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `disponibilidad_tecnico`
--
ALTER TABLE `disponibilidad_tecnico`
  MODIFY `id_disponibilidad` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `instalacion`
--
ALTER TABLE `instalacion`
  MODIFY `id_mantenimiento` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `instalacion_socio`
--
ALTER TABLE `instalacion_socio`
  MODIFY `id_instalacion_socio` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `mantenimientos`
--
ALTER TABLE `mantenimientos`
  MODIFY `id_mantenimiento` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `mantenimiento_socio`
--
ALTER TABLE `mantenimiento_socio`
  MODIFY `id_mantenimiento_socio` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `moto`
--
ALTER TABLE `moto`
  MODIFY `id_moto` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `repuesto`
--
ALTER TABLE `repuesto`
  MODIFY `id_repuesto` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `reserva`
--
ALTER TABLE `reserva`
  MODIFY `id_reserva` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id_rol` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `rol_socio`
--
ALTER TABLE `rol_socio`
  MODIFY `id_rol_socio` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `sector`
--
ALTER TABLE `sector`
  MODIFY `id_sector` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `socio`
--
ALTER TABLE `socio`
  MODIFY `id_socio` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `socio_vivienda`
--
ALTER TABLE `socio_vivienda`
  MODIFY `id_socio_vivienda` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tecnico`
--
ALTER TABLE `tecnico`
  MODIFY `id_tecnico` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tipo_servicio`
--
ALTER TABLE `tipo_servicio`
  MODIFY `id_tipo_servicio` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `vivienda`
--
ALTER TABLE `vivienda`
  MODIFY `id_vivienda` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `arranque`
--
ALTER TABLE `arranque`
  ADD CONSTRAINT `FKsei4d5lqlk8rtmopn0bb436y` FOREIGN KEY (`vivienda_id`) REFERENCES `vivienda` (`id_vivienda`);

--
-- Filtros para la tabla `cita`
--
ALTER TABLE `cita`
  ADD CONSTRAINT `FKck4xju0ehu00wycoy4osou08b` FOREIGN KEY (`tipo_servicio_id`) REFERENCES `tipo_servicio` (`id_tipo_servicio`),
  ADD CONSTRAINT `FKckf8og82b6f27507jtum0mmr9` FOREIGN KEY (`tecnico_id`) REFERENCES `tecnico` (`id_tecnico`),
  ADD CONSTRAINT `FKk26n4u0ec76o6mxopkp1fqnsc` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id_socio`),
  ADD CONSTRAINT `FKsxc4326byjc90ac4ho47543yu` FOREIGN KEY (`moto_id`) REFERENCES `moto` (`id_moto`);

--
-- Filtros para la tabla `cita_repuesto`
--
ALTER TABLE `cita_repuesto`
  ADD CONSTRAINT `FK4ecaayqu0rwmljtr89tysb3r0` FOREIGN KEY (`repuesto_id`) REFERENCES `repuesto` (`id_repuesto`),
  ADD CONSTRAINT `FKnkptvfupjeghtvbj0fs37ecx8` FOREIGN KEY (`cita_id`) REFERENCES `cita` (`id_cita`);

--
-- Filtros para la tabla `disponibilidad_tecnico`
--
ALTER TABLE `disponibilidad_tecnico`
  ADD CONSTRAINT `FKe6eoc9vviafd7dfiwktl24i4u` FOREIGN KEY (`tecnico_id`) REFERENCES `tecnico` (`id_tecnico`);

--
-- Filtros para la tabla `instalacion`
--
ALTER TABLE `instalacion`
  ADD CONSTRAINT `FKhm4y00l0lio0yuhwtvbm0qlrl` FOREIGN KEY (`id_socio_registro`) REFERENCES `socio` (`id_socio`),
  ADD CONSTRAINT `FKj80r5861ek2v3y490c9fa5epd` FOREIGN KEY (`arranque_id`) REFERENCES `arranque` (`id_arranque`);

--
-- Filtros para la tabla `instalacion_socio`
--
ALTER TABLE `instalacion_socio`
  ADD CONSTRAINT `FKbv627fmjihqigx6ryi70yn7ax` FOREIGN KEY (`socios_id`) REFERENCES `socio` (`id_socio`),
  ADD CONSTRAINT `FKkg36c1hg3hkpoixtiuh68nwh9` FOREIGN KEY (`instalacion_id`) REFERENCES `instalacion` (`id_mantenimiento`);

--
-- Filtros para la tabla `mantenimientos`
--
ALTER TABLE `mantenimientos`
  ADD CONSTRAINT `FK3n2stgq5mlva36idsua87hs5o` FOREIGN KEY (`categoria_mantenimiento_id`) REFERENCES `categoria_mantenimiento` (`id_categoria`),
  ADD CONSTRAINT `FK4ntc8f6926m9gq68o1ji7l4k1` FOREIGN KEY (`arranque_id`) REFERENCES `arranque` (`id_arranque`),
  ADD CONSTRAINT `FKj3oue33x9jgf3w2igjuw8brud` FOREIGN KEY (`socio_registro_id`) REFERENCES `socio` (`id_socio`);

--
-- Filtros para la tabla `mantenimiento_socio`
--
ALTER TABLE `mantenimiento_socio`
  ADD CONSTRAINT `FK6gka80q0pvpheubn3r3ii64x5` FOREIGN KEY (`mantenimiento_id`) REFERENCES `mantenimientos` (`id_mantenimiento`),
  ADD CONSTRAINT `FKbydj8w0d20qi4cexnrg5jdwir` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id_socio`);

--
-- Filtros para la tabla `moto`
--
ALTER TABLE `moto`
  ADD CONSTRAINT `FKejl7agvklori0ygu4jbohcgap` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id_socio`);

--
-- Filtros para la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD CONSTRAINT `FK2vyleyjw1ppit1cqr1adwipe4` FOREIGN KEY (`moto_id`) REFERENCES `moto` (`id_moto`),
  ADD CONSTRAINT `FK8jr9f0gutoq4ark2si2t7ysq7` FOREIGN KEY (`registro_por`) REFERENCES `socio` (`id_socio`),
  ADD CONSTRAINT `FKnstevvv5eyhtffgquothgobqd` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id_socio`);

--
-- Filtros para la tabla `rol_socio`
--
ALTER TABLE `rol_socio`
  ADD CONSTRAINT `FKbecmfsp55p6kffhp4jmhpkhr7` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id_rol`),
  ADD CONSTRAINT `FKtfhwq2mxhbf14hqfmv9ko0c8c` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id_socio`);

--
-- Filtros para la tabla `socio_vivienda`
--
ALTER TABLE `socio_vivienda`
  ADD CONSTRAINT `FK597a8ku6g4y0bgg6shu6k9cd6` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id_socio`),
  ADD CONSTRAINT `FKmdq20p6i9kgxosbn4k7dd9g7u` FOREIGN KEY (`vivienda_id`) REFERENCES `vivienda` (`id_vivienda`);

--
-- Filtros para la tabla `tecnico`
--
ALTER TABLE `tecnico`
  ADD CONSTRAINT `FKgn1vtholclx7nm11nneeot3yi` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id_socio`);

--
-- Filtros para la tabla `vivienda`
--
ALTER TABLE `vivienda`
  ADD CONSTRAINT `FKg3yerdl6tvvcolgvi9nbgsb2f` FOREIGN KEY (`sector_id`) REFERENCES `sector` (`id_sector`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
