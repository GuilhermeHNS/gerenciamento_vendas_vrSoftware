package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.configuration.DatabaseConnection;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ProdutoDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProdutoDAOImpl implements ProdutoDAO {
    @Override
    public Optional<Produto> findProdutoById(Long id) throws SQLException {
        String sql = "SELECT";
        sql += "\n         produto_id AS ID,";
        sql += "\n         produto_descricao AS DESC,";
        sql += "\n         produto_preco AS PRECO";
        sql += "\n FROM produto";
        sql += "\n WHERE produto_id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Produto(rs.getLong("ID"), rs.getString("DESC"), rs.getBigDecimal("PRECO")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar a busca do produto!");
        }
    }

    @Override
    public void createProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto(";
        sql += "\n         produto_descricao,";
        sql += "\n         produto_preco";
        sql += "\n )";
        sql += "\n VALUES (?, ?);";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, produto.descricao());
            pstmt.setBigDecimal(2, produto.preco());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar o cadastro do produto!");
        }
    }

    @Override
    public void updateProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produto";
        sql += "\n     SET produto_descricao= ?,";
        sql += "\n         produto_preco= ?";
        sql += "\n WHERE produto_id = ?;";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            try {
                pstmt.setString(1, produto.descricao());
                pstmt.setBigDecimal(2, produto.preco());
                pstmt.setLong(3, produto.codigo());
                pstmt.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar a atualização do cadastro do produto!");
        }
    }

    @Override
    public void deleteProduto(Long id) throws SQLException {
        String sql = "DELETE FROM produto WHERE produto_id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            try {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível deletar o produto!");
        }
    }

    @Override
    public List<Produto> findAll() throws SQLException {
        String sql = "SELECT";
        sql += "\n         produto_id AS ID,";
        sql += "\n         produto_descricao AS DESC,";
        sql += "\n         produto_preco AS PRECO";
        sql += "\n FROM produto;";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            List<Produto> produtoList = new ArrayList<>();
            while (rs.next()) {
                produtoList.add(new Produto(rs.getLong("ID"), rs.getString("DESC"), rs.getBigDecimal("PRECO")));
            }
            return produtoList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar a busca dos produtos!");
        }
    }

    @Override
    public List<Produto> findProdutoByDesc(String desc) throws SQLException {
        String sql = "SELECT";
        sql += "\n         produto_id AS ID,";
        sql += "\n         produto_descricao AS DESC,";
        sql += "\n         produto_preco AS PRECO";
        sql += "\n FROM produto";
        sql += "\n WHERE UPPER(produto_descricao) like ?";

        List<Produto> produtoList = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, "%" + desc.toUpperCase() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    produtoList.add(new Produto(rs.getLong("ID"), rs.getString("DESC"), rs.getBigDecimal("PRECO")));
                }
            }
            return produtoList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Não foi possível efetuar a busca dos produtos!");
        }
    }
}
