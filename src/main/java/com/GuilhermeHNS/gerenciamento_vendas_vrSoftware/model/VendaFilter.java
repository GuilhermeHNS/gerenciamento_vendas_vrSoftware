package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model;

import java.time.LocalDate;
import java.util.Optional;

public record VendaFilter(
        Optional<Long> idCliente,
        Optional<String>dataInicio ,
        Optional<String> dataFim ,
        Optional<Long> idProduto
) {
    public VendaFilter {
        idCliente = Optional.empty();
        dataInicio = Optional.empty();
        dataFim = Optional.empty();
        idProduto = Optional.empty();
    }
}
