package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.configuration.DatabaseConnection;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.ProdutoVenda;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Venda;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendasDAOImpl implements VendasDAO {
    @Override
    public BigDecimal getValorDisponivel(Long idCliente, String dataFechamento) throws SQLException {
        String sql = "SELECT";
        sql += "\n COALESCE(SUM(vendas_quantidade * vendas_precoUnitario), 0) AS total_venda";
        sql += "\n FROM vendas";
        sql += "\n WHERE venda_cliente_id = ?";
        sql += "\n AND vendas_dataVendas BETWEEN ?::TIMESTAMP AND CURRENT_TIMESTAMP";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, idCliente);
            pstmt.setString(2, dataFechamento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_venda");
                }
                return new BigDecimal(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar a busca do valor gasto pelo cliente!");
        }
    }

    @Override
    public void createVenda(Venda venda) throws SQLException {
        String sql = "INSERT INTO vendas(";
        sql += "\n         vendas_cliente_id, vendas_produto_id, vendas_quantidade, vendas_precounitario)";
        sql += "\n VALUES (?, ?, ?, ?, ?);";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (ProdutoVenda produtoVenda: venda.produtoVendaList()) {
                pstmt.setLong(1, venda.idCliente());
                pstmt.setLong(2, produtoVenda.codigo());
                pstmt.setInt(3, produtoVenda.quantidade());
                pstmt.setBigDecimal(4, produtoVenda.preco());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar o registro da venda!");
        }
    }
}
