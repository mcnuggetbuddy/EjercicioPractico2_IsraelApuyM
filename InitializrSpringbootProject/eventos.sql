-- Usuario de BD
DROP USER IF EXISTS 'usuario_caso_practico_02'@'localhost';

CREATE USER 'usuario_caso_practico_02'@'localhost'
IDENTIFIED BY '12345';

-- Crear BD
DROP DATABASE IF EXISTS eventos;

CREATE DATABASE eventos
CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;

 GRANT ALL PRIVILEGES ON eventos.* TO 'usuario_caso_practico_02'@'localhost';
  FLUSH PRIVILEGES;  

USE eventos;

-- Tabla roles 
CREATE TABLE rol (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla usuarios
CREATE TABLE usuario (
id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
nombre VARCHAR(150),
email VARCHAR(200) UNIQUE,
password VARCHAR(255),
rol_id BIGINT UNSIGNED,
activo BOOLEAN DEFAULT TRUE,
fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (rol_id) REFERENCES rol(id) 
);

-- Tabla relación usuario-rol (ManyToMany)
CREATE TABLE usuario_rol (
    id_usuario BIGINT UNSIGNED NOT NULL,
    id_rol     BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_rol)     REFERENCES rol(id)
);

-- Tabla eventos
CREATE TABLE evento (
id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
nombre VARCHAR(150),
descripcion TEXT,
fecha DATE,
capacidad INT,
activo BOOLEAN DEFAULT TRUE
);

-- Datos de prueba
INSERT INTO rol (nombre) VALUES ('ADMIN'), ('ORGANIZADOR'), ('CLIENTE');
INSERT INTO usuario (nombre, email, password, rol_id) VALUES
('Admin',       'admin@email.com',    '$2a$10$0qTz21PPSX7VMDvOCAVsH.oojCQP.PAuzHG7cYVIR6NRKrdO0tD32', 1),
('Organizador', 'org@email.com',      '$2a$10$Kb9UvhW.9vdlSMXB5hev1eSKXSGJp1O.Q/BZrEEiJ1VM2Aupzp1sy', 2),
('Cliente',     'cliente@email.com',  '$2a$10$B08.CHC.nH1tc0F9iQa9Cu9qD1w42o.MXqhtqGVo5Et0lcgMxBedK', 3);
INSERT INTO evento (nombre, descripcion, fecha, capacidad) VALUES ('Conferencia Tech', 'Evento de tecnología', '2026-05-10', 100), ('Taller Web', 'Curso práctico', '2026-06-15', 50);

-- Asignar roles en la tabla ManyToMany (id_usuario, id_rol)
-- Admin(1) -> ADMIN(1), Organizador(2) -> ORGANIZADOR(2), Cliente(3) -> CLIENTE(3)
INSERT INTO usuario_rol (id_usuario, id_rol) VALUES (1, 1), (2, 2), (3, 3);