-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Servidor: db684697551.db.1and1.com
-- Tiempo de generación: 19-06-2017 a las 20:14:32
-- Versión del servidor: 5.5.55-0+deb7u1-log
-- Versión de PHP: 5.4.45-0+deb7u8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `db684697551`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `area`
--

CREATE TABLE IF NOT EXISTS `area` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` text COLLATE latin1_spanish_ci,
  `descripcion` text COLLATE latin1_spanish_ci,
  `idImagenDestacada` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=7 ;

--
-- Volcado de datos para la tabla `area`
--

INSERT INTO `area` (`id`, `nombre`, `descripcion`, `idImagenDestacada`) VALUES
(1, 'Informática', NULL, NULL),
(2, 'Diseño gráfico', NULL, NULL),
(3, 'Arquitectura', NULL, NULL),
(4, 'Comunicaciones', NULL, NULL),
(5, 'Audio visuales', NULL, NULL),
(6, 'Empresariales', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `areaProyecto`
--

CREATE TABLE IF NOT EXISTS `areaProyecto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idProyecto` int(11) DEFAULT NULL,
  `idArea` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `areaProyecto`
--

INSERT INTO `areaProyecto` (`id`, `idProyecto`, `idArea`) VALUES
(1, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `avance`
--

CREATE TABLE IF NOT EXISTS `avance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL,
  `nombre` text COLLATE latin1_spanish_ci,
  `descripcion` text COLLATE latin1_spanish_ci,
  `idUsuario` int(11) DEFAULT NULL,
  `idProyecto` int(11) DEFAULT NULL,
  `idImagenDestacada` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=4 ;

--
-- Volcado de datos para la tabla `avance`
--

INSERT INTO `avance` (`id`, `fecha`, `nombre`, `descripcion`, `idUsuario`, `idProyecto`, `idImagenDestacada`) VALUES
(1, '2017-06-19 00:00:00', 'Añadida nueva funcionalidad', 'He creado la funcionalidad para ver los avances en el proyecto, comprobemos si funciona', 1, 1, 1),
(2, '2017-06-19 20:00:44', 'Funcionalidaf', 'Creo que la red social va a tener más público con las nuevas funcionalidades', 1, 1, 16),
(3, '2017-06-19 20:09:55', 'El último avance', 'El proyecto ya esta acabado parece que ha ido bien', 1, 1, 17);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `buenaIdea`
--

CREATE TABLE IF NOT EXISTS `buenaIdea` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) DEFAULT NULL,
  `idProyecto` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=40 ;

--
-- Volcado de datos para la tabla `buenaIdea`
--

INSERT INTO `buenaIdea` (`id`, `idUsuario`, `idProyecto`) VALUES
(25, 9, 1),
(33, 3, 2),
(6, 7, 1),
(39, 1, 1),
(32, 3, 1),
(35, 12, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comentario`
--

CREATE TABLE IF NOT EXISTS `comentario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) DEFAULT NULL,
  `idProyecto` int(11) DEFAULT NULL,
  `descripcion` text COLLATE latin1_spanish_ci,
  `fecha` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=27 ;

--
-- Volcado de datos para la tabla `comentario`
--

INSERT INTO `comentario` (`id`, `idUsuario`, `idProyecto`, `descripcion`, `fecha`) VALUES
(1, 2, 1, '!Este proyecto está avanzando!....genial', '2017-06-05 00:00:00'),
(2, 1, 1, 'holaaa', '2017-06-05 00:00:00'),
(3, 7, 1, 'hollaaa', '2017-06-05 00:00:00'),
(4, 7, 1, 'bien', '2017-06-04 00:00:00'),
(5, 7, 1, 'yaa', '2017-06-06 00:00:00'),
(19, 1, 1, 'esto es util', '2017-06-07 12:53:47'),
(18, 1, 1, 'prueba 2', '2017-06-07 12:44:05'),
(17, 1, 1, 'otra prueba de comentarios', '2017-06-07 12:42:14'),
(16, 1, 1, 'creo que ya funciona', '2017-06-07 12:37:43'),
(10, 1, 1, 'no esta mal', '2017-06-06 00:00:00'),
(11, 1, 1, 'voy a probar', '2017-06-07 00:00:00'),
(13, 1, 1, 'hola', '2017-06-07 00:00:00'),
(20, 3, 1, 'hola, va genial', '2017-06-07 16:16:39'),
(21, 1, 1, 'probemos el comentario', '2017-06-08 00:34:08'),
(22, 9, 1, 'donde puedo encontrar más información?  como podría participar? ', '2017-06-08 09:36:17'),
(23, 3, 1, 'Me parece un proyecto interesante', '2017-06-08 16:31:05'),
(24, 3, 2, 'ok', '2017-06-08 19:51:55'),
(25, 1, 2, 'Es un nuevo proyecto', '2017-06-15 20:25:53'),
(26, 1, 2, 'no se sabe aún de que trata?', '2017-06-15 20:51:08');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `especialidad`
--

CREATE TABLE IF NOT EXISTS `especialidad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` text COLLATE latin1_spanish_ci,
  `descripcion` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=5 ;

--
-- Volcado de datos para la tabla `especialidad`
--

INSERT INTO `especialidad` (`id`, `nombre`, `descripcion`) VALUES
(1, 'Informática', NULL),
(2, 'Diseñador gráfico', NULL),
(3, 'Marketer', NULL),
(4, 'Maquetar de video', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hashtag`
--

CREATE TABLE IF NOT EXISTS `hashtag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(300) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `multimedia`
--

CREATE TABLE IF NOT EXISTS `multimedia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idProyecto` int(11) DEFAULT NULL,
  `idAvance` int(11) DEFAULT NULL,
  `nombre` text COLLATE latin1_spanish_ci,
  `urlPreview` text COLLATE latin1_spanish_ci,
  `url` text COLLATE latin1_spanish_ci,
  `tipo` varchar(300) COLLATE latin1_spanish_ci DEFAULT NULL,
  `urlSubtitulos` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=18 ;

--
-- Volcado de datos para la tabla `multimedia`
--

INSERT INTO `multimedia` (`id`, `idProyecto`, `idAvance`, `nombre`, `urlPreview`, `url`, `tipo`, `urlSubtitulos`) VALUES
(1, 1, 0, 'Logo', 'logo_web.png', 'logo_web.png', 'imagen', NULL),
(2, 1, 0, 'Las nuevas vistas del proyecto', NULL, '512583717.819704.jpg', 'imagen360', NULL),
(3, 1, 0, 'Video equipo', NULL, 'Equi.mp4', 'video', NULL),
(4, 1, 0, 'Capturas del equipo 360', '', '512587754.218986.jpg', 'imagen360', NULL),
(5, 1, 0, 'Preparando la reunion 360', '', '512579692.825717.jpg', 'imagen360', NULL),
(6, 1, 0, 'Preparando el Design Thinking', NULL, 'IMG_3896.JPG', 'imagen', NULL),
(7, 1, 0, 'Preparando el rodaje del spot publicitario', 'IMG_3901.JPG', 'IMG_3901.JPG', 'imagen', NULL),
(8, 1, 0, 'Primera reunión del equipo', '', 'IMG_20170125_204049.jpg', 'imagen', NULL),
(9, 1, 0, 'Explicando que es el Design Thinking', '', 'IMG_3906.JPG', 'imagen', NULL),
(10, 1, 0, 'Aportando ideas', '', 'IMG_3913.JPG', 'imagen', NULL),
(11, 1, 0, 'Juanjo probando la app SmartU', '', 'IMG_4649.JPG', 'imagen', NULL),
(12, 1, 0, 'Alejandro probando la app SmartU', 'IMG_4650.JPG', 'IMG_4650.JPG', 'imagen', NULL),
(13, 1, 0, 'Explicación del proyecto en PDF', NULL, 'smartUGR-TFG-multidisciplinar.pdf', 'pdf', NULL),
(14, NULL, NULL, '$1', NULL, 'imgproject_SmartU_1497894281688.jpg', 'imagen', NULL),
(15, NULL, NULL, '$1', NULL, 'imgproject_SmartU_1497894491926.jpg', 'imagen', NULL),
(16, NULL, NULL, NULL, NULL, 'imgproject_SmartU_1497895230526.jpg', 'imagen', NULL),
(17, NULL, NULL, NULL, NULL, 'imgproject_SmartU_1497895800498.jpg', 'imagen', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notificacion`
--

CREATE TABLE IF NOT EXISTS `notificacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL,
  `nombre` varchar(300) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` text COLLATE latin1_spanish_ci,
  `idProyecto` int(11) DEFAULT '0',
  `idUsuario` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=129 ;

--
-- Volcado de datos para la tabla `notificacion`
--

INSERT INTO `notificacion` (`id`, `fecha`, `nombre`, `descripcion`, `idProyecto`, `idUsuario`) VALUES
(1, '2017-06-05 00:00:00', 'Germán a dado buena idea', 'El usuario germán ha dado como buena idea al proyecto SmartU', 1, 3),
(23, '2017-06-07 12:37:42', 'Nuevo comentario en SmartU!', 'El usuario Emilio ha hecho el siguiente comentario creo que ya funciona...', 1, 1),
(10, '2017-06-07 00:00:00', 'Nuevo comentario en SmartU!', 'El usuario Emilio ha hecho el siguiente comentario hola...', 1, 1),
(9, '2017-06-07 00:00:00', 'Nuevo comentario en !', 'El usuario Germán ha hecho el siguiente comentario ...', 0, 3),
(11, '2017-06-07 00:00:00', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(13, '2017-06-07 00:00:00', 'Nuevo comentario en !', 'El usuario Germán ha hecho el siguiente comentario ...', 0, 3),
(14, '2017-06-07 09:33:38', 'Nuevo comentario en !', 'El usuario Germán ha hecho el siguiente comentario ...', 0, 3),
(15, '2017-06-07 11:10:52', 'El usuario Emilio tiene nuevos intereses!', 'Al usuario Emilio ahora le interesan Informática, Informática, Audio visuales, Empresariales, Diseño gráfico', 0, 1),
(16, '2017-06-07 11:14:34', 'El usuario Emilio tiene nuevos intereses!', 'Al usuario Emilio ahora le interesan Informática, Informática, Audio visuales, Empresariales, Arquitectura', 0, 1),
(17, '2017-06-07 11:22:44', 'El usuario Emilio tiene nuevos intereses!', 'Al usuario Emilio ahora le interesan Informática, Informática, Audio visuales, Empresariales, Comunicaciones', 0, 1),
(18, '2017-06-07 11:46:31', 'El proyecto SmartU es una buena idea!', 'El usuario Germán ha dado una buena idea al proyecto SmartU', 1, 3),
(19, '2017-06-07 11:46:39', 'El proyecto SmartU es una buena idea!', 'El usuario Germán ha dado una buena idea al proyecto SmartU', 1, 3),
(20, '2017-06-07 11:47:34', 'El usuario Germán tiene nuevos intereses!', 'Al usuario Germán ahora le interesan Informática, Diseño gráfico', 0, 3),
(21, '2017-06-07 11:47:42', 'El usuario Germán tiene nuevos intereses!', 'Al usuario Germán ahora le interesan Informática, Diseño gráfico', 0, 3),
(22, '2017-06-07 11:59:36', 'El usuario Emilio tiene nuevos intereses!', 'Al usuario Emilio ahora le interesan Informática, Informática, Empresariales, Arquitectura', 0, 1),
(24, '2017-06-07 12:42:14', 'Nuevo comentario en SmartU!', 'El usuario Emilio ha hecho el siguiente comentario otra prueba de comentarios...', 1, 1),
(25, '2017-06-07 12:44:04', 'Nuevo comentario en SmartU!', 'El usuario Emilio ha hecho el siguiente comentario prueba 2...', 1, 1),
(26, '2017-06-07 12:53:47', 'Nuevo comentario en SmartU!', 'El usuario Emilio ha hecho el siguiente comentario esto es util...', 1, 1),
(27, '2017-06-07 12:54:36', 'El usuario Emilio tiene nuevos intereses!', 'Al usuario Emilio ahora le interesan Informática, Empresariales, Arquitectura', 0, 1),
(28, '2017-06-07 13:00:35', 'El usuario Emilio tiene nuevos intereses!', 'Al usuario Emilio ahora le interesan Arquitectura', 0, 1),
(29, '2017-06-07 13:00:53', 'El usuario Emilio tiene nuevos intereses!', 'Al usuario Emilio ahora le interesan Arquitectura, Informática, Empresariales', 0, 1),
(30, '2017-06-07 15:57:50', 'El proyecto SmartU es una buena idea!', 'El usuario Germán ha dado una buena idea al proyecto SmartU', 1, 3),
(31, '2017-06-07 16:05:47', 'El usuario Germán tiene nuevos intereses!', 'Al usuario Germán ahora le interesan Informática, Informática, Informática, Diseño gráfico, Diseño gráfico, Diseño gráfico, Audio visuales', 0, 3),
(32, '2017-06-07 16:11:12', 'El usuario Germán ha seguido a alguien!', 'El usuario Germán ahora está siguiendo a Irene', 0, 3),
(33, '2017-06-07 16:11:16', 'El usuario Germán ha seguido a alguien!', 'El usuario Germán ahora está siguiendo a Irene', 0, 3),
(34, '2017-06-07 16:11:57', 'El usuario Germán ha seguido a alguien!', 'El usuario Germán ahora está siguiendo a Juanjo', 0, 3),
(35, '2017-06-07 16:12:36', 'El usuario Germán ha seguido a alguien!', 'El usuario Germán ahora está siguiendo a Pepe', 0, 3),
(36, '2017-06-07 16:12:50', 'El usuario Germán ha seguido a alguien!', 'El usuario Germán ahora está siguiendo a Pepe', 0, 3),
(37, '2017-06-07 16:12:54', 'El usuario Germán ha seguido a alguien!', 'El usuario Germán ahora está siguiendo a Pepe', 0, 3),
(38, '2017-06-07 16:16:29', 'Nuevo comentario en SmartU!', 'El usuario Germán ha hecho el siguiente comentario hola, va genial...', 1, 3),
(39, '2017-06-07 17:36:24', 'Nueva solicitud de unión en SmartU!', 'El usuario Javi quiere unirse al proyecto ', 1, 7),
(40, '2017-06-07 17:38:55', 'Nueva solicitud de unión en SmartU!', 'El usuario Javi quiere unirse al proyecto ', 1, 7),
(41, '2017-06-07 17:39:15', 'Nueva solicitud de unión en SmartU!', 'El usuario Javi quiere unirse al proyecto ', 1, 7),
(42, '2017-06-07 17:47:07', 'Nueva solicitud de unión en SmartU!', 'El usuario Javi quiere unirse al proyecto ', 1, 7),
(43, '2017-06-07 17:48:29', 'Nueva solicitud de unión en SmartU!', 'El usuario Javi quiere unirse al proyecto ', 1, 7),
(44, '2017-06-07 17:50:32', 'Nueva solicitud de unión en SmartU!', 'El usuario Javi quiere unirse al proyecto ', 1, 7),
(46, '2017-06-07 18:02:29', 'Nueva solicitud de unión en SmartU!', 'El usuario Javi quiere unirse al proyecto ', 1, 7),
(118, '2017-06-15 20:19:40', 'El proyecto SmartU es una buena idea!', 'El usuario emiliocj ha dado una buena idea al proyecto SmartU', 1, 1),
(116, '2017-06-15 20:19:17', 'El usuario emiliocj ha seguido a alguien!', 'El usuario emiliocj ahora está siguiendo a juanji', 0, 1),
(51, '2017-06-07 18:20:46', 'El usuario Juanjo ha subido de status!', 'El usuario Juanjo ahora es Creador', 0, 2),
(52, '2017-06-07 18:20:47', 'El usuario Emilio ha seguido a alguien!', 'El usuario Emilio ahora está siguiendo a Juanjo', 0, 1),
(53, '2017-06-07 19:03:09', 'El usuario Germán ha subido de status!', 'El usuario Germán ahora es Creador', 0, 3),
(54, '2017-06-07 19:03:09', 'El usuario Emilio ha seguido a alguien!', 'El usuario Emilio ahora está siguiendo a Germán', 0, 1),
(55, '2017-06-07 19:15:00', 'El usuario Javi ha subido de status!', 'El usuario Javi ahora es Novato', 0, 1),
(56, '2017-06-07 19:15:01', 'El usuario Emilio ha seguido a alguien!', 'El usuario Emilio ahora está siguiendo a Javi', 0, 1),
(115, '2017-06-15 20:10:45', 'El usuario emiliocj ha seguido a alguien!', 'El usuario emiliocj ahora está siguiendo a juanji', 0, 1),
(114, '2017-06-15 20:06:11', 'El usuario emiliocj ha seguido a alguien!', 'El usuario emiliocj ahora está siguiendo a juanji', 0, 1),
(57, '2017-06-07 20:53:49', 'El usuario Javi ha subido de status!', 'El usuario Javi ahora es Novato', 0, 7),
(58, '2017-06-07 21:00:25', 'El usuario Javi ha subido de status!', 'El usuario Javi ahora es Novato', 0, 7),
(59, '2017-06-07 21:00:26', 'El usuario Emilio ha seguido a alguien!', 'El usuario Emilio ahora está siguiendo a Javi', 0, 1),
(60, '2017-06-07 21:46:32', 'El usuario Javi ha subido de status!', 'El usuario Javi ahora es Novato', 0, 7),
(61, '2017-06-07 21:46:45', 'El usuario Javi ha subido de status!', 'El usuario Javi ahora es Novato', 0, 7),
(62, '2017-06-07 21:47:30', 'El usuario Javi ha subido de status!', 'El usuario Javi ahora es Novato', 0, 7),
(63, '2017-06-07 21:51:11', 'El usuario Javi ha subido de status!', 'El usuario Javi ahora es Novato', 0, 7),
(64, '2017-06-07 21:56:57', 'El usuario Javi ha subido de status!', 'El usuario Javi ahora es Novato', 0, 7),
(65, '2017-06-07 22:01:03', 'El usuario Javi ha subido de status!', 'El usuario Javi ahora es Novato', 0, 7),
(66, '2017-06-07 22:06:48', 'El usuario Germán ha subido de status!', 'El usuario Germán ahora es Creador', 0, 3),
(67, '2017-06-07 22:06:54', 'El usuario Germán ha subido de status!', 'El usuario Germán ahora es Creador', 0, 3),
(68, '2017-06-07 22:06:54', 'El usuario Emilio ha seguido a alguien!', 'El usuario Emilio ahora está siguiendo a Germán', 0, 1),
(117, '2017-06-15 20:19:31', 'El usuario emiliocj ha seguido a alguien!', 'El usuario emiliocj ahora está siguiendo a juanji', 0, 1),
(70, '2017-06-07 22:07:33', 'El usuario Emilio ha seguido a alguien!', 'El usuario Emilio ahora está siguiendo a Guillermo', 0, 1),
(71, '2017-06-07 22:29:18', 'El usuario Emilio ha seguido a alguien!', 'El usuario Emilio ahora está siguiendo a miguel', 0, 1),
(72, '2017-06-07 22:29:59', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(73, '2017-06-07 22:44:55', 'El usuario Emilio ha subido de status!', 'El usuario Emilio ahora es Novato', 0, 1),
(74, '2017-06-07 22:44:55', 'El usuario Emilio ha seguido a alguien!', 'El usuario Emilio ahora está siguiendo a Emilio', 0, 1),
(75, '2017-06-07 23:11:32', 'El usuario Emilio ha subido de status!', 'El usuario Emilio ahora es Creador', 0, 1),
(76, '2017-06-07 23:11:36', 'El usuario Emilio ha subido de status!', 'El usuario Emilio ahora es Novato', 0, 1),
(77, '2017-06-07 23:11:37', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(78, '2017-06-07 23:11:45', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(79, '2017-06-07 23:18:12', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(80, '2017-06-07 23:38:38', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(81, '2017-06-07 23:38:51', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(82, '2017-06-07 23:43:14', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(83, '2017-06-07 23:43:19', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(84, '2017-06-07 23:44:27', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(85, '2017-06-07 23:49:23', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(86, '2017-06-08 00:34:06', 'Nuevo comentario en SmartU!', 'El usuario Emilio ha hecho el siguiente comentario probemos el comentario...', 1, 1),
(87, '2017-06-08 08:27:53', 'El usuario Germán ha seguido a alguien!', 'El usuario Germán ahora está siguiendo a Guillermo', 0, 3),
(88, '2017-06-08 09:31:49', 'El usuario miguel ha seguido a alguien!', 'El usuario miguel ahora está siguiendo a Emilio', 0, 9),
(89, '2017-06-08 09:31:55', 'El usuario miguel ha seguido a alguien!', 'El usuario miguel ahora está siguiendo a Emilio', 0, 9),
(90, '2017-06-08 09:32:20', 'El usuario miguel ha seguido a alguien!', 'El usuario miguel ahora está siguiendo a Emilio', 0, 9),
(91, '2017-06-08 09:32:50', 'El proyecto SmartU es una buena idea!', 'El usuario miguel ha dado una buena idea al proyecto SmartU', 1, 9),
(92, '2017-06-08 09:32:56', 'El proyecto SmartU es una buena idea!', 'El usuario miguel ha dado una buena idea al proyecto SmartU', 1, 9),
(93, '2017-06-08 09:35:13', 'El usuario miguel ha seguido a alguien!', 'El usuario miguel ahora está siguiendo a miguel', 0, 9),
(94, '2017-06-08 09:36:16', 'Nuevo comentario en SmartU!', 'El usuario miguel ha hecho el siguiente comentario donde puedo encontrar más información?  como podría participar? ...', 1, 9),
(95, '2017-06-08 09:37:01', 'El proyecto SmartU es una buena idea!', 'El usuario miguel ha dado una buena idea al proyecto SmartU', 1, 9),
(96, '2017-06-08 10:01:00', 'El proyecto SmartU es una buena idea!', 'El usuario Germán ha dado una buena idea al proyecto SmartU', 1, 3),
(97, '2017-06-08 11:04:35', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(98, '2017-06-08 13:41:59', 'El proyecto SmartU es una buena idea!', 'El usuario Germán ha dado una buena idea al proyecto SmartU', 1, 3),
(99, '2017-06-08 14:31:00', 'El proyecto SmartU es una buena idea!', 'El usuario Emilio ha dado una buena idea al proyecto SmartU', 1, 1),
(100, '2017-06-08 15:26:25', 'El usuario Emilio ha seguido a alguien!', 'El usuario Emilio ahora está siguiendo a Pepe', 0, 1),
(101, '2017-06-08 16:28:12', 'El proyecto Proyecto2 es una buena idea!', 'El usuario Germán ha dado una buena idea al proyecto Proyecto2', 2, 3),
(102, '2017-06-08 16:30:13', 'El proyecto SmartU es una buena idea!', 'El usuario Germán ha dado una buena idea al proyecto SmartU', 1, 3),
(103, '2017-06-08 16:30:53', 'Nuevo comentario en SmartU!', 'El usuario Germán ha hecho el siguiente comentario Me parece un proyecto interesante...', 1, 3),
(104, '2017-06-08 18:53:34', 'El usuario Germán ha seguido a alguien!', 'El usuario Germán ahora está siguiendo a Guillermo', 0, 3),
(105, '2017-06-08 19:03:49', 'El usuario Emilio ha subido de status!', 'El usuario Emilio ahora es Creador', 0, 1),
(106, '2017-06-08 19:45:52', 'El proyecto SmartU es una buena idea!', 'El usuario Germán ha dado una buena idea al proyecto SmartU', 1, 3),
(107, '2017-06-08 19:45:56', 'El proyecto Proyecto2 es una buena idea!', 'El usuario Germán ha dado una buena idea al proyecto Proyecto2', 2, 3),
(108, '2017-06-08 19:51:43', 'Nuevo comentario en Proyecto2!', 'El usuario Germán ha hecho el siguiente comentario ok...', 2, 3),
(109, '2017-06-10 18:02:04', 'El usuario Emilio tiene nuevos intereses!', 'Al usuario Emilio ahora le interesan Informática, Informática, Diseño gráfico, Audio visuales', 0, 1),
(110, '2017-06-11 22:28:22', 'El usuario Germán tiene nuevos intereses!', 'Al usuario Germán ahora le interesan Informática, Informática, Diseño gráfico, Arquitectura', 0, 3),
(111, '2017-06-13 18:40:18', 'El proyecto SmartU es una buena idea!', 'El usuario Alejandro ha dado una buena idea al proyecto SmartU', 1, 12),
(112, '2017-06-13 18:40:26', 'El proyecto SmartU es una buena idea!', 'El usuario Alejandro ha dado una buena idea al proyecto SmartU', 1, 12),
(113, '2017-06-13 18:44:25', 'El usuario Alejandro tiene nuevos intereses!', 'Al usuario Alejandro ahora le interesan Arquitectura, Audio visuales', 0, 12),
(119, '2017-06-15 20:23:42', 'El proyecto SmartU es una buena idea!', 'El usuario emiliocj ha dado una buena idea al proyecto SmartU', 1, 1),
(120, '2017-06-15 20:25:36', 'Nuevo comentario en Proyecto2!', 'El usuario Emilio ha hecho el siguiente comentario Es un nuevo proyecto...', 2, 1),
(121, '2017-06-15 20:50:50', 'Nuevo comentario en Proyecto2!', 'El usuario Emilio ha hecho el siguiente comentario no se sabe aún de que trata?...', 2, 1),
(122, '2017-06-15 20:51:23', 'El proyecto Proyecto2 es una buena idea!', 'El usuario emiliocj ha dado una buena idea al proyecto Proyecto2', 2, 1),
(123, '2017-06-16 15:10:20', 'El usuario emiliocj tiene nuevos intereses!', 'Al usuario emiliocj ahora le interesan Comunicaciones', 0, 1),
(124, '2017-06-16 15:53:43', 'El usuario emiliocj tiene nuevos intereses!', 'Al usuario emiliocj ahora le interesan Arquitectura', 0, 1),
(125, '2017-06-16 20:29:00', 'El proyecto SmartU es una buena idea!', 'El usuario emiliocj ha dado una buena idea al proyecto SmartU', 1, 1),
(126, '2017-06-17 10:24:23', 'El usuario emiliocj tiene nuevos intereses!', 'Al usuario emiliocj ahora le interesan Informática, Arquitectura', 0, 1),
(127, '2017-06-18 18:43:52', 'Nuevo colaborador en SmartU!', 'El usuario javi ahora es colaborador en SmartU', 1, 7),
(128, '2017-06-18 19:16:12', 'Nueva solicitud de unión en SmartU!', 'El usuario pepe quiere unirse al proyecto ', 1, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proyecto`
--

CREATE TABLE IF NOT EXISTS `proyecto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` text COLLATE latin1_spanish_ci,
  `web` varchar(300) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fechaCreacion` date NOT NULL,
  `fechaFinalizacion` date NOT NULL DEFAULT '0000-00-00',
  `localizacion` text COLLATE latin1_spanish_ci,
  `coordenadas` varchar(100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `idImagenDestacada` int(11) DEFAULT NULL,
  `idUsuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `proyecto`
--

INSERT INTO `proyecto` (`id`, `nombre`, `descripcion`, `web`, `fechaCreacion`, `fechaFinalizacion`, `localizacion`, `coordenadas`, `idImagenDestacada`, `idUsuario`) VALUES
(1, 'SmartU', 'La idea general de este proyecto es mediante el uso de herramientas, metodologías y técnicas provenientes de todas las disciplinas integrantes del proyecto se obtenga como resultado un producto final, el cual conecte la Universidad con la ciudad mediante un espacio de coworking de ideas y servicios', 'http://www.smartu.coloredmoon.com', '2017-06-05', '2020-11-30', 'Calle Periodista Daniel Saucedo Aranda, s/n, 18071 Granada', '37.197103, -3.624611', 1, 1),
(2, 'Proyecto2', 'Para probar los filtros', 'http://prueba.com', '2017-06-01', '2017-06-30', 'Calle prueba', '37.209952, -3.620904', 1, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proyectoHashtag`
--

CREATE TABLE IF NOT EXISTS `proyectoHashtag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idProyecto` int(11) DEFAULT NULL,
  `idHashtag` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `redSocial`
--

CREATE TABLE IF NOT EXISTS `redSocial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) DEFAULT NULL,
  `idProyecto` int(11) DEFAULT NULL,
  `nombre` varchar(100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `url` varchar(300) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `redSocial`
--

INSERT INTO `redSocial` (`id`, `idUsuario`, `idProyecto`, `nombre`, `url`) VALUES
(1, 1, 0, 'facebook', 'https://facebook.com'),
(2, 0, 1, 'twitter', 'https://twitter.com');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `seguidor`
--

CREATE TABLE IF NOT EXISTS `seguidor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) DEFAULT NULL,
  `idUsuarioSeguido` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=35 ;

--
-- Volcado de datos para la tabla `seguidor`
--

INSERT INTO `seguidor` (`id`, `idUsuario`, `idUsuarioSeguido`) VALUES
(34, 1, 2),
(2, 2, 3),
(6, 3, 1),
(10, 3, 8),
(28, 9, 9),
(27, 9, 1),
(22, 1, 9),
(15, 3, 4),
(20, 1, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `solicitudUnion`
--

CREATE TABLE IF NOT EXISTS `solicitudUnion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `descripcion` text COLLATE latin1_spanish_ci,
  `idUsuarioSolicitante` int(11) DEFAULT NULL,
  `idProyecto` int(11) DEFAULT NULL,
  `idGrupoTrabajo` int(11) DEFAULT NULL,
  `idVacante` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=10 ;

--
-- Volcado de datos para la tabla `solicitudUnion`
--

INSERT INTO `solicitudUnion` (`id`, `fecha`, `descripcion`, `idUsuarioSolicitante`, `idProyecto`, `idGrupoTrabajo`, `idVacante`) VALUES
(9, '2017-06-18 17:16:12', 'Quiere unirse como diseñador gráfico', 4, 1, NULL, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `status`
--

CREATE TABLE IF NOT EXISTS `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `puntos` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=5 ;

--
-- Volcado de datos para la tabla `status`
--

INSERT INTO `status` (`id`, `nombre`, `puntos`) VALUES
(1, 'Novato', 10),
(2, 'Creador', 20),
(3, 'Master', 30),
(4, 'UltraCreador', 40);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` text COLLATE latin1_spanish_ci NOT NULL,
  `nombre` text COLLATE latin1_spanish_ci,
  `apellidos` text COLLATE latin1_spanish_ci,
  `user` varchar(100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `email` varchar(500) COLLATE latin1_spanish_ci NOT NULL,
  `password` varchar(300) COLLATE latin1_spanish_ci DEFAULT NULL,
  `biografia` text COLLATE latin1_spanish_ci,
  `web` text COLLATE latin1_spanish_ci,
  `nPuntos` int(11) DEFAULT '0',
  `CIF` varchar(100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `verificado` tinyint(1) NOT NULL DEFAULT '0',
  `admin` tinyint(1) DEFAULT '0',
  `imagenPerfil` text COLLATE latin1_spanish_ci,
  `firebaseToken` text COLLATE latin1_spanish_ci NOT NULL,
  `localizacion` text COLLATE latin1_spanish_ci NOT NULL,
  `idStatus` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=14 ;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `uid`, `nombre`, `apellidos`, `user`, `email`, `password`, `biografia`, `web`, `nPuntos`, `CIF`, `verificado`, `admin`, `imagenPerfil`, `firebaseToken`, `localizacion`, `idStatus`) VALUES
(1, 'i7OptZwlUQVVQ5w7bHtrgkD5tW43', 'Emilio', 'Chica Jiménez', 'emiliocj', 'emiliocj@correo.ugr.es', '08327e239bdf940ae87e80361bcdabef3f151e5e', 'Estudiante universitario de la ETSIIT que vive en Granada y es Graduado en Ingeniería Informática', 'http://coloredmoon.com', 5, NULL, 1, 1, 'foto-buena.jpg', 'cxpGB-luhfM:APA91bGhbZYGZhXlNbyjLTMYjywgGn-Q0BQTC41ThCuXJ_GInOSjbfoXOYz0qijPCRjsZuTS4IfQOwP57yP7g8Eoh1lsHKVXy3CGHP1E9GgA4CgjDRdQ2vHKMZcDkQ0UXE037ph1uKqg', '', 1),
(2, 'qJG5OgYElpajoNcMLMRr1M022g02', 'Juanjo', 'Jiménez', 'juanji', 'juanji@gmail.com', '08327e239bdf940ae87e80361bcdabef3f151e5e', 'Estudiante universitario de la ETSIIT que vive en Granada, en Armilla y es Graduado en Ingeniería Informática', 'http://smartu.coloredmoon.com', 20, NULL, 1, 1, 'juanjo.png', 'eK6_q_jLGpA:APA91bFIl6fs4fn_lVZYnrKd7GNF35YZJxEfsylWvry3phZkzrk6Kj68NbnRHAaFEMKqiC1uImNCQxTOx9nfqMZByi-VGsVn2KxhDm8AtJzQuQ-yx0Srpvr3LtgR3ZQUKzPAeUGbcCJN', '', 2),
(3, 'KEKtOZy4mbd42LygAUgOp2ZVr3M2', 'Germán', 'Zayas Cabrera', 'german', 'german@gmail.com', '08327e239bdf940ae87e80361bcdabef3f151e5e', 'Estudiante universitario de la UGR que vive en Granada, en Peligros con el Grado en Bellas Artes', 'http://smartu.coloredmoon.com', 21, NULL, 1, 1, 'german.png', 'e7Vpqt_fDqc:APA91bFi6wenjWJfdI9407xxYZaFJXxTASRSDQmWjFnluhS', '', 2),
(11, 'eKEIHdX3kaNHf2voUUNppJnvXPY2', 'Juan', 'Árbol Gutiérrez', 'Juan', 'juanarguti@gmail.com', '1042805349cd69e8feb85ad54e3c64b325ea927d', NULL, NULL, 0, NULL, 0, 0, NULL, 'd-D9CrNfuVE:APA91bHcw2evRolhXxCUx9CMHNDWahscQULiuU979GPzvF5Wnd3e8q4nElGrxIvZffaqGj_5mQX7SeJYH8-EexAamy2N9uvvGXQWYaf5p5DrovflA8EGmnWmRjRIg5fEARlyWh2_2qYH', '', 1),
(4, 'm2AmdS4doFW4ncuUefwsGxheiHM2', 'Pepe', NULL, 'pepe', 'pepe@gmail.com', '08327e239bdf940ae87e80361bcdabef3f151e5e', NULL, NULL, 2, NULL, 0, 0, NULL, 'fAwGw2nm8e0:APA91bHlbnAizq_eqfoqBn7iOFmyXdG2IeTEiSF97dO1jyJc-fOeehYtkcdFtvpGDkwoLfx7_6CdspzqQeeduXUCM66PZz1KlT1M7Y3MNC_2NeFp8Kr75XzUswqZlkV69QF10Xdil1nP', '', 1),
(7, 'kAAgXQyN39PdRji90m6bkBxMAEI2', 'Javi', NULL, 'javi', 'javi@gmail.com', '08327e239bdf940ae87e80361bcdabef3f151e5e', NULL, NULL, -5, NULL, 0, 0, NULL, 'fQpPY0dhubM:APA91bFEc6X6uVRcq_-qVb8CUvMtm4zUDz9uxHXhMDyz11rSvZYCyF1uLAcFvPfARFZZI4ONVIQFxZFsmgqdhoXum-IcurfTPXj58DcI3tY7_qBOaXzMSBX0ZoM-7uu8Az0QevJhGj6H', '', 1),
(8, 'GQshqdXQpDWw3YTfHkS8IMmF1l53', 'Irene', 'Castillo', 'irenecastillo', 'irenecastillo.epal@gmail.com', 'b6058081de07d738742fc7f57d14b678e82dce41', NULL, NULL, 0, NULL, 0, 0, NULL, 'dL0B2xYyqGE:APA91bGKRTtbN4Q1cqsRjrbPDL_D1Qgls8ZPUXWtVwyqllZuj5tK2ncIBjfBbo74W1nIrGJtSE7QhUSgmbk5taxN93a1CguZzMssWAG8GIYZ9h__ph1zL', '', 1),
(9, 'vkLAbMAKy9TKJdhlWTy5frjz99Z2', 'miguel', 'gea', 'mgea', 'usalab@gmail.com', '08327e239bdf940ae87e80361bcdabef3f151e5e', NULL, NULL, 2, NULL, 0, 0, NULL, 'eOZpTRrn8e4:APA91bEFMc3VQNQpVNNV7wcg4R9QFlaXqNwKvPoYwUbTVUTYUeuoNwsscDnJHii9YD-zT599uA0vNB_f6fLjm7EodEN_xK0mwxDYNbQBlAcFYkfkYzh6XcHWdkWYOfb2y54ayOI0idvb', '', 1),
(13, '0zboHmhx4OMfTxWjI68vawWXsAR2', 'Victoria', 'Guerra', 'vguerra', 'vguerramolina@gmail.com', '5e921455e0283a03f081f8bcdbb33b3a1234356f', NULL, NULL, 0, NULL, 0, 0, NULL, 'eWmnnHCtW1o:APA91bEJUhHxar8OnLdWjufsEBuuVutY3a6KCKrXDUWqHyRRVwlj9-udFab16q5jsDT1vthKzXTK_fsqF6L_EfoYJ7O3yO1yJQ6706yG4oc2dupT95TCw6nMc4ENv0g58WUBgF0tT7C8', '', 1),
(12, 'ulMBZ9cSM3OlkmxXfhmPi0Fec242', 'Alejandro', 'Grindlay', 'alejandrogrindlay', 'alejandrogrindlay@gmail.com', '70ad72ad6e6aacc70805b7db18c1d7dcec0b5c84', NULL, NULL, 0, NULL, 0, 0, NULL, 'dckU4r1rawo:APA91bHfHzTOKcyu7qFrlCcJuGZo5Amu9ukBXsM-FzWBIZAQ0Doj--n3P6I1T5Nnc6bdF2Qi81uVMS-1cVywQWFuwHcobe6XkB80V4pRjeFKcigVPoH9yMXsRY49jov-rPQ1WWYD5HZT', '', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarioColaboradorProyecto`
--

CREATE TABLE IF NOT EXISTS `usuarioColaboradorProyecto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) DEFAULT NULL,
  `idProyecto` int(11) DEFAULT NULL,
  `idEspecialidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=5 ;

--
-- Volcado de datos para la tabla `usuarioColaboradorProyecto`
--

INSERT INTO `usuarioColaboradorProyecto` (`id`, `idUsuario`, `idProyecto`, `idEspecialidad`) VALUES
(1, 1, 1, 1),
(2, 2, 1, 1),
(3, 3, 1, 2),
(4, 7, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarioEspecialidad`
--

CREATE TABLE IF NOT EXISTS `usuarioEspecialidad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `experiencia` text COLLATE latin1_spanish_ci,
  `idEspecialidad` int(11) DEFAULT NULL,
  `idUsuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `usuarioEspecialidad`
--

INSERT INTO `usuarioEspecialidad` (`id`, `experiencia`, `idEspecialidad`, `idUsuario`) VALUES
(1, 'Casi 11 años de experiencia en la área de informática', 1, 1),
(2, 'Casi 5 años de experiencia en la área de informática', 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarioInteresaArea`
--

CREATE TABLE IF NOT EXISTS `usuarioInteresaArea` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idArea` int(11) DEFAULT NULL,
  `idUsuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=64 ;

--
-- Volcado de datos para la tabla `usuarioInteresaArea`
--

INSERT INTO `usuarioInteresaArea` (`id`, `idArea`, `idUsuario`) VALUES
(2, 1, 2),
(3, 6, 7),
(4, 3, 7),
(5, 5, 7),
(14, 2, 2),
(15, 1, 3),
(16, 2, 3),
(63, 3, 1),
(32, 1, 3),
(55, 3, 3),
(56, 3, 12),
(57, 5, 12),
(62, 1, 1),
(53, 5, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vacante`
--

CREATE TABLE IF NOT EXISTS `vacante` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `experienciaNecesaria` text COLLATE latin1_spanish_ci,
  `idVacante` int(11) DEFAULT NULL,
  `idEspecialidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=5 ;

--
-- Volcado de datos para la tabla `vacante`
--

INSERT INTO `vacante` (`id`, `experienciaNecesaria`, `idVacante`, `idEspecialidad`) VALUES
(3, 'Al menos 2 años de diseño gráfico', 2, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vacanteProyecto`
--

CREATE TABLE IF NOT EXISTS `vacanteProyecto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idProyecto` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=4 ;

--
-- Volcado de datos para la tabla `vacanteProyecto`
--

INSERT INTO `vacanteProyecto` (`id`, `idProyecto`) VALUES
(2, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
