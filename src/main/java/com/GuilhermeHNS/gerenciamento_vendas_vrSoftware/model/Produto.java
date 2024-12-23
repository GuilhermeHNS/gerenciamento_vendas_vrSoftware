package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model;

import java.math.BigDecimal;

public record Produto(
        Long codigo,
        String descricao,
        BigDecimal preco
) {
}
