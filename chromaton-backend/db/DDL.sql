CREATE TABLE usuarios (
id_usuario SERIAL UNIQUE NOT NULL,
nombre VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
password VARCHAR(150) NOT NULL
);

SELECT * FROM usuarios;

DROP TABLE usuarios;