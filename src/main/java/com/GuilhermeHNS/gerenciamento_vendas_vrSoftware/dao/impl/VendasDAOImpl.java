package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.configuration.DatabaseConnection;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.ProdutoVenda;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Venda;

import java.math.BigDecimal;
import java.sql.*;

public class VendasDAOImpl implements VendasDAO {
    @Override
    public BigDecimal getValorDisponivel(Long idCliente, String dataFechamento, String dataFinal) throws SQLException {
        String sql = "SELECT";
        sql += "\n COALESCE(SUM(vp.quantidade * vp.preco_unitario), 0) AS total_vendas";
        sql += "\n FROM vendas v";
        sql += "\n JOIN venda_produtos vp ON v.venda_id = vp.venda_id";
        sql += "\n WHERE v.cliente_id = ?";
        sql += "\n AND v.data_venda BETWEEN ? AND ?;";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, idCliente);
            pstmt.setString(2, dataFechamento);
            pstmt.setString(3, dataFinal);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_vendas");
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
        String sqlVenda = "\n INSERT INTO vendas (cliente_id)";
        sqlVenda += "\n VALUES (?);";

        String sqlVendaProduto = "\n INSERT INTO venda_produtos (venda_id, produto_id, quantidade, preco_unitario)";
        sqlVendaProduto += "\n VALUES (?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtProduto = con.prepareStatement(sqlVendaProduto)) {
            con.setAutoCommit(false);
            Long idVenda = 0L;
            try {
                pstmt.setLong(1, venda.idCliente());
                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new Exception("ID da venda não foi gerado!");
                    }
                    idVenda = rs.getLong(1);
                }
                for (ProdutoVenda produtoVenda : venda.produtoVendaList()) {
                    pstmtProduto.setLong(1, idVenda);
                    pstmtProduto.setLong(2, produtoVenda.codigo());
                    pstmtProduto.setInt(3, produtoVenda.quantidade());
                    pstmtProduto.setBigDecimal(4, produtoVenda.preco());
                    pstmtProduto.addBatch();
                }
                pstmtProduto.executeBatch();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar a venda!");
        }
    }
}
