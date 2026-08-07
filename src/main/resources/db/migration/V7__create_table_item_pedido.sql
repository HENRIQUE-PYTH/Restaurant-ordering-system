CREATE TABLE item_pedido(
    id BIGSERIAL PRIMARY KEY,
    quantidade INTEGER NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,
    observacao VARCHAR(250),
    produto_id BIGINT,
    pedido_id BIGINT,

    CONSTRAINT fk_item_pedido_produto
                        FOREIGN KEY (produto_id)
                        REFERENCES produtos(id),

    CONSTRAINT fk_item_pedido_pedido
                        FOREIGN KEY (pedido_id)
                        REFERENCES pedidos(id)
);