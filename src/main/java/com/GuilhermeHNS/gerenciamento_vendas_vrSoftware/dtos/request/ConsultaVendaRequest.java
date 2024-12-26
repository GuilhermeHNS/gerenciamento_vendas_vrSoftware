package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request;

import java.util.Optional;

public record ConsultaVendaRequest(
        Optional<String> cpfCnpj,
        Optional<String> dataInicio,
        Optional<String> dataFim,
        Optional<String> idProduto
) {
    public ConsultaVendaRequest{
        cpfCnpj = Optional.empty();
        dataInicio = Optional.empty();
        dataFim = Optional.empty();
        idProduto = Optional.empty();
    }
}
