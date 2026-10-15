DROP DATABASE IF EXISTS `videoclub`;
-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS videoclub
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE videoclub;

-- -------------------------------------------------------
--  Limpiar tablas si ya existían (útil para re-ejecutar)
-- -------------------------------------------------------
DROP TABLE IF EXISTS alquileres;
DROP TABLE IF EXISTS peliculas;

-- -------------------------------------------------------
--  Tabla: peliculas
-- -------------------------------------------------------
CREATE TABLE peliculas (
    id         INT          NOT NULL,
    titulo     VARCHAR(200) NOT NULL,
    genero     VARCHAR(50)  NOT NULL,         -- 'comedia', 'accion', 'drama' u otro
    anyo       INT          NOT NULL,
    disponible BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  Tabla: alquileres
-- -------------------------------------------------------
CREATE TABLE alquileres (
    id          INT          NOT NULL,
    id_pelicula INT          NOT NULL,
    cliente     VARCHAR(200) NOT NULL,
    dni         VARCHAR(20)  NOT NULL,
    dias        INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_pelicula FOREIGN KEY (id_pelicula)
        REFERENCES peliculas(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  Datos de prueba — películas
--  Incluye los tres géneros con tarifa propia
--  para poder verificar todos los cálculos
-- -------------------------------------------------------
INSERT INTO peliculas (id, titulo, genero, anyo, disponible) VALUES
(1,  'Supercool',           'comedia', 2022, TRUE),   -- 2,00 €/día
(2,  'Mad Max: Fury Road',  'accion',  2015, TRUE),   -- 3,00 €/día
(3,  'La vida es bella',    'drama',   1997, TRUE),   -- 2,50 €/día
(4,  'Los Vengadores',      'accion',  2012, TRUE),   -- 3,00 €/día
(5,  'Shrek',               'comedia', 2001, TRUE),   -- 2,00 €/día
(6,  'Forrest Gump',        'drama',   1994, TRUE),   -- 2,50 €/día
(7,  'El Padrino',          'drama',   1972, TRUE),   -- 2,50 €/día
(8,  'Inception',           'accion',  2010, TRUE),   -- 3,00 €/día
(9,  'Kung Fu Panda',       'comedia', 2008, TRUE),   -- 2,00 €/día
(10, 'Documental Cosmos',   'documental', 2014, TRUE); -- 1,50 €/día (otro género)

-- -------------------------------------------------------
--  Sin alquileres iniciales
--  Los alquileres se crean desde la aplicación Java
-- -------------------------------------------------------

-- -------------------------------------------------------
--  Verificación rápida
-- -------------------------------------------------------
SELECT CONCAT('Películas cargadas: ', COUNT(*)) AS resultado FROM peliculas;
SELECT CONCAT('Alquileres iniciales: ', COUNT(*)) AS resultado FROM alquileres;
