-- =============================================
-- Base de Datos: Authors
-- Creada para el Desafio 2 - Directorio de Autores
-- Materia: SPP901
-- =============================================

CREATE DATABASE IF NOT EXISTS Authors;
USE Authors;

-- Tabla de generos literarios
CREATE TABLE IF NOT EXISTS literarygenre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Tabla principal de autores
CREATE TABLE IF NOT EXISTS author (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    birthdate DATE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    genre_id INT NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES literarygenre(id)
);

-- Insertar algunos generos de ejemplo
INSERT INTO literarygenre (name) VALUES 
    ('Drama'),
    ('Terror'),
    ('Comedia'),
    ('Romance'),
    ('Ciencia Ficcion'),
    ('Fantasia'),
    ('Misterio');

-- Insertar autores de ejemplo (como aparecen en el mockup)
INSERT INTO author (name, birthdate, phone, genre_id) VALUES
    ('Mauricio Figueroa', '1987-01-01', '2250-5555', 1),
    ('Joel Amaya', '1997-12-01', '2250-5555', 2);
