CREATE TABLE pedidos(
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    comanda_id BIGINT,
    horario TIMESTAMP,
    status_pedido VARCHAR(50),

    CONSTRAINT fk_pedidos_comanda
                    FOREIGN KEY (comanda_id)
                    REFERENCES comandas(id)

);