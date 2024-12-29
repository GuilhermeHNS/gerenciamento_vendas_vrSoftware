package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.configuration.DatabaseConnection;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.response.HistoricoVendaClienteResponse;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.VendaProdutos;
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
        sql += "\n JOIN venda_produtos vp ON v.vendas_id = vp.venda_id";
        sql += "\n WHERE v.vendas_cliente_id = ?";
        sql += "\n AND v.vendas_datavendas BETWEEN ? AND ?;";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, idCliente);
            pstmt.setTimestamp(2, Timestamp.valueOf(dataFechamento + ":00"));
            pstmt.setTimestamp(3, Timestamp.valueOf(dataFinal));
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
    public Long createVenda(Venda venda, Connection con) throws SQLException {
        String sqlVenda = "\n INSERT INTO vendas (vendas_cliente_id)";
        sqlVenda += "\n VALUES (?);";

        try (PreparedStatement pstmt = con.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
            try {
                pstmt.setLong(1, venda.idCliente());
                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new Exception("ID da venda não foi gerado!");
                    }
                    return rs.getLong(1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Não foi possível efetuar a venda!");
            }
        }
    }

    @Override
    public List<HistoricoVendaClienteResponse> getHistoricoVendasCliente(VendaFilter vendaFilter) throws SQLException {
        List<HistoricoVendaClienteResponse> responseList = new ArrayList<>();
        String sql = "select distinct";
        sql += "\n v.vendas_id,";
        sql += "\n         c.cliente_name,";
        sql += "\n         TO_CHAR(v.vendas_datavendas, 'DD/MM/YYYY HH24:MI:SS') as vendas_datavendas,";
        sql += "\n         sum(vp.preco_unitario * vp.quantidade) as total_vendas";
        sql += "\n from vendas v";
        sql += "\n inner join venda_produtos vp on vp.venda_id = v.vendas_id";
        sql += "\n inner join cliente c on c.cliente_id = v.vendas_cliente_id";
        if (vendaFilter.getIdProduto().isPresent()) {
            sql += "\n LEFT JOIN venda_produtos vp2 ON vp2.venda_id = v.vendas_id";
        }
        sql += "\n where v.vendas_datavendas BETWEEN ?::timestamp AND ?::timestamp";
        if (vendaFilter.getIdCliente().isPresent()) {
            sql += "\n AND c.cliente_id = ?";
        }
        if (vendaFilter.getIdProduto().isPresent()) {
            sql += "\n AND vp2.produto_id = ?";
        }
        sql += "\n group by v.vendas_id, c.cliente_name";
        sql += "\n order by v.vendas_id desc;";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            int countPstmt = 1;
            pstmt.setString(countPstmt++, vendaFilter.getDataInicio());
            pstmt.setString(countPstmt++, vendaFilter.getDataFim());

            if(vendaFilter.getIdCliente().isPresent()){
                pstmt.setLong(countPstmt++, vendaFilter.getIdCliente().get());
            }
            if (vendaFilter.getIdProduto().isPresent()) {
                pstmt.setLong(countPstmt, vendaFilter.getIdProduto().get());
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível consultar os registros de venda!");
        }
    }

    @Override
    public void deleteVenda(Long id, Connection con) throws SQLException {
        String sql = "DELETE FROM vendas WHERE vendas_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }
}
