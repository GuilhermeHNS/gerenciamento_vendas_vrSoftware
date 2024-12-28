package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model;

import java.math.BigDecimal;

public record VendaProdutos(
        Long idVenda,
        Long idProduto,
        Integer quantidade,
        BigDecimal preco
) {
}
