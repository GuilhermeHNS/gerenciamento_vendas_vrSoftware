package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model;

import java.math.BigDecimal;

public record ProdutoVenda(
        Long codigo,
        Integer quantidade,
        BigDecimal preco
) {
}
