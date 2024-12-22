package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterUpdateClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;

import java.util.List;

public interface ClienteService {
    void createCliente(RegisterUpdateClienteRequest request);
    Cliente getClienteByDoc(String cpfCnpj);
    void updateCliente(RegisterUpdateClienteRequest request);
    void deleteCliente(String cpfCnpj);
    List<Cliente> getAllClientes();
}
