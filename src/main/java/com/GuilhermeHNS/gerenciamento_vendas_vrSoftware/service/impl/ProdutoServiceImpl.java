package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ProdutoDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.UpdateProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ProdutoNotFoundException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ValidationException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ProdutoService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.utils.ExibeJPanelError.exibeError;

public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoDAO produtoDAO;

    public ProdutoServiceImpl(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    @Override
    public Produto getProdutoById(String codigo) {
        try {
            Long idProduto = Long.getLong(codigo);
            Optional<Produto> produto = produtoDAO.findProdutoById(idProduto);
            return produto.orElseThrow(() -> {
                exibeError("Produto não encontrado!");
                return new ProdutoNotFoundException();
            });
        } catch (SQLException e) {
            exibeError("Error: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void createProduto(RegisterProdutoRequest request) {
        verificaDadosParaPersistenciaDeProduto(request.descricao(), request.preco());
        try {
            Produto produto = new Produto(-1L, request.descricao(), new BigDecimal(request.preco()));
            produtoDAO.createProduto(produto);
        } catch (SQLException e) {
            exibeError("Error: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateProduto(UpdateProdutoRequest request) {
        verificaDadosParaPersistenciaDeProduto(request.descricao(), request.preco());
        try {
            Produto produto = new Produto(
                    Long.getLong(request.codigo()),
                    request.descricao(),
                    new BigDecimal(request.preco())
            );
            produtoDAO.updateProduto(produto);
        } catch (SQLException e) {
            exibeError("Error: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteProduto(String codigo) {
        try {
            produtoDAO.deleteProduto(Long.getLong(codigo));
        } catch (SQLException e) {
            exibeError("Error: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Produto> getAllProdutos() {
        try {
            return produtoDAO.findAll();
        } catch (SQLException e) {
            exibeError("Error: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    private void verificaDadosParaPersistenciaDeProduto(String descricao, String preco) {
        if (descricao == null || descricao.isBlank()) {
            throw new ValidationException("A descrição do produto é obrigatória!");
        }
        if (preco == null || preco.isBlank() || new BigDecimal(preco).compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("O preço do produto não pode ser menor que 0!");
        }
    }

}
