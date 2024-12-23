package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions;

public class ProdutoNotFoundException extends RuntimeException{
    public ProdutoNotFoundException() {
        super("Produto não encontrado!");
    }

    public ProdutoNotFoundException(String message) {
        super(message);
    }
}
