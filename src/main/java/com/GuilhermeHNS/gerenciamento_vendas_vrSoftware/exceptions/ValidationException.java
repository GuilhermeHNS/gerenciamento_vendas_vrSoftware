package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions;

import static com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.utils.Utils.exibeJPanel;

public class ValidationException extends RuntimeException{
    public ValidationException() {
        super("Dados inválidos!");
        exibeJPanel("Dados inválidos");
    }

    public ValidationException(String message) {
        super(message);
        exibeJPanel(message);
    }

}
