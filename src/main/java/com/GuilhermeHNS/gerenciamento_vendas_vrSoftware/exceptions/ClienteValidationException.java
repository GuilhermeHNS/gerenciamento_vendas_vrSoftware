package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions;

import javax.swing.*;

public class ClienteValidationException extends RuntimeException{
    public ClienteValidationException() {
        super("Dados inválidos!");
    }

    public ClienteValidationException(String message) {
        super(message);
    }
}
