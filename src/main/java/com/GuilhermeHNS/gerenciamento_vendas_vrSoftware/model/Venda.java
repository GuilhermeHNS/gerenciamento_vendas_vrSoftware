package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model;

import java.util.List;

public record Venda(
        Long id,
        Long idCliente,
        String dataVenda
) {
}
