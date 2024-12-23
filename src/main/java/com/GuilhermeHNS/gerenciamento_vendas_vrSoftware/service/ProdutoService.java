package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.UpdateProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;

import java.util.List;

public interface ProdutoService {
    Produto getProdutoById(String codigo);

    void createProduto(RegisterProdutoRequest request);

    void updateProduto(UpdateProdutoRequest request);

    void deleteProduto(String codigo);

    List<Produto> getAllProdutos();

}
