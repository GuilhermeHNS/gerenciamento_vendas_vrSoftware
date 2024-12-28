package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.VendaProdutos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface VendasProdutoDAO {
    void createVendaProduto(List<VendaProdutos> vendaProdutosList, Connection con) throws SQLException;
    void deleteProdutoByIdVenda(Long idVenda, Connection con) throws SQLException;
}
