package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.VendaProdutoDTO;

import java.util.List;

public record UpdateVendaRequest(
        String idVenda,
        String cpfCnpj,
        String dataCompra,
        List<VendaProdutoDTO> vendaProdutoList
) {
}
