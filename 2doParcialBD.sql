CREATE TABLE Resto (
    idResto SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    domicilio VARCHAR(100),
    localidad VARCHAR(50)
);

-- Tabla para Mesa
CREATE TABLE Mesa (
    id SERIAL PRIMARY KEY,
    nro_mesa SERIAL,
    capacidad INT,
    consumo INT,
    estado VARCHAR(50),  
    idResto INT,
    FOREIGN KEY (idResto) REFERENCES Resto(idResto)
);

-- Tabla para Reserva
CREATE TABLE Reserva (
    id SERIAL PRIMARY KEY,
    fecha DATE,
    nombreApellido VARCHAR(50),
    comensales INT,
    id_mesa INT,
    FOREIGN KEY (id_mesa) REFERENCES Mesa(id)
);