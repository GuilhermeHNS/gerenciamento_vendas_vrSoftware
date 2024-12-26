package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.configuration.DatabaseConnection;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.response.HistoricoVendaClienteResponse;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.ProdutoVenda;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Venda;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.VendaFilter;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<HistoricoVendaClienteResponse> getHistoricoVendasCliente(VendaFilter vendaFilter) throws SQLException {
        List<HistoricoVendaClienteResponse> responseList = new ArrayList<>();
        String sql = "SELECT";
        sql += "\n c.cliente_name,";
        sql += "\n         v.vendas_id,";
        sql += "\n         v.vendas_dataVendas,";
        sql += "\n         SUM(vp.quantidade * vp.preco_unitario) AS total_venda";
        sql += "\n FROM vendas v";
        sql += "\n INNER JOIN venda_produtos vp ON v.vendas_id = vp.venda_id";
        sql += "\n INNER JOIN cliente c on c.cliente_id = v.vendas_cliente_id";
        sql += "\n WHERE v.vendas_cliente_id = ?";
        if (vendaFilter.dataInicio().isPresent() && vendaFilter.dataFim().isPresent()) {
            sql += "\n AND v.vendas_dataVendas BETWEEN ? AND ?";
        }
        if (vendaFilter.idProduto().isPresent()) {
            sql += "\n AND vp.produto_id = ?";
        }
        sql += "\n GROUP BY v.vendas_id, v.vendas_dataVendas, c.cliente_name";
        sql += "\n ORDER BY v.vendas_dataVendas DESC;";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            Integer countPstmt = 1;
            pstmt.setLong(countPstmt++, vendaFilter.idCliente().get());
            if (vendaFilter.dataInicio().isPresent() && vendaFilter.dataFim().isPresent()) {
                pstmt.setString(countPstmt++, vendaFilter.dataInicio().get());
                pstmt.setString(countPstmt++, vendaFilter.dataFim().get());
            }
            if (vendaFilter.idProduto().isPresent()) {
                pstmt.setLong(countPstmt++, vendaFilter.idProduto().get());
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    responseList.add(new HistoricoVendaClienteResponse(
                            rs.getString("cliente_name"),
                            rs.getLong("venda_id"),
                            rs.getString("vendas_dataVendas"),
                            rs.getBigDecimal("total_vendas"))
                    );
                }
            }
            return responseList;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível consultar os registros de venda!");
        }
    }
}
