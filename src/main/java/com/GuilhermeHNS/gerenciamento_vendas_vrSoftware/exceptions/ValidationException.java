package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.utils.ExibeJPanelError;

public class ValidationException extends RuntimeException{
    public ValidationException() {
        super("Dados inválidos!");
        ExibeJPanelError.exibeError("Dados inválidos");
    }

    public ValidationException(String message) {
        super(message);
        ExibeJPanelError.exibeError(message);
    }

}
