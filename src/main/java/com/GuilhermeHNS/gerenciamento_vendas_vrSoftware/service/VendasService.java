package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterVendaRequest;

public interface VendasService {
    void createVenda(RegisterVendaRequest request);
}
