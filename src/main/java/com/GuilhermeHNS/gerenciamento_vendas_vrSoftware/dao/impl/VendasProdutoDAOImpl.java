package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasProdutoDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.VendaProdutos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class VendasProdutoDAOImpl implements VendasProdutoDAO{
    @Override
    public void createVendaProduto(List<VendaProdutos> vendaProdutosList, Connection con) throws SQLException {
        String sqlVendaProduto = "\n INSERT INTO venda_produtos (venda_id, produto_id, quantidade, preco_unitario)";
        sqlVendaProduto += "\n VALUES (?,?,?,?)";
        try(PreparedStatement pstmt = con.prepareStatement(sqlVendaProduto)) {
            for (VendaProdutos vendaProdutos : vendaProdutosList) {
                pstmt.setLong(1, vendaProdutos.idVenda());
                pstmt.setLong(2, vendaProdutos.idProduto());
                pstmt.setInt(3, vendaProdutos.quantidade());
                pstmt.setBigDecimal(4, vendaProdutos.preco());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    @Override
    public void deleteProdutoByIdVenda(Long idVenda, Connection con) throws SQLException {
        String sql = "DELETE FROM venda_produtos WHERE venda_id = ?";
        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, idVenda);
            pstmt.executeUpdate();
        }
    }
}
