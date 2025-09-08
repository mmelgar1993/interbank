CREATE TABLE IF NOT EXISTS producto (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255),
    precio DECIMAL(10,2),
    stock INT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pedido (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha DATE,
  total DOUBLE,
  estado VARCHAR(255),
  PRIMARY KEY (id)
);

