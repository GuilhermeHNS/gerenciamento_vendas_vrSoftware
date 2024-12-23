package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions;

import javax.swing.*;

public class ClienteAlreadyExistException extends RuntimeException{
    public ClienteAlreadyExistException() {
        super("Cliente já existente!");
    }

    public ClienteAlreadyExistException(String message) {
        super(message);
    }
}
