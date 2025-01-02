package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request;

public record UpdateProdutoRequest(
        Long codigo,
        String descricao,
        String preco,
        Boolean ativo
) {
}
