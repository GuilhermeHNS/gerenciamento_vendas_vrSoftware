package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.ClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;

public interface ClienteService {
    void createCliente(RegisterClienteRequest request);

    Cliente getClienteByDoc(ClienteRequest request);
}
