package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request;

import java.math.BigDecimal;

public record RegisterUpdateClienteRequest(
        String name,
        String cpfCnpj,
        String valorLimiteCredito,
        String diaFechamentoFatura
) {
}
