-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         5.6.15-enterprise-commercial-advanced - MySQL Enterprise Server - Advanced Edition (Commercial)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             8.3.0.4694
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Volcando estructura para tabla escolar.adeudos
CREATE TABLE IF NOT EXISTS `adeudos` (
  `idAdeudos` int(11) NOT NULL,
  `idAlumno_FK` int(11) NOT NULL,
  `Monto` double NOT NULL,
  `Fecha` date NOT NULL,
  `Motivo` varchar(45) NOT NULL,
  `FechaPago` varchar(50) NOT NULL,
  PRIMARY KEY (`idAdeudos`),
  KEY `FK_adeudos_alumnos` (`idAlumno_FK`),
  CONSTRAINT `FK_adeudos_alumnos` FOREIGN KEY (`idAlumno_FK`) REFERENCES `alumnos` (`idAlumnos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla escolar.alumnos
CREATE TABLE IF NOT EXISTS `alumnos` (
  `idAlumnos` int(11) NOT NULL,
  `idCarrera_FK` int(11) DEFAULT NULL,
  `nombre` varchar(35) NOT NULL,
  PRIMARY KEY (`idAlumnos`),
  KEY `FK_alumnos_carreras` (`idCarrera_FK`),
  CONSTRAINT `FK_alumnos_carreras` FOREIGN KEY (`idCarrera_FK`) REFERENCES `carreras` (`idCarrera`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla escolar.carreras
CREATE TABLE IF NOT EXISTS `carreras` (
  `idCarrera` int(11) NOT NULL,
  `nombre` varchar(35) NOT NULL,
  PRIMARY KEY (`idCarrera`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla escolar.catalogos
CREATE TABLE IF NOT EXISTS `catalogos` (
  `idCatalogo` int(11) NOT NULL,
  `idMateria_FK` int(11) DEFAULT NULL,
  `idMaestro_FK` int(11) DEFAULT NULL,
  `Hora` varchar(15) NOT NULL,
  `Aula` varchar(45) NOT NULL,
  `Perior` varchar(45) NOT NULL,
  PRIMARY KEY (`idCatalogo`),
  KEY `FK_Catalogos_materias` (`idMateria_FK`),
  KEY `FK_Catalogos_maestros` (`idMaestro_FK`),
  CONSTRAINT `FK_Catalogos_maestros` FOREIGN KEY (`idMaestro_FK`) REFERENCES `maestros` (`idMaestros`),
  CONSTRAINT `FK_Catalogos_materias` FOREIGN KEY (`idMateria_FK`) REFERENCES `materias` (`idMateria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla escolar.grupos
CREATE TABLE IF NOT EXISTS `grupos` (
  `idGrupos` int(11) NOT NULL,
  `idCatalogo_FK` int(11) DEFAULT NULL,
  `idAlumno_FK` int(11) DEFAULT NULL,
  PRIMARY KEY (`idGrupos`),
  KEY `FK__catalogos` (`idCatalogo_FK`),
  KEY `FK_Grupos_alumnos` (`idAlumno_FK`),
  CONSTRAINT `FK_Grupos_alumnos` FOREIGN KEY (`idAlumno_FK`) REFERENCES `alumnos` (`idAlumnos`),
  CONSTRAINT `FK__catalogos` FOREIGN KEY (`idCatalogo_FK`) REFERENCES `catalogos` (`idCatalogo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla escolar.maestros
CREATE TABLE IF NOT EXISTS `maestros` (
  `idMaestros` int(11) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Departamento` varchar(45) NOT NULL,
  PRIMARY KEY (`idMaestros`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla escolar.materias
CREATE TABLE IF NOT EXISTS `materias` (
  `idMateria` int(11) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Costo` double NOT NULL,
  PRIMARY KEY (`idMateria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla escolar.pagos
CREATE TABLE IF NOT EXISTS `pagos` (
  `idPagos` int(11) NOT NULL,
  `idAlumno_FK` int(11) NOT NULL,
  `Monto` double NOT NULL,
  `Fecha` date NOT NULL,
  PRIMARY KEY (`idPagos`),
  KEY `FK__alumnos` (`idAlumno_FK`),
  CONSTRAINT `FK__alumnos` FOREIGN KEY (`idAlumno_FK`) REFERENCES `alumnos` (`idAlumnos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
