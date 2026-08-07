CREATE TABLE atendimentos (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    horario TIMESTAMP,
    status VARCHAR(50),
    tipo_atendimento VARCHAR(50),
    usuario_id BIGINT,
    mesa_id BIGINT,

    CONSTRAINT fk_atendimentos_usuario
                          FOREIGN KEY (usuario_id)
                          REFERENCES usuarios(id),
    CONSTRAINT fk_atendimentos_mesas
                          FOREIGN KEY (mesa_id)
                          REFERENCES mesas(id)
);