package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.response;

import java.math.BigDecimal;

public record HistoricoVendaClienteResponse(
        String nameCliente,
        Long idVenda,
        String dataVenda,
        BigDecimal valorTotalVenda
) {
}
