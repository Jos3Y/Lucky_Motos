-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 17-11-2025 a las 15:35:08
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

--
-- Volcado de datos para la tabla `arranque`
--

INSERT INTO `arranque` (`estado_instalacion`, `fecha_solicitud`, `id_arranque`, `vivienda_id`, `estado_arranque`, `nro_arranque`, `tipo_arranque`) VALUES
(NULL, '2025-10-01', 1, 1, 'PENDIENTE', NULL, 'Nuevo'),
(NULL, '2025-10-05', 2, 2, 'APROBADO', NULL, 'Reconexi??n'),
(NULL, '2025-10-10', 3, 3, 'PENDIENTE', NULL, 'Nuevo'),
(NULL, '2025-10-15', 4, 4, 'RECHAZADO', NULL, 'Nuevo'),
(NULL, '2025-10-20', 5, 5, 'APROBADO', NULL, 'Reconexi??n'),
(NULL, '2025-10-25', 6, 6, 'PENDIENTE', NULL, 'Nuevo'),
(NULL, '2025-11-01', 7, 7, 'PENDIENTE', NULL, 'Nuevo'),
(NULL, '2025-11-05', 8, 8, 'APROBADO', NULL, 'Reconexi??n'),
(NULL, '2025-11-10', 9, 9, 'PENDIENTE', NULL, 'Nuevo'),
(NULL, '2025-11-12', 10, 10, 'PENDIENTE', NULL, 'Nuevo');

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

--
-- Volcado de datos para la tabla `categoria_mantenimiento`
--

INSERT INTO `categoria_mantenimiento` (`costo_base`, `estado`, `created_at`, `id_categoria`, `descripcion_categoria`, `nombre_categoria`) VALUES
(50, b'1', NULL, 1, 'Mantenimiento para prevenir fallas', 'Preventivo'),
(100, b'1', NULL, 2, 'Mantenimiento para corregir fallas existentes', 'Correctivo'),
(150, b'1', NULL, 3, 'Mantenimiento basado en an??lisis de datos', 'Predictivo'),
(250, b'1', NULL, 4, 'Atenci??n de fallas cr??ticas inesperadas', 'Emergencia'),
(200, b'1', NULL, 5, 'Instalaci??n de mejoras en el sistema', 'Mejora');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita`
--

CREATE TABLE `cita` (
  `id_cita` bigint(20) NOT NULL,
  `codigo_cita` varchar(50) DEFAULT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `moto_id` bigint(20) NOT NULL,
  `tecnico_id` bigint(20) DEFAULT NULL,
  `tipo_servicio_id` bigint(20) DEFAULT NULL,
  `fecha_cita` date NOT NULL,
  `hora_cita` time(6) NOT NULL,
  `estado` enum('PENDIENTE','CONFIRMADA','EN_PROCESO','COMPLETADA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
  `observaciones` text DEFAULT NULL,
  `motivo_estado` varchar(500) DEFAULT NULL,
  `pago_inicial` bit(1) DEFAULT b'0',
  `monto_pago_inicial` decimal(10,2) DEFAULT NULL,
  `comprobante_pago_url` varchar(500) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `cita`
--

INSERT INTO `cita` (`id_cita`, `codigo_cita`, `cliente_id`, `moto_id`, `tecnico_id`, `tipo_servicio_id`, `fecha_cita`, `hora_cita`, `estado`, `observaciones`, `motivo_estado`, `pago_inicial`, `monto_pago_inicial`, `comprobante_pago_url`, `created_at`, `updated_at`) VALUES
(1, NULL, 1, 1, 4, 1, '2025-11-21', '02:00:00.000000', 'CONFIRMADA', 'Revisi??n general solicitada por el cliente.', NULL, b'1', 50.00, '/api/comprobantes/view/fdb6ea38-f39a-4ca1-9fc4-b1fc8583a34e.webp', NULL, '2025-11-17 01:11:03.000000'),
(2, NULL, 2, 2, 2, 4, '2025-11-21', '11:00:00.000000', 'EN_PROCESO', 'Cliente reporta p??rdida de potencia.', NULL, b'1', 70.00, NULL, NULL, NULL),
(3, NULL, 3, 3, 3, 3, '2025-11-22', '14:00:00.000000', 'PENDIENTE', 'Revisar ruido en el freno delantero.', NULL, b'0', 0.00, NULL, NULL, NULL),
(4, NULL, 4, 4, 1, 2, '2025-11-23', '10:00:00.000000', 'COMPLETADA', 'Cambio de ambas llantas.', NULL, b'1', 80.00, NULL, NULL, NULL),
(6, NULL, 6, 6, 3, 1, '2025-11-25', '09:30:00.000000', 'CONFIRMADA', 'Mantenimiento de los 20,000 km.', NULL, b'1', 50.00, NULL, NULL, NULL),
(7, NULL, 7, 7, 1, 5, '2025-11-26', '11:30:00.000000', 'PENDIENTE', 'Fallo en luces direccionales.', NULL, b'0', 0.00, NULL, NULL, NULL),
(8, 'CITA-1763358361319', 9, 1, 1, 1, '2025-11-20', '09:00:00.000000', 'PENDIENTE', 'Test detallado de creacion de cita', NULL, b'1', 50.00, '/api/comprobantes/view/test-comprobante.webp', '2025-11-17 00:46:01.000000', '2025-11-17 00:46:01.000000'),
(9, 'CITA-1763358372848', 10, 6, 5, 9, '2025-11-20', '04:27:00.000000', 'PENDIENTE', 'Hola ', NULL, b'1', 50.00, '/api/comprobantes/view/dcff337a-4b1a-4ab5-b5a7-537d14623aa5.png', '2025-11-17 00:46:12.000000', '2025-11-17 00:46:12.000000'),
(10, 'CITA-1763359174210', 11, 1, 1, 1, '2025-11-24', '04:00:00.000000', 'PENDIENTE', 'Cita creada para Daniel Ynoan', NULL, b'1', 100.00, NULL, '2025-11-17 00:59:34.000000', '2025-11-17 00:59:34.000000'),
(11, 'CITA-1763359867169', 12, 2, 2, 2, '2025-11-27', '08:30:00.000000', 'PENDIENTE', 'Servicio de mantenimiento completo', NULL, b'1', 150.00, '/api/comprobantes/view/0c991e8b-a63f-4f68-a856-89e7f1b2bb22.png', '2025-11-17 01:11:07.000000', '2025-11-17 09:17:36.000000'),
(12, 'CITA-1763360072597', 13, 5, 6, 6, '2025-11-21', '06:12:00.000000', 'PENDIENTE', 'Hola Mundo', NULL, b'1', 100.00, '/api/comprobantes/view/c0ff59b1-c8ea-49bb-9d4d-b33dbc5550fd.webp', '2025-11-17 01:14:32.000000', '2025-11-17 01:14:32.000000'),
(13, 'CITA-1763362214830', 13, 7, 6, 2, '2025-11-18', '05:52:00.000000', 'PENDIENTE', 'Reserva.', NULL, b'1', 100.00, '/api/comprobantes/view/abd48b6a-b996-46f5-a993-26698023a7b7.png', '2025-11-17 01:50:14.000000', '2025-11-17 01:50:14.000000');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita_repuesto`
--

CREATE TABLE `cita_repuesto` (
  `id_cita_repuesto` bigint(20) NOT NULL,
  `cita_id` bigint(20) NOT NULL,
  `repuesto_id` bigint(20) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT 1,
  `precio_unitario` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `id_cliente` bigint(20) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `apellidos` varchar(255) NOT NULL,
  `dni` varchar(20) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `telefono` varchar(20) NOT NULL,
  `direccion` varchar(500) DEFAULT NULL,
  `estado` bit(1) DEFAULT b'1',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id_cliente`, `nombre`, `apellidos`, `dni`, `correo`, `telefono`, `direccion`, `estado`, `created_at`, `updated_at`) VALUES
(1, 'Brayan', 'Sanchez', '74125896', 'carlos.gomez@example.com', '987654321', 'Lima - Peru', b'1', '2025-11-16 23:30:09.000000', '2025-11-17 01:11:40.000000'),
(2, 'Juan', 'Perez', '87654321', 'juan.perez@example.com', '912345678', 'Calle Secundaria 456', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(3, 'Javier', 'Rodriguez', '11223344', 'javier.rodriguez@example.com', '945678901', 'Jr. Central 789', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(4, 'Laura', 'Perez', '10099887', 'laura.perez@example.com', '956789012', 'Av. Los Girasoles 321', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(5, 'David', 'Sanchez', '20033445', 'david.sanchez@example.com', '967890123', 'Calle Las Palmeras 654', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(6, 'Sofia', 'Ramirez', '30044556', 'sofia.ramirez@example.com', '978901234', 'Av. El Sol 987', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(7, 'Elena', 'Vargas', '50066778', 'elena.vargas@example.com', '990123456', 'Calle La Luna 159', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(8, 'Soft', '', '74125896', 'softwa2673@gmail.com', '951236874', 'Lima Calle a22', b'1', '2025-11-16 23:38:32.000000', '2025-11-16 23:38:32.000000'),
(9, 'Brayan', 'Als', '74123658', 'brayasn23@gmail.com', '951472368', 'Lima Calle SA25', b'1', '2025-11-17 00:06:39.000000', '2025-11-17 00:06:39.000000'),
(10, 'Soft', '', 'Ybosn', 'softw2452@gmail.com', '998754653', 'Lima Calle a123', b'1', '2025-11-17 00:28:09.000000', '2025-11-17 00:28:09.000000'),
(11, 'Daniel', 'Ynoan', '12345678', 'daniel.ynoan@example.com', '987654321', 'Lima, Perú', b'1', '2025-11-17 00:59:33.000000', '2025-11-17 00:59:33.000000'),
(12, 'María', 'González', '87654321', 'maria.gonzalez@example.com', '999888777', 'Av. Principal 123, Lima, Peru', b'1', '2025-11-17 01:11:06.000000', '2025-11-17 09:17:36.000000'),
(13, 'Brass', '', '741258963', 'brass253@gmail.com', '951236874', 'Lima, Lince', b'1', '2025-11-17 01:14:32.000000', '2025-11-17 01:50:14.000000');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `disponibilidad_tecnico`
--

CREATE TABLE `disponibilidad_tecnico` (
  `id_disponibilidad` bigint(20) NOT NULL,
  `tecnico_id` bigint(20) NOT NULL,
  `dia_semana` enum('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO') NOT NULL,
  `hora_inicio` time(6) NOT NULL,
  `hora_fin` time(6) NOT NULL,
  `disponible` bit(1) DEFAULT b'1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `disponibilidad_tecnico`
--

INSERT INTO `disponibilidad_tecnico` (`id_disponibilidad`, `tecnico_id`, `dia_semana`, `hora_inicio`, `hora_fin`, `disponible`) VALUES
(55, 1, 'LUNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(56, 1, 'MARTES', '02:00:00.000000', '14:00:00.000000', b'1'),
(57, 1, 'MIERCOLES', '02:00:00.000000', '14:00:00.000000', b'1'),
(58, 1, 'JUEVES', '02:00:00.000000', '14:00:00.000000', b'1'),
(59, 1, 'VIERNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(60, 1, 'SABADO', '02:00:00.000000', '14:00:00.000000', b'1'),
(61, 2, 'LUNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(62, 2, 'MARTES', '02:00:00.000000', '14:00:00.000000', b'1'),
(63, 2, 'MIERCOLES', '02:00:00.000000', '14:00:00.000000', b'1'),
(64, 2, 'JUEVES', '02:00:00.000000', '14:00:00.000000', b'1'),
(65, 2, 'VIERNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(66, 2, 'SABADO', '02:00:00.000000', '14:00:00.000000', b'1'),
(67, 3, 'LUNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(68, 3, 'MARTES', '02:00:00.000000', '14:00:00.000000', b'1'),
(69, 3, 'MIERCOLES', '02:00:00.000000', '14:00:00.000000', b'1'),
(70, 3, 'JUEVES', '02:00:00.000000', '14:00:00.000000', b'1'),
(71, 3, 'VIERNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(72, 3, 'SABADO', '02:00:00.000000', '14:00:00.000000', b'1'),
(73, 4, 'LUNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(74, 4, 'MARTES', '02:00:00.000000', '14:00:00.000000', b'1'),
(75, 4, 'MIERCOLES', '02:00:00.000000', '14:00:00.000000', b'1'),
(76, 4, 'JUEVES', '02:00:00.000000', '14:00:00.000000', b'1'),
(77, 4, 'VIERNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(78, 4, 'SABADO', '02:00:00.000000', '14:00:00.000000', b'1'),
(79, 5, 'LUNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(80, 5, 'MARTES', '02:00:00.000000', '14:00:00.000000', b'1'),
(81, 5, 'MIERCOLES', '02:00:00.000000', '14:00:00.000000', b'1'),
(82, 5, 'JUEVES', '02:00:00.000000', '14:00:00.000000', b'1'),
(83, 5, 'VIERNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(84, 5, 'SABADO', '02:00:00.000000', '14:00:00.000000', b'1'),
(85, 6, 'LUNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(86, 6, 'MARTES', '02:00:00.000000', '14:00:00.000000', b'1'),
(87, 6, 'MIERCOLES', '02:00:00.000000', '14:00:00.000000', b'1'),
(88, 6, 'JUEVES', '02:00:00.000000', '14:00:00.000000', b'1'),
(89, 6, 'VIERNES', '02:00:00.000000', '14:00:00.000000', b'1'),
(90, 6, 'SABADO', '02:00:00.000000', '14:00:00.000000', b'1');

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

--
-- Volcado de datos para la tabla `instalacion`
--

INSERT INTO `instalacion` (`costo_estimado`, `fecha_instlacion`, `arranque_id`, `id_mantenimiento`, `id_socio_registro`) VALUES
(300, '2025-11-20', 1, 1, 1),
(350, '2025-11-21', 3, 2, 2),
(400, '2025-11-22', 6, 3, 1),
(320, '2025-11-23', 7, 4, 2),
(380, '2025-11-24', 9, 5, 1),
(410, '2025-11-25', 10, 6, 2);

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

--
-- Volcado de datos para la tabla `mantenimientos`
--

INSERT INTO `mantenimientos` (`arranque_id`, `categoria_mantenimiento_id`, `fecha_mantenimiento`, `id_mantenimiento`, `socio_registro_id`, `descripcion_mantenimiento`, `tipo_mantenimiento`) VALUES
(2, 1, '2025-11-15 10:00:00.000000', 1, 1, 'Revisi??n anual de tuber??as', 'PREVENTIVO'),
(5, 2, '2025-11-16 11:00:00.000000', 2, 1, 'Reparaci??n de fuga en medidor', 'CORRECTIVO'),
(8, 1, '2025-11-17 14:00:00.000000', 3, 2, 'Limpieza de tanque', 'PREVENTIVO'),
(2, 5, '2025-11-18 09:00:00.000000', 4, 2, 'Instalaci??n de nuevo grifo', 'MEJORA'),
(5, 4, '2025-11-19 16:00:00.000000', 5, 1, 'Atenci??n por rotura de tuber??a principal', 'EMERGENCIA');

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
  `id_moto` bigint(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `placa` varchar(20) NOT NULL,
  `marca` varchar(100) NOT NULL,
  `modelo` varchar(100) NOT NULL,
  `anio` int(11) DEFAULT NULL,
  `nro_serie_motor` varchar(100) DEFAULT NULL,
  `numero_chasis` varchar(100) DEFAULT NULL,
  `tipo_combustible` varchar(50) DEFAULT NULL,
  `kilometraje_actual` int(11) DEFAULT NULL,
  `estado` enum('ACTIVO','EN_REPARACION','VENDIDA','INACTIVO') DEFAULT 'ACTIVO',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `año` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `moto`
--

INSERT INTO `moto` (`id_moto`, `cliente_id`, `placa`, `marca`, `modelo`, `anio`, `nro_serie_motor`, `numero_chasis`, `tipo_combustible`, `kilometraje_actual`, `estado`, `created_at`, `updated_at`, `año`) VALUES
(1, 1, 'ABC-111', 'Honda', 'CBR500R', 2025, NULL, NULL, NULL, 15000, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-17 01:10:18.000000', NULL),
(2, 12, 'DEF-222', 'Yamaha', 'MT-07', 2025, NULL, NULL, NULL, 25000, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-17 09:17:36.000000', NULL),
(3, 3, 'GHI-333', 'Suzuki', 'V-Strom 650', 2021, 'MTR-003', 'CHS-003', 'Gasolina', 35000, 'EN_REPARACION', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(4, 4, 'JKL-444', 'Kawasaki', 'Ninja 400', 2022, 'MTR-004', 'CHS-004', 'Gasolina', 12000, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(5, 13, 'TRE-123', 'Bajaj', 'Pulsar NS200', 2024, NULL, NULL, NULL, 45000, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-17 01:14:32.000000', NULL),
(6, 6, 'PQR-666', 'KTM', 'Duke 390', 2021, 'MTR-006', 'CHS-006', 'Gasolina', 18000, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(7, 13, 'GSS-532', 'BMW', 'G 310 R', 2021, NULL, NULL, NULL, 22000, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-17 01:50:14.000000', NULL),
(8, 1, 'VWX-888', 'Honda', 'Navi', 2023, 'MTR-008', 'CHS-008', 'Gasolina', 5000, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(9, 2, 'YZA-999', 'Yamaha', 'FZ-S', 2018, 'MTR-009', 'CHS-009', 'Gasolina', 30000, 'VENDIDA', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(10, 3, 'BCD-123', 'Suzuki', 'Gixxer 150', 2020, 'MTR-010', 'CHS-010', 'Gasolina', 40000, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `repuesto`
--

CREATE TABLE `repuesto` (
  `id_repuesto` bigint(20) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `marca` varchar(100) DEFAULT NULL,
  `modelo_compatible` varchar(100) DEFAULT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `precio` decimal(10,2) NOT NULL,
  `estado` enum('DISPONIBLE','BAJO_STOCK','AGOTADO') DEFAULT 'DISPONIBLE',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `repuesto`
--

INSERT INTO `repuesto` (`id_repuesto`, `nombre`, `marca`, `modelo_compatible`, `stock`, `precio`, `estado`, `created_at`, `updated_at`) VALUES
(1, 'Filtro de Aceite', 'Bosch', 'Universal', 1, 25.00, 'BAJO_STOCK', '2025-11-16 23:30:09.000000', '2025-11-17 07:46:44.000000'),
(2, 'Pastillas de Freno Delanteras', 'Brembo', 'Yamaha MT-07', 30, 120.00, 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(3, 'Kit de Arrastre', 'DID', 'Honda CBR500R', 20, 350.00, 'BAJO_STOCK', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(4, 'Bujia de Iridio', 'NGK', 'Universal', 100, 45.00, 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-17 02:26:07.000000'),
(5, 'Llanta Trasera 180/55-17', 'Michelin', 'Deportivas', 15, 450.00, 'BAJO_STOCK', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(6, 'Bateria de Gel', 'Yuasa', 'Universal', 25, 250.00, 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-17 02:26:25.000000'),
(7, 'Filtro de Aire de Alto Flujo', 'K&N', 'Universal', 40, 180.00, 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(8, 'Aceite Sint??tico 10W-40', 'Motul', '4 Tiempos', 80, 60.00, 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(9, 'Manigueta de Freno Regulable', 'Rizoma', 'Universal', 100, 280.00, 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-17 02:22:41.000000'),
(10, 'Espejo Retrovisor Deportivo', 'Puig', 'Universal', 35, 150.00, 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000');

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

--
-- Volcado de datos para la tabla `reserva`
--

INSERT INTO `reserva` (`fecha_reserva`, `hora_reserva`, `created_at`, `id_reserva`, `moto_id`, `registro_por`, `socio_id`, `updated_at`, `codigo_reserva`, `comentario`, `estado_registro`, `estado_reserva`) VALUES
('2025-12-01', '10:00:00.000000', NULL, 1, 1, 1, 3, NULL, NULL, 'Reserva para chequeo general', 'ACTIVO', 'PENDIENTE'),
('2025-12-02', '11:00:00.000000', NULL, 2, 2, 1, 4, NULL, NULL, 'Reserva para cambio de aceite', 'ACTIVO', 'PENDIENTE'),
('2025-12-03', '14:00:00.000000', NULL, 3, 3, 1, 7, NULL, NULL, 'Cliente pospuso la visita', 'ACTIVO', 'CANCELADA'),
('2025-12-04', '09:00:00.000000', NULL, 4, 4, 1, 8, NULL, NULL, 'Servicio completado con ??xito', 'ACTIVO', 'FINALIZADA'),
('2025-12-05', '15:00:00.000000', NULL, 5, 5, 1, 9, NULL, NULL, 'Revisi??n de sistema de escape', 'ACTIVO', 'PENDIENTE'),
('2025-12-06', '10:30:00.000000', NULL, 6, 6, 1, 10, NULL, NULL, 'Ajuste de cadena', 'ACTIVO', 'PENDIENTE'),
('2025-12-08', '11:30:00.000000', NULL, 7, 7, 1, 12, NULL, NULL, 'Problema con el arranque en fr??o', 'ACTIVO', 'PENDIENTE'),
('2025-12-09', '16:00:00.000000', NULL, 8, 8, 1, 3, NULL, NULL, 'Instalaci??n de exploradoras', 'ACTIVO', 'PENDIENTE'),
('2025-12-10', '14:30:00.000000', NULL, 9, 2, 1, 4, NULL, NULL, 'Balanceo de ruedas', 'ACTIVO', 'PENDIENTE'),
('2025-12-11', '12:00:00.000000', NULL, 10, 10, 1, 7, NULL, NULL, 'Chequeo de luces', 'ACTIVO', 'PENDIENTE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `id_rol` bigint(20) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`id_rol`, `descripcion`, `created_at`, `updated_at`) VALUES
(1, 'ROLE_ADMIN', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(2, 'ROLE_TECNICO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(3, 'ROLE_RECEPCIONISTA', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol_socio`
--

CREATE TABLE `rol_socio` (
  `id_rol_socio` bigint(20) NOT NULL,
  `rol_id` bigint(20) NOT NULL,
  `socio_id` bigint(20) NOT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `rol_socio`
--

INSERT INTO `rol_socio` (`id_rol_socio`, `rol_id`, `socio_id`, `estado`, `created_at`, `updated_at`, `descripcion`) VALUES
(1, 1, 1, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(2, 2, 2, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(3, 2, 3, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(4, 2, 4, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(5, 2, 5, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(6, 2, 6, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(7, 2, 7, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(8, 3, 8, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL),
(9, 3, 9, 'ACTIVO', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sector`
--

CREATE TABLE `sector` (
  `id_sector` bigint(20) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `nombre_sector` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `sector`
--

INSERT INTO `sector` (`id_sector`, `descripcion`, `nombre_sector`) VALUES
(1, 'Sector norte de la ciudad', 'Norte'),
(2, 'Sector sur de la ciudad', 'Sur'),
(3, 'Zona c??ntrica', 'Centro'),
(4, 'Sector este de la ciudad', 'Este'),
(5, 'Sector oeste de la ciudad', 'Oeste');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `socio`
--

CREATE TABLE `socio` (
  `id_socio` bigint(20) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `apellidos` varchar(255) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `dni` varchar(255) DEFAULT NULL,
  `estado` bit(1) DEFAULT b'1',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `estado_civil` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `fecha_registro` date DEFAULT NULL,
  `genero` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `socio`
--

INSERT INTO `socio` (`id_socio`, `nombre`, `apellidos`, `correo`, `contrasena`, `telefono`, `dni`, `estado`, `created_at`, `updated_at`, `estado_civil`, `fecha_nacimiento`, `fecha_registro`, `genero`) VALUES
(1, 'Admin', 'Sistema', 'admin@luckymotos.com', '$2a$10$QGgC2ykKhwWLIppUFpxcNuFMAkvMp3FMdqLF3JAXwrVaMwYxQ5YSW', '999999999', '00000000', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL, NULL, NULL, NULL),
(2, 'Luis', 'Fernandez', 'luis.fernandez@luckymotos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '923456789', '11223344', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL, NULL, NULL, NULL),
(3, 'Maria', 'Lopez', 'maria.lopez@luckymotos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '934567890', '44556677', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL, NULL, NULL, NULL),
(4, 'Jorge', 'Torres', 'jorge.torres@luckymotos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '989012345', '40055667', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL, NULL, NULL, NULL),
(5, 'Pedro', 'Garcia', 'pedro.garcia@luckymotos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '991234567', '60077889', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL, NULL, NULL, NULL),
(6, 'Roberto', 'Mendez', 'roberto.mendez@luckymotos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '992345678', '70088990', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL, NULL, NULL, NULL),
(7, 'Fernando', 'Castro', 'fernando.castro@luckymotos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '993456789', '80099011', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL, NULL, NULL, NULL),
(8, 'Ana', 'Martinez', 'ana.martinez@luckymotos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '912345678', '87654321', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL, NULL, NULL, NULL),
(9, 'Laura', 'Perez', 'laura.perez@luckymotos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '956789012', '10099887', b'1', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000', NULL, NULL, NULL, NULL);

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
  `id_tecnico` bigint(20) NOT NULL,
  `socio_id` bigint(20) NOT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `estado` enum('DISPONIBLE','OCUPADO','INACTIVO') NOT NULL DEFAULT 'DISPONIBLE',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `tecnico`
--

INSERT INTO `tecnico` (`id_tecnico`, `socio_id`, `especialidad`, `estado`, `created_at`, `updated_at`) VALUES
(1, 2, 'Mecanica General', 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-17 09:18:14.000000'),
(2, 3, 'Electricidad', 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(3, 4, 'Frenos y Suspension', 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-17 07:16:13.000000'),
(4, 5, 'Mecinica Avanzada', 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-17 07:12:41.000000'),
(5, 6, 'Sistema El??ctrico', 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-16 23:30:09.000000'),
(6, 7, 'Diagnostico y Reparacion', 'DISPONIBLE', '2025-11-16 23:30:09.000000', '2025-11-17 07:16:29.000000');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_servicio`
--

CREATE TABLE `tipo_servicio` (
  `id_tipo_servicio` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `precio_base` decimal(10,2) NOT NULL,
  `duracion_estimada_minutos` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `tipo_servicio`
--

INSERT INTO `tipo_servicio` (`id_tipo_servicio`, `nombre`, `descripcion`, `precio_base`, `duracion_estimada_minutos`) VALUES
(1, 'Mantenimiento General', 'Revision completa de la moto, cambio de aceite y filtros.', 150.00, 120),
(2, 'Cambio de Llantas', 'Instalaci??n de llantas nuevas y balanceo.', 80.00, 60),
(3, 'Revisi??n de Frenos', 'Inspeccion y ajuste de sistema de frenos.', 150.00, 90),
(4, 'Afinamiento de Motor', 'Limpieza de inyectores y calibraci??n.', 200.00, 180),
(5, 'Sistema El??ctrico', 'Diagn??stico y reparaci??n de fallas el??ctricas.', 120.00, 150),
(6, 'Cambio de Kit de Arrastre', 'Reemplazo de cadena, pi????n y corona.', 180.00, 100),
(7, 'Servicio de Suspensi??n', 'Mantenimiento de horquillas y amortiguador.', 160.00, 110),
(8, 'Diagn??stico por Esc??ner', 'Uso de esc??ner para detectar fallas electr??nicas.', 90.00, 45),
(9, 'Pintura y Planchado', 'Reparaci??n de carenados y pintura general.', 500.00, 480),
(10, 'Lavado Premium', 'Lavado detallado y encerado de la moto.', 50.00, 70);

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
-- Volcado de datos para la tabla `vivienda`
--

INSERT INTO `vivienda` (`activo`, `created_at`, `nro_familias`, `updated_at`, `id_vivienda`, `sector_id`, `direccion`, `manzana`, `numero_lote`) VALUES
(b'1', NULL, NULL, NULL, 1, 1, 'Av. Principal 123', 'A', '10'),
(b'1', NULL, NULL, NULL, 2, 2, 'Calle Secundaria 456', 'B', '20'),
(b'1', NULL, NULL, NULL, 3, 3, 'Jr. Central 789', 'C', '30'),
(b'1', NULL, NULL, NULL, 4, 1, 'Av. Los Girasoles 321', 'D', '15'),
(b'1', NULL, NULL, NULL, 5, 2, 'Calle Las Palmeras 654', 'E', '25'),
(b'1', NULL, NULL, NULL, 6, 4, 'Av. El Sol 987', 'F', '5'),
(b'1', NULL, NULL, NULL, 7, 5, 'Calle La Luna 159', 'G', '35'),
(b'1', NULL, NULL, NULL, 8, 3, 'Jr. Las Estrellas 753', 'H', '40'),
(b'1', NULL, NULL, NULL, 9, 1, 'Av. Los Pinos 852', 'I', '50'),
(b'1', NULL, NULL, NULL, 10, 2, 'Calle Los Eucaliptos 963', 'J', '55');

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
  ADD UNIQUE KEY `codigo_cita` (`codigo_cita`),
  ADD KEY `fk_cita_cliente` (`cliente_id`),
  ADD KEY `fk_cita_moto` (`moto_id`),
  ADD KEY `fk_cita_tecnico` (`tecnico_id`),
  ADD KEY `fk_cita_tipo_servicio` (`tipo_servicio_id`);

--
-- Indices de la tabla `cita_repuesto`
--
ALTER TABLE `cita_repuesto`
  ADD PRIMARY KEY (`id_cita_repuesto`),
  ADD KEY `fk_cita_repuesto_cita` (`cita_id`),
  ADD KEY `fk_cita_repuesto_repuesto` (`repuesto_id`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`id_cliente`),
  ADD KEY `idx_cliente_dni` (`dni`),
  ADD KEY `idx_cliente_correo` (`correo`);

--
-- Indices de la tabla `disponibilidad_tecnico`
--
ALTER TABLE `disponibilidad_tecnico`
  ADD PRIMARY KEY (`id_disponibilidad`),
  ADD KEY `fk_disponibilidad_tecnico` (`tecnico_id`);

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
  ADD KEY `fk_moto_cliente` (`cliente_id`);

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
  ADD PRIMARY KEY (`id_rol`),
  ADD UNIQUE KEY `descripcion` (`descripcion`);

--
-- Indices de la tabla `rol_socio`
--
ALTER TABLE `rol_socio`
  ADD PRIMARY KEY (`id_rol_socio`),
  ADD KEY `fk_rol_socio_rol` (`rol_id`),
  ADD KEY `fk_rol_socio_socio` (`socio_id`);

--
-- Indices de la tabla `sector`
--
ALTER TABLE `sector`
  ADD PRIMARY KEY (`id_sector`);

--
-- Indices de la tabla `socio`
--
ALTER TABLE `socio`
  ADD PRIMARY KEY (`id_socio`),
  ADD UNIQUE KEY `correo` (`correo`),
  ADD UNIQUE KEY `uk_socio_correo` (`correo`);

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
  ADD UNIQUE KEY `uk_tecnico_socio` (`socio_id`);

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
  MODIFY `id_arranque` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `categoria_mantenimiento`
--
ALTER TABLE `categoria_mantenimiento`
  MODIFY `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `cita`
--
ALTER TABLE `cita`
  MODIFY `id_cita` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `cita_repuesto`
--
ALTER TABLE `cita_repuesto`
  MODIFY `id_cita_repuesto` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `id_cliente` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `disponibilidad_tecnico`
--
ALTER TABLE `disponibilidad_tecnico`
  MODIFY `id_disponibilidad` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT de la tabla `instalacion`
--
ALTER TABLE `instalacion`
  MODIFY `id_mantenimiento` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `instalacion_socio`
--
ALTER TABLE `instalacion_socio`
  MODIFY `id_instalacion_socio` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `mantenimientos`
--
ALTER TABLE `mantenimientos`
  MODIFY `id_mantenimiento` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `mantenimiento_socio`
--
ALTER TABLE `mantenimiento_socio`
  MODIFY `id_mantenimiento_socio` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `moto`
--
ALTER TABLE `moto`
  MODIFY `id_moto` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `repuesto`
--
ALTER TABLE `repuesto`
  MODIFY `id_repuesto` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `reserva`
--
ALTER TABLE `reserva`
  MODIFY `id_reserva` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id_rol` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `rol_socio`
--
ALTER TABLE `rol_socio`
  MODIFY `id_rol_socio` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `sector`
--
ALTER TABLE `sector`
  MODIFY `id_sector` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `socio`
--
ALTER TABLE `socio`
  MODIFY `id_socio` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `socio_vivienda`
--
ALTER TABLE `socio_vivienda`
  MODIFY `id_socio_vivienda` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tecnico`
--
ALTER TABLE `tecnico`
  MODIFY `id_tecnico` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `tipo_servicio`
--
ALTER TABLE `tipo_servicio`
  MODIFY `id_tipo_servicio` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `vivienda`
--
ALTER TABLE `vivienda`
  MODIFY `id_vivienda` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

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
  ADD CONSTRAINT `fk_cita_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id_cliente`),
  ADD CONSTRAINT `fk_cita_moto` FOREIGN KEY (`moto_id`) REFERENCES `moto` (`id_moto`),
  ADD CONSTRAINT `fk_cita_tecnico` FOREIGN KEY (`tecnico_id`) REFERENCES `tecnico` (`id_tecnico`),
  ADD CONSTRAINT `fk_cita_tipo_servicio` FOREIGN KEY (`tipo_servicio_id`) REFERENCES `tipo_servicio` (`id_tipo_servicio`);

--
-- Filtros para la tabla `cita_repuesto`
--
ALTER TABLE `cita_repuesto`
  ADD CONSTRAINT `fk_cita_repuesto_cita` FOREIGN KEY (`cita_id`) REFERENCES `cita` (`id_cita`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_cita_repuesto_repuesto` FOREIGN KEY (`repuesto_id`) REFERENCES `repuesto` (`id_repuesto`);

--
-- Filtros para la tabla `disponibilidad_tecnico`
--
ALTER TABLE `disponibilidad_tecnico`
  ADD CONSTRAINT `fk_disponibilidad_tecnico` FOREIGN KEY (`tecnico_id`) REFERENCES `tecnico` (`id_tecnico`);

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
  ADD CONSTRAINT `fk_moto_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id_cliente`);

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
  ADD CONSTRAINT `fk_rol_socio_rol` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id_rol`),
  ADD CONSTRAINT `fk_rol_socio_socio` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id_socio`);

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
  ADD CONSTRAINT `fk_tecnico_socio` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id_socio`);

--
-- Filtros para la tabla `vivienda`
--
ALTER TABLE `vivienda`
  ADD CONSTRAINT `FKg3yerdl6tvvcolgvi9nbgsb2f` FOREIGN KEY (`sector_id`) REFERENCES `sector` (`id_sector`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
