CREATE TABLE comandas(
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    status VARCHAR(50),
    abertura TIMESTAMP,
    fechamento TIMESTAMP,
    mesa_id BIGINT,
    usuario_id BIGINT,

    CONSTRAINT fk_comandas_mesas
                     FOREIGN KEY (mesa_id)
                     REFERENCES mesas(id),

    CONSTRAINT fk_comandas_usuarios
                     FOREIGN KEY (usuario_id)
                     REFERENCES usuarios(id)
);