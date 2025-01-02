package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.configuration.DatabaseConnection;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ClienteDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAOImpl implements ClienteDAO {
    @Override
    public void createCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO CLIENTE(";
        sql += "\n        cliente_name,";
        sql += "\n        cliente_cpfcnpj,";
        sql += "\n        cliente_limitecompra,";
        sql += "\n        cliente_diafechamentofatura,";
        sql += "\n        cliente_ativo";
        sql += "\n) values (?,?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, cliente.nome());
            pstmt.setString(2, cliente.cpfCnpj());
            pstmt.setBigDecimal(3, cliente.limiteCredito());
            pstmt.setInt(4, cliente.diaFechamentoFatura());
            pstmt.setBoolean(5, cliente.ativo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar o cadastro do cliente!");
        }
    }

    @Override
    public Optional<Cliente> getClienteByCpfCnpj(String cpfCnpj) throws SQLException {

        String sql = "SELECT";
        sql += "\n         cliente_id AS CODIGO,";
        sql += "\n         cliente_name AS NAME,";
        sql += "\n         cliente_cpfcnpj AS CPFCNPJ,";
        sql += "\n         cliente_limitecompra AS LIMITECREDITO,";
        sql += "\n         cliente_diafechamentofatura AS DIAFECHAMENTOFATURA,";
        sql += "\n         cliente_ativo AS ATIVO";
        sql += "\n FROM cliente";
        sql += "\n WHERE cliente_cpfcnpj = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, cpfCnpj);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Cliente(
                        rs.getLong("CODIGO"),
                        rs.getString("NAME"),
                        rs.getString("CPFCNPJ"),
                        rs.getBigDecimal("LIMITECREDITO"),
                        rs.getInt("DIAFECHAMENTOFATURA"),
                        rs.getBoolean("ATIVO"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível buscar o cliente informado!");
        }

    }

    @Override
    public void updateCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente";
        sql += "\n     SET cliente_name=?,";
        sql += "\n         cliente_limitecompra=?,";
        sql += "\n         cliente_diafechamentofatura=?,";
        sql += "\n         cliente_ativo = ?";
        sql += "\n WHERE cliente_cpfcnpj = ?";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            try {
                pstmt.setString(1, cliente.nome());
                pstmt.setBigDecimal(2, cliente.limiteCredito());
                pstmt.setInt(3, cliente.diaFechamentoFatura());
                pstmt.setBoolean(4, cliente.ativo());
                pstmt.setString(5, cliente.cpfCnpj());
                pstmt.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar a atualização do cadastro do cliente!");
        }
    }

    @Override
    public void deleteCliente(String cpfCnpj) throws SQLException {
        String sql = "DELETE FROM cliente WHERE cliente_cpfcnpj = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            try {
                pstmt.setString(1, cpfCnpj);
                pstmt.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível deletar o cadastro do cliente!");
        }
    }

    @Override
    public List<Cliente> getAllClientes() throws SQLException {
        String sql = "SELECT";
        sql += "\n         cliente_id AS CODIGO,";
        sql += "\n         cliente_name AS NAME,";
        sql += "\n         cliente_cpfcnpj AS CPFCNPJ,";
        sql += "\n         cliente_limitecompra AS LIMITECREDITO,";
        sql += "\n         cliente_diafechamentofatura AS DIAFECHAMENTOFATURA,";
        sql += "\n         cliente_ativo as ATIVO";
        sql += "\n FROM cliente";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            List<Cliente> clientesList = new ArrayList<>();
            while (rs.next()) {
                clientesList.add(
                        new Cliente(rs.getLong("CODIGO"),
                                rs.getString("NAME"),
                                rs.getString("CPFCNPJ"),
                                rs.getBigDecimal("LIMITECREDITO"),
                                rs.getInt("DIAFECHAMENTOFATURA"),
                                rs.getBoolean("ATIVO"))
                );
            }
            return clientesList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível buscar os clientes cadastrados!");
        }
    }

    @Override
    public void inativaCliente(String cpfCnpj) throws SQLException {
        String sql = "UPDATE cliente";
        sql += "\n SET cliente_ativo = FALSE";
        sql += "\n WHERE cliente_cpfCnpj = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            try {
                pstmt.setString(1, cpfCnpj);
                pstmt.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível deletar o cadastro do cliente!");
        }
    }

    @Override
    public Optional<Cliente> getClienteAtivoByCpfCnpj(String cpfCnpj) throws SQLException {
        String sql = "SELECT";
        sql += "\n         cliente_id AS CODIGO,";
        sql += "\n         cliente_name AS NAME,";
        sql += "\n         cliente_cpfcnpj AS CPFCNPJ,";
        sql += "\n         cliente_limitecompra AS LIMITECREDITO,";
        sql += "\n         cliente_diafechamentofatura AS DIAFECHAMENTOFATURA,";
        sql += "\n         cliente_ativo AS ATIVO";
        sql += "\n FROM cliente";
        sql += "\n WHERE cliente_cpfcnpj = ?";
        sql += "\n AND cliente_ativo = TRUE";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, cpfCnpj);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Cliente(
                        rs.getLong("CODIGO"),
                        rs.getString("NAME"),
                        rs.getString("CPFCNPJ"),
                        rs.getBigDecimal("LIMITECREDITO"),
                        rs.getInt("DIAFECHAMENTOFATURA"),
                        rs.getBoolean("ATIVO"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível buscar o cliente informado!");
        }
    }
}
