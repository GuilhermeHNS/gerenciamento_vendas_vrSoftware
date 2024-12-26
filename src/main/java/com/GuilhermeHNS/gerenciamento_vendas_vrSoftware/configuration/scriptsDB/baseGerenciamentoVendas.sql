CREATE TABLE cliente (
    cliente_id SERIAL PRIMARY KEY,
    cliente_name VARCHAR(100) NOT NULL,
    cliente_cpfCnpj VARCHAR(14) UNIQUE NOT NULL,
    cliente_limiteCompra NUMERIC(10,2) NOT NULL,
    cliente_diaFechamentoFatura SMALLINT NOT NULL
);

CREATE TABLE produto (
    produto_id SERIAL PRIMARY KEY,
    produto_descricao VARCHAR(255) NOT NULL,
    produto_preco NUMERIC(10,2) NOT NULL
);

CREATE TABLE vendas (
    vendas_cliente_id INT NOT NULL,
    vendas_produto_id INT NOT NULL,
    vendas_dataVendas TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    vendas_quantidade INT NOT NULL,
    vendas_precoUnitario NUMERIC(10,2) NOT NULL,
    CONSTRAINT pk_vendas PRIMARY KEY (vendas_cliente_id, vendas_produto_id, vendas_dataVendas),
    CONSTRAINT fk_cliente FOREIGN KEY (vendas_cliente_id) REFERENCES cliente(cliente_id),
    CONSTRAINT fk_produto FOREIGN KEY (vendas_produto_id) REFERENCES produto(produto_id)
);

