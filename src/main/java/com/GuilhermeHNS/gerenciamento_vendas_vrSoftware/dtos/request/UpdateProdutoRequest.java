package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request;

public record UpdateProdutoRequest(
        String codigo,
        String descricao,
        String preco
) {
}
