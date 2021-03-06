USE RECETARIO;

#baja las tablas
DROP TABLE ESPERO;
DROP TABLE AGREGOING;
DROP TABLE CALIENTO;
DROP TABLE PASO;
DROP TABLE RECETA;
DROP TABLE INGREDIENTE;
DROP TABLE UNIDAD;


CREATE TABLE UNIDAD(
	NOMBREUNIDAD VARCHAR(20) NOT NULL,
    EQUI_GRAMOS INT NOT NULL,
    CONSTRAINT RESTR_UNIDAD PRIMARY KEY(NOMBREUNIDAD)

);
CREATE TABLE INGREDIENTE(
	NOMBREINGR VARCHAR(20) NOT NULL,
	COSTOPESOS INT NOT NULL,
    CALORIAS_CADA_CIENGR FLOAT(5) NOT NULL,
    PROTEINAS_CADA_CIENGR FLOAT(5) NOT NULL,
    GRASAS_CADA_CIENGR FLOAT(5) NOT NULL,
    
    CONSTRAINT RESTR_NOMBREINGR PRIMARY KEY(NOMBREINGR)
);
CREATE TABLE RECETA(
    NOMBREREC VARCHAR(20) NOT NULL,
    TIPORECETA INT NOT NULL, -- 0:POSTRE 1:CARNE 2:PASTA 3:ENSALADA 4:PLATO_PRINCIPAL
	DESCRIPCION VARCHAR(255) NOT NULL,
	COMENTARIO VARCHAR(255) NOT NULL,
    
	CONSTRAINT RESTR_RECETA PRIMARY KEY(NOMBREREC)
);
CREATE TABLE PASO(
	NOMBREREC VARCHAR(20) NOT NULL,
	NROPASO INT NOT NULL,
    TIPOPASO VARCHAR(10) NOT NULL,
   	CONSTRAINT RESTR_PASO PRIMARY KEY(NOMBREREC, NROPASO),
	CONSTRAINT EXT_NOMBREREC FOREIGN KEY(NOMBREREC) REFERENCES RECETA(NOMBREREC)
);

CREATE TABLE ESPERO(
	NOMBREREC VARCHAR(20) NOT NULL,
	NROPASO INT NOT NULL,
    HORA INT NOT NULL,
    MIN INT NOT NULL,
    SEG INT NOT NULL,

	CONSTRAINT EXT_ESPERO FOREIGN KEY(NOMBREREC,NROPASO) REFERENCES PASO(NOMBREREC,NROPASO),
   	CONSTRAINT RESTR_ESPERO PRIMARY KEY(NOMBREREC, NROPASO)
);

CREATE TABLE CALIENTO (
	NOMBREREC VARCHAR(20) NOT NULL,
	NROPASO INT NOT NULL,
    HORA INT NOT NULL,
    MIN INT NOT NULL,
    SEG INT NOT NULL,
    GRADO INT NOT NULL,
    
	CONSTRAINT EXT_CALIENTO FOREIGN KEY(NOMBREREC, NROPASO) REFERENCES PASO(NOMBREREC, NROPASO),
    CONSTRAINT RESTR_CALIENTO PRIMARY KEY(NOMBREREC, NROPASO)
);

CREATE TABLE AGREGOING (
    NOMBREREC VARCHAR(20) NOT NULL,
	NROPASO INT NOT NULL,
    HORA INT NOT NULL,
    MIN INT NOT NULL,
    SEG INT NOT NULL,
	NOMBREINGR VARCHAR(20) NOT NULL,
    CANTIDAD INT NOT NULL,
    NOMBREUNIDAD CHAR(10) NOT NULL,
    CONSTRAINT RESTR_AGREGOING PRIMARY KEY(NOMBREREC, NROPASO),
	CONSTRAINT EXT_AGREGOING FOREIGN KEY(NOMBREREC, NROPASO) REFERENCES PASO(NOMBREREC, NROPASO),
    CONSTRAINT EXT_NOMBREING FOREIGN KEY(NOMBREINGR) REFERENCES INGREDIENTE(NOMBREINGR),
    CONSTRAINT EXT_UNIDAD FOREIGN KEY(NOMBREUNIDAD) REFERENCES UNIDAD(NOMBREUNIDAD)
);
