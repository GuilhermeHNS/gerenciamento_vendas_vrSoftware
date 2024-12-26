package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.VendaProdutoDTO;

import java.util.List;

public record RegisterVendaRequest(
        String cpfCnpjCliente,
        List<VendaProdutoDTO> vendaProdutoList
) {
}
