package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterVendaRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.VendasService;

public class VendasController {
    private final VendasService vendasService;
    public VendasController(VendasService vendasService) {
        this.vendasService = vendasService;
    }
    public void registerVenda(RegisterVendaRequest request) {
        vendasService.createVenda(request);
    }
}
