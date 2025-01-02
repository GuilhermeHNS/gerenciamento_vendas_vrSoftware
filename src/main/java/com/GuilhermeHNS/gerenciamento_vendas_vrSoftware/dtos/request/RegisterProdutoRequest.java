package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request;

public record RegisterProdutoRequest(
        String descricao,
        String preco,
        Boolean ativo
) {
}
