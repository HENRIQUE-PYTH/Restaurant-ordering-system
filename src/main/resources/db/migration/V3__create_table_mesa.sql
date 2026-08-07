CREATE TABLE mesas(
    id BIGSERIAL PRIMARY KEY,
    numero_mesa INTEGER UNIQUE NOT NULL,
    status VARCHAR(50),
    qr_code_token VARCHAR(100) UNIQUE
);