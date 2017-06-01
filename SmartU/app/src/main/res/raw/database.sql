PRAGMA foreign_keys = ON;
DROP TABLE IF EXISTS  `vacanteproyecto`;
CREATE TABLE `vacanteproyecto` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDProyecto`	INTEGER
);
DROP TABLE IF EXISTS  `vacante`;
CREATE TABLE `vacante` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`experienciaNecesaria`	TEXT,
	`IDVacante`	INTEGER,
	`IDEspecialidad`	INTEGER
);
DROP TABLE IF EXISTS  `usuariointeresaarea`;
CREATE TABLE `usuariointeresaarea` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDArea`	INTEGER,
	`IDUsuario`	INTEGER
);
DROP TABLE IF EXISTS  `usuarioespecialidad`;
CREATE TABLE `usuarioespecialidad` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`experiencia`	TEXT,
	`IDEspecialidad`	INTEGER,
	`IDUsuario`	INTEGER
);
DROP TABLE IF EXISTS  `usuariocolaboradorproyecto`;
CREATE TABLE `usuariocolaboradorproyecto` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDUsuario`	INTEGER,
	`IDProyecto`	INTEGER,
	`IDEspecialidad`	INTEGER
);
DROP TABLE IF EXISTS  `usuario`;
CREATE TABLE `usuario` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`nombre`	TEXT,
	`apellidos`	TEXT,
	`user`	varchar(100),
	`password`	varchar(300),
	`biografia`	TEXT,
	`web`	TEXT,
	`nPuntos`	INTEGER,
	`CIF`	varchar(100),
	`verificado`	INTEGER,
	`admin`	INTEGER,
	`IDImagenPerfil`	INTEGER
);
DROP TABLE IF EXISTS  `status`;
CREATE TABLE `status` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`nombre`	varchar(100),
	`puntos`	INTEGER,
	`IDUsuario`	INTEGER
);
DROP TABLE IF EXISTS  `solicitudunion`;
CREATE TABLE `solicitudunion` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`fecha`	timestamp,
	`descripcion`	TEXT,
	`IDUsuarioSolicitante`	INTEGER,
	`IDProyecto`	INTEGER,
	`IDGrupoTrabajo`	INTEGER
);
DROP TABLE IF EXISTS  `seguidor`;
CREATE TABLE `seguidor` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDUsuario`	INTEGER,
	`IDUsuarioSeguido`	INTEGER
);
DROP TABLE IF EXISTS  `redsocial`;
CREATE TABLE `redsocial` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDUsuario`	INTEGER,
	`IDProyecto`	INTEGER,
	`nombre`	varchar(100),
	`url`	varchar(300)
);
DROP TABLE IF EXISTS  `proyectohastag`;
CREATE TABLE `proyectohastag` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDProyecto`	INTEGER,
	`IDHashtag`	INTEGER
);
DROP TABLE IF EXISTS  `proyecto`;
CREATE TABLE `proyecto` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`nombre`	varchar(200),
	`descripcion`	TEXT,
	`web`	varchar(300),
	`fechaCreacion`	timestamp,
	`fechaFinalizacion`	timestamp,
	`localizacion`	TEXT,
	`coordenadas`	varchar(100),
	`IDImagenDestacada`	INTEGER,
	`IDUsuario`	INTEGER
);
DROP TABLE IF EXISTS  `notificacion`;
CREATE TABLE `notificacion` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`fecha`	timestamp,
	`nombre`	varchar(300),
	`descripcion`	TEXT,
	`IDProyecto`	INTEGER,
	`IDUsuario`	INTEGER
);
DROP TABLE IF EXISTS  `multimedia`;
CREATE TABLE `multimedia` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDProyecto`	INTEGER,
	`IDAvance`	INTEGER,
	`nombre`	TEXT,
	`urlPreview`	TEXT,
	`url`	TEXT,
	`tipo`	varchar(300),
	`urlSubtitulos`	TEXT
);
DROP TABLE IF EXISTS  `hashtag`;
CREATE TABLE `hashtag` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`nombre`	varchar(300)
);
DROP TABLE IF EXISTS  `especialidad`;
CREATE TABLE `especialidad` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`nombre`	TEXT,
	`descripcion`	TEXT
);
DROP TABLE IF EXISTS  `comentarios`;
CREATE TABLE `comentarios` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDUsuario`	INTEGER,
	`IDProyecto`	INTEGER,
	`descripcion`	TEXT,
	`fecha`	timestamp
);
DROP TABLE IF EXISTS  `buenaidea`;
CREATE TABLE `buenaidea` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDUsuario`	INTEGER,
	`IDProyecto`	INTEGER
);
DROP TABLE IF EXISTS  `avance`;
CREATE TABLE `avance` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`fecha`	timestamp,
	`nombre`	TEXT,
	`descripcion`	TEXT,
	`IDUsuario`	INTEGER,
	`IDProyecto`	INTEGER,
	`IDImagenDestacada`	INTEGER
);
DROP TABLE IF EXISTS  `areaproyecto`;
CREATE TABLE `areaproyecto` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`IDProyecto`	INTEGER,
	`IDArea`	INTEGER
);
DROP TABLE IF EXISTS  `area`;
CREATE TABLE `area` (
	`id`	INTEGER NOT NULL PRIMARY KEY ,
	`nombre`	TEXT,
	`descripcion`	TEXT,
	`IDImagenDestacada`	INTEGER
);
COMMIT;
