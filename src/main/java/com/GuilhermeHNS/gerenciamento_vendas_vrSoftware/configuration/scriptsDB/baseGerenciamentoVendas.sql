CREATE TABLE cliente (
    cliente_id SERIAL PRIMARY KEY,
    cliente_name VARCHAR(100) NOT NULL,
    cliente_cpfCnpj VARCHAR(14) UNIQUE NOT NULL,
    cliente_limiteCompra NUMERIC(10,2) NOT NULL,
    cliente_diaFechamentoFatura SMALLINT NOT NULL,
    cliente_ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE produto (
    produto_id SERIAL PRIMARY KEY,
    produto_descricao VARCHAR(255) NOT NULL,
    produto_preco NUMERIC(10,2) NOT NULL,
    produto_ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE vendas (
    vendas_id SERIAL PRIMARY KEY,
    vendas_cliente_id INT NOT NULL,
    vendas_dataVendas TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente FOREIGN KEY (vendas_cliente_id) REFERENCES cliente(cliente_id)
);

CREATE TABLE venda_produtos (
    venda_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,
    PRIMARY KEY (venda_id, produto_id),
    FOREIGN KEY (venda_id) REFERENCES vendas(vendas_id),
    FOREIGN KEY (produto_id) REFERENCES produto(produto_id)
);