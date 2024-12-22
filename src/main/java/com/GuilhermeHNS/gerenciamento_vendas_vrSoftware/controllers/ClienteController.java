package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterUpdateClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;

public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void registerCliente(RegisterUpdateClienteRequest request) {
        clienteService.createCliente(request);
    }

    public Cliente consultaCliente(String cpfCnpj) {
        return clienteService.getClienteByDoc(cpfCnpj);
    }

    public void atualizaCliente(RegisterUpdateClienteRequest request) {
        clienteService.updateCliente(request);
    }

    public void deleteCliente(String cpfCnpj) {
        clienteService.deleteCliente(cpfCnpj);
    }
}
