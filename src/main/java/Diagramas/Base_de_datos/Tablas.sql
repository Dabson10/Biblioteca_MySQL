-- Usuario
CREATE TABLE biblioteca_mix.usuarios
(
    personaID      VARCHAR(15) PRIMARY KEY NOT NULL,
    nombres        varchar(40)             NOT NULL,
    apellidos      varchar(45)             NOT NULL,
    correo         varchar(40)             NOT NULL,
    clave          varchar(40)             NOT NULL,
    rol            varchar(20)             NOT NULL,
    libro_prestado varchar(15) DEFAULT NULL
);

-- Tabla de Libro, este solo tendra uno por categoria
CREATE TABLE biblioteca_mix.libros
(
    ISBN             VARCHAR(30) PRIMARY KEY NOT NULL,
    titulo           VARCHAR(30)             NOT NULL,
    categoria        VARCHAR(30)             NOT NULL,
    autor            VARCHAR(30)             NOT NULL,
    prefijo_ejemplar VARCHAR(15)             NOT NULL
);

-- Tabla de ejemplar
CREATE TABLE biblioteca_mix.ejemplar
(
    -- Columnas
    codigo_ejemplar varchar(15) PRIMARY KEY NOT NULL,
    codigo_libro    varchar(15)             NOT NULL,
    ubicacion       VARCHAR(30)             NOT NULL,
    tipo            VARCHAR(20)             NOT NULL,
    disponible      boolean DEFAULT TRUE    NOT NULL,
    -- Llaves foraneas
    FOREIGN KEY (codigo_libro) REFERENCES biblioteca_mix.libros (ISBN)
);
-- Tabla de prestamos
CREATE TABLE biblioteca_mix.prestamo
(
    prestamo_ID       varchar(15) PRIMARY KEY NOT NULL,
    ejemplar_prestado varchar(15)             NOT NULL,
    solicitado_por    varchar(15)             NOT NULL,
    fecha_prestamo    DATE                    NOT NULL,
    fecha_entrega     DATE                    NOT NULL,
    -- Llave foraneas.
    FOREIGN KEY (ejemplar_prestado) REFERENCES biblioteca_mix.ejemplar (codigo_ejemplar),
    FOREIGN KEY (solicitado_por) REFERENCES biblioteca_mix.usuarios (personaID)
);
