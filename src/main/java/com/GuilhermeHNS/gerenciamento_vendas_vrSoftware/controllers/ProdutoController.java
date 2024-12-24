package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.UpdateProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ProdutoService;

import java.util.List;

public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public List<Produto> listarTodosOsProdutos() {
        return produtoService.getAllProdutos();
    }

    public void salvarProduto(RegisterProdutoRequest request) {
        produtoService.createProduto(request);
    }

    public Produto consultaProduto(String codigo) {
        return produtoService.getProdutoById(codigo);
    }

    public void atualizaProduto(UpdateProdutoRequest request) {
        produtoService.updateProduto(request);
    }

    public void deletarProduto(Long codigo) {
        produtoService.deleteProduto(codigo);
    }

    public List<Produto> listarProdutoPorDescricao(String desc) {
        return produtoService.getProdutoByDescricao(desc);
    }
}
