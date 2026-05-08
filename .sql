-- Tabela de Usuários
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(20) NOT NULL -- Ex: 'ADMIN', 'USER'
);

-- Tabela de Produtos
CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(150) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    tipo_produto VARCHAR(50),
    codigo_barras VARCHAR(50) UNIQUE,
    qr_code TEXT
);

-- Tabela de Movimentação (Substitui o "Controle de Estoque" estático)
CREATE TABLE movimentacoes_estoque (
    id SERIAL PRIMARY KEY,
    produto_id INTEGER REFERENCES produtos(id),
    quantidade INTEGER NOT NULL,
    tipo_movimentacao VARCHAR(10) NOT NULL, -- 'ENTRADA' ou 'SAIDA'
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_validade DATE
);
