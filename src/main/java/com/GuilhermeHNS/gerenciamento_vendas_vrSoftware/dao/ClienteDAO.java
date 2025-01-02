package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ClienteDAO {
    void createCliente(Cliente cliente) throws SQLException;

    Optional<Cliente> getClienteByCpfCnpj(String cpfCnpj) throws SQLException;

    void updateCliente(Cliente cliente) throws SQLException;

    void deleteCliente(String cpfCnpj) throws SQLException;

    List<Cliente> getAllClientes() throws SQLException;

    void inativaCliente(String cpfCnpj) throws SQLException;

    Optional<Cliente> getClienteAtivoByCpfCnpj(String cpfCnpj) throws SQLException;
}
