package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProdutoDAO {
    Optional<Produto> findProdutoById(Long id) throws SQLException;

    void createProduto(Produto produto) throws SQLException;

    void updateProduto(Produto produto) throws SQLException;

    void deleteProduto(Long id) throws SQLException;

    List<Produto> findAll() throws SQLException;

    List<Produto> findProdutoByDesc(String desc) throws SQLException;

    void inativaProduto(Long id) throws SQLException;

    Optional<Produto> findProdutoAtivoById(Long id) throws SQLException;
}
