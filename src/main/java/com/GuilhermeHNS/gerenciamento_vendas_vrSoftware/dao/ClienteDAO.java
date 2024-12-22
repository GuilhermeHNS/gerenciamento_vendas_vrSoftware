package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;

import java.sql.SQLException;
import java.util.Optional;

public interface ClienteDAO {
    void createCliente(Cliente cliente) throws SQLException;

    Optional<Cliente> getClienteByCpfCnpj(String cpfCnpj) throws SQLException;

}
