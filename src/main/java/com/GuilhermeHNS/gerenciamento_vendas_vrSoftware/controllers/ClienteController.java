package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.ClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;

public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void registerCliente(RegisterClienteRequest request) {
        clienteService.createCliente(request);
    }

    public Cliente consultaCliente(ClienteRequest request) {
        return clienteService.getClienteByDoc(request);
    }
}
