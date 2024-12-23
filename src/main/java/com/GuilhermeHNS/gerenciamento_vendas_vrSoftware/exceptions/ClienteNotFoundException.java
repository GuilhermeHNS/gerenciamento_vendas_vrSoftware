package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions;

public class ClienteNotFoundException extends RuntimeException{
    public ClienteNotFoundException() {
        super("Cliente não encontrado!");
    }

    public ClienteNotFoundException(String message) {
        super(message);
    }
}
