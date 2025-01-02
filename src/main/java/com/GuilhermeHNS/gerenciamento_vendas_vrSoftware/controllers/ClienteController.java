package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterUpdateClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;

import java.util.List;

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

    public Cliente consultaClienteAtivo(String cpfCnpj) {
        return clienteService.getClienteAtivoByDoc(cpfCnpj);
    }

    public List<Cliente> listaTodosOsClientes() {
        return clienteService.getAllClientes();
    }

    public void atualizaCliente(RegisterUpdateClienteRequest request) {
        clienteService.updateCliente(request);
    }

    public void deleteCliente(String cpfCnpj) {
        clienteService.deleteCliente(cpfCnpj);
    }

    public void inativarCliente(String cpfCnpj) {
        clienteService.inativarCliente(cpfCnpj);
    }

}
