-- ========================================
-- BASE DE DATOS: BIBLIOTECA DON BOSCO
-- ========================================
DROP DATABASE IF EXISTS biblioteca;
CREATE DATABASE biblioteca;
USE biblioteca;

-- ========================================
-- 1. TIPO USUARIO
-- ========================================
CREATE TABLE tipo_usuario (
    id_tipo INT PRIMARY KEY AUTO_INCREMENT,
    nombre_tipo VARCHAR(20) UNIQUE NOT NULL,
    descripcion VARCHAR(100)
);

-- ========================================
-- 2. USUARIOS
-- ========================================
CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    id_tipo INT NOT NULL,
    tiene_mora BOOLEAN DEFAULT FALSE,
    monto_mora DECIMAL(10,2) DEFAULT 0.00,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_tipo) REFERENCES tipo_usuario(id_tipo)
);

-- ========================================
-- 3. TIPO DOCUMENTO
-- ========================================
CREATE TABLE tipo_documento (
    id_tipo_doc INT PRIMARY KEY AUTO_INCREMENT,
    nombre_tipo VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200)
);

-- ========================================
-- 4. CATEGORÍA
-- ========================================
CREATE TABLE categoria (
    id_categoria INT PRIMARY KEY AUTO_INCREMENT,
    nombre_categoria VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200)
);

-- ========================================
-- 5. UBICACIÓN
-- ========================================
CREATE TABLE ubicacion (
    id_ubicacion INT PRIMARY KEY AUTO_INCREMENT,
    edificio VARCHAR(10) NOT NULL,
    piso VARCHAR(10) NOT NULL,
    seccion VARCHAR(10) NOT NULL,
    estante VARCHAR(10) NOT NULL,
    descripcion VARCHAR(200)
);

-- ========================================
-- 6. EJEMPLAR
-- ========================================
CREATE TABLE ejemplar (
    id_ejemplar INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(100),
    editorial VARCHAR(100),
    isbn VARCHAR(20),
    anio_publicacion INT,
    id_tipo_documento INT NOT NULL,
    id_categoria INT NOT NULL,
    id_ubicacion INT NOT NULL,
    numero_edicion VARCHAR(20),
    idioma VARCHAR(20) DEFAULT 'Español',
    num_paginas INT,
    descripcion TEXT,
    cantidad_total INT DEFAULT 1,
    cantidad_disponible INT DEFAULT 1,
    fecha_ingreso DATE DEFAULT (CURDATE()),
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_tipo_documento) REFERENCES tipo_documento(id_tipo_doc),
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
    FOREIGN KEY (id_ubicacion) REFERENCES ubicacion(id_ubicacion)
);

-- ========================================
-- 7. PRÉSTAMO
-- ========================================
CREATE TABLE prestamo (
    id_prestamo INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_ejemplar INT NOT NULL,
    fecha_prestamo DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento DATE,
    fecha_devolucion DATETIME NULL,
    mora_calculada DECIMAL(10,2) DEFAULT 0.00,
    estado VARCHAR(20) DEFAULT 'Activo',
    observaciones TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_ejemplar) REFERENCES ejemplar(id_ejemplar)
);

-- ========================================
-- 8. CONFIGURACIÓN
-- ========================================
CREATE TABLE configuracion (
    id_config INT PRIMARY KEY AUTO_INCREMENT,
    max_prestamos_alumno INT DEFAULT 3,
    max_prestamos_profesor INT DEFAULT 5,
    dias_prestamo_alumno INT DEFAULT 7,
    dias_prestamo_profesor INT DEFAULT 14,
    mora_diaria DECIMAL(5,2) DEFAULT 1.50,
    anio_aplicacion INT DEFAULT 2025
);

-- ========================================
-- VISTA DE PRUEBA
-- ========================================
CREATE VIEW vista_ejemplares_disponibles AS
SELECT 
    e.id_ejemplar,
    e.titulo, 
    e.autor,
    e.editorial,
    td.nombre_tipo AS tipo_documento,
    c.nombre_categoria,
    u.edificio || '-' || u.piso || '-' || u.seccion || '-' || u.estante AS ubicacion,
    e.cantidad_disponible,
    e.idioma
FROM ejemplar e
JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc
JOIN categoria c ON e.id_categoria = c.id_categoria
JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion
WHERE e.activo = TRUE AND e.cantidad_disponible > 0;

-- ========================================
-- DATOS DE PRUEBA
-- ========================================

-- 1. TIPOS DE USUARIO
INSERT INTO tipo_usuario (nombre_tipo, descripcion) VALUES
('Administrador', 'Acceso total al sistema'),
('Profesor', 'Préstamo por 14 días'),
('Alumno', 'Préstamo por 7 días');

-- 2. USUARIOS
INSERT INTO usuarios (nombre_completo, correo, usuario, contrasena, id_tipo) VALUES
('Ana López', 'ana@donbosco.edu', 'ana.lopez', 'ana2025', 1),     -- ADMIN
('Luis Torres', 'luis@donbosco.edu', 'ltorres', 'luis123', 2),     -- PROFESOR
('Sofía Mendoza', 'sofia@donbosco.edu', 'smendoza', 'sofia2025', 3), -- ALUMNO
('Carlos Rivera', 'carlos@donbosco.edu', 'crivera', 'carlos2025', 3),
('María Gómez', 'maria@donbosco.edu', 'mgomez', 'maria2025', 2);

-- 3. TIPOS DE DOCUMENTO
INSERT INTO tipo_documento (nombre_tipo, descripcion) VALUES
('Libro', 'Libros impresos'),
('Tesis', 'Trabajos de grado'),
('Revista', 'Publicaciones periódicas'),
('Manual', 'Guías técnicas'),
('Informe', 'Documentos internos'),
('CD', 'CD Audio'),
('DVD', 'DVD Video');

-- 4. CATEGORÍAS
INSERT INTO categoria (nombre_categoria, descripcion) VALUES
('Tecnología', 'Programación, redes, IA'),
('Ciencias', 'Física, química, biología'),
('Humanidades', 'Filosofía, historia, literatura'),
('Matemáticas', 'Álgebra, cálculo, estadística'),
('Arte', 'Música, pintura, diseño');

-- 5. UBICACIONES FÍSICAS
INSERT INTO ubicacion (edificio, piso, seccion, estante, descripcion) VALUES
('A', '1', 'A', '01', 'Fondo general - Tecnología'),
('A', '1', 'B', '12', 'Matemáticas y ciencias'),
('A', '2', 'C', '05', 'Tesis de ingeniería'),
('B', '1', 'A', '08', 'Revistas científicas'),
('B', '2', 'D', '03', 'Humanidades'),
('C', '1', 'B', '15', 'Manuales técnicos');

-- 6. EJEMPLARES (con todos los campos)
INSERT INTO ejemplar (
    titulo, autor, editorial, isbn, anio_publicacion, numero_edicion, num_paginas,
    id_tipo_documento, id_categoria, id_ubicacion, cantidad_total, cantidad_disponible,
    idioma, descripcion, fecha_ingreso
) VALUES
('Programación en Java', 'Herbert Schildt', 'McGraw-Hill', '978-0134685991', 2020, '8va', 800, 1, 1, 1, 2, 2, 'Español', 'Libro de referencia para POO', '2025-01-15'),
('Cálculo Diferencial', 'James Stewart', 'Cengage Learning', '978-970-10-7654-0', 2019, '7ma', 650, 1, 4, 2, 1, 1, 'Español', 'Texto universitario', '2025-02-10'),
('Tesis: Redes 5G', 'Carlos Ramírez', 'UDB', 'TESIS-001', 2024, '1ra', 120, 2, 1, 3, 1, 0, 'Español', 'Trabajo de grado - PRESTADO', '2025-03-01'),
('Revista IEEE', 'IEEE', 'IEEE Press', 'ISSN-0018-9219', 2025, 'Vol. 113', 150, 3, 1, 4, 3, 3, 'Inglés', 'Publicación mensual', '2025-04-05'),
('Historia del Arte', 'E.H. Gombrich', 'Phaidon', '978-0714832470', 1995, '16va', 688, 1, 5, 5, 1, 1, 'Español', 'Clásico del arte', '2025-01-20'),
('Manual de MySQL', 'Paul DuBois', 'O''Reilly', '978-0596009578', 2008, '3ra', 700, 4, 1, 6, 1, 1, 'Español', 'Guía completa de bases de datos', '2025-02-28');

-- 7. PRÉSTAMOS ACTIVOS (para probar mora y devolución)
INSERT INTO prestamo (id_usuario, id_ejemplar, fecha_prestamo, fecha_vencimiento, estado, observaciones) VALUES
(3, 3, '2025-10-25 10:00:00', '2025-11-01', 'Activo', 'Préstamo atrasado - Mora activa'), -- Sofía
(2, 1, '2025-11-05 14:30:00', '2025-11-19', 'Activo', 'Profesor - 14 días'), -- Luis
(4, 2, '2025-11-06 09:15:00', '2025-11-13', 'Activo', 'Alumno - 7 días'); -- Carlos

-- 8. CONFIGURACIÓN
INSERT INTO configuracion (
    max_prestamos_alumno, max_prestamos_profesor,
    dias_prestamo_alumno, dias_prestamo_profesor,
    mora_diaria, anio_aplicacion
) VALUES (3, 5, 7, 14, 1.50, 2025);

-- 9. SIMULAR MORA EN USUARIO
UPDATE usuarios SET tiene_mora = TRUE, monto_mora = 18.00 WHERE id_usuario = 3;

-- ========================================
-- VERIFICACIÓN FINAL
-- ========================================
SELECT COUNT(*) AS total_usuarios FROM usuarios;
SELECT COUNT(*) AS total_ejemplares FROM ejemplar;
SELECT COUNT(*) AS prestamos_activos FROM prestamo WHERE estado = 'Activo';
SELECT * FROM vista_ejemplares_disponibles;

Select * from  ejemplar;