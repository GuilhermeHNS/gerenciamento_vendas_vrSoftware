package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.ConsultaVendaRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterVendaRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.response.HistoricoVendaClienteResponse;

import java.util.List;

public interface VendasService {
    void createVenda(RegisterVendaRequest request);

    List<HistoricoVendaClienteResponse> getHistoricoVendasCliente(ConsultaVendaRequest request);
}
