package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.configuration.DatabaseConnection;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ClienteDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClienteDAOImpl implements ClienteDAO {
    @Override
    public void createCliente(Cliente cliente) {
        String sql = "INSERT INTO CLIENTE(";
        sql += "\n        cliente_name,";
        sql += "\n        cliente_cpfcnpj,";
        sql += "\n        cliente_limitecompra,";
        sql += "\n        cliente_diafechamentofatura";
        sql += "\n) values (?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, cliente.nome());
            pstmt.setString(2, cliente.cpfCnpj());
            pstmt.setBigDecimal(3, cliente.limiteCredito());
            pstmt.setInt(4, cliente.diaFechamentoFatura());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred while save cliente.");
        }
    }

    @Override
    public Optional<Cliente> getClienteByCpfCnpj(String cpfCnpj) {

        String sql ="SELECT";
        sql +="\n         cliente_id AS CODIGO,";
        sql +="\n         cliente_name AS NAME,";
        sql +="\n         cliente_cpfcnpj AS CPFCPNJ,";
        sql +="\n         cliente_limitecompra AS LIMITECREDITO,";
        sql +="\n         cliente_diafechamentofatura AS DIAFECHAMENTOFATURA";
        sql +="\n FROM cliente";
        sql +="\n WHERE CPFCNPJ = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, cpfCnpj);
            try(ResultSet rs = pstmt.executeQuery()) {
                if(!rs.next()){
                    return Optional.empty();
                }
                return Optional.of(new Cliente(
                        rs.getLong("CODIGO"),
                        rs.getString("NAME"),
                        rs.getString("CPFCNPJ"),
                        rs.getBigDecimal("LIMITECREDITO"),
                        rs.getInt("DIAFECHAMENTOFATURA"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred while searching for the cliente.");
        }

    }
}
