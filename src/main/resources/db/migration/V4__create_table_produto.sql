CREATE TABLE produtos(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    preco NUMERIC(10,2) NOT NULL,
    ativo BOOLEAN NOT NULL,
    categoria_id BIGINT,

    CONSTRAINT fk_produtos_categoria
                     FOREIGN KEY (categoria_id)
                     REFERENCES categorias(id)

);