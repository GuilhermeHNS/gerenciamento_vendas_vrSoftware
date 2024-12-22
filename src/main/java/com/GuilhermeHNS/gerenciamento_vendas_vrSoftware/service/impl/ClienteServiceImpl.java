package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ClienteDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.ClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {

    private ClienteDAO clienteDAO;

    public ClienteServiceImpl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void createCliente(RegisterClienteRequest request) {
        try{
            verificaDadosParaPersistenciaDeCliente(request);
            Optional<Cliente> existingCliente = clienteDAO.getClienteByCpfCnpj(request.cpfCnpj());
            if(existingCliente.isPresent()) {
                throw new RuntimeException("Cliente already exist!");
            }
            Cliente cliente = new Cliente(-1L, request.name(), request.cpfCnpj(), request.valorLimiteCredito(), request.diaFechamentoFatura());
            clienteDAO.createCliente(cliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error: " + e.getMessage() ,e);
        }
    }

    @Override
    public Cliente getClienteByDoc(ClienteRequest request) {
        try {
            Optional<Cliente> cliente = clienteDAO.getClienteByCpfCnpj(request.cpfCnpj());
            return  cliente.orElseThrow(() -> new RuntimeException("Cliente not found!"));
        } catch (SQLException e) {
            throw new RuntimeException("Error: " + e.getMessage() ,e);
        }
    }


    private void verificaDadosParaPersistenciaDeCliente(RegisterClienteRequest request) {
        if (request.name() == null || request.name().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio.");
        }
        if (request.cpfCnpj() == null || request.cpfCnpj().isEmpty()) {
            throw new IllegalArgumentException("CPF/CNPJ não pode ser vazio.");
        }
        if (request.valorLimiteCredito() == null || request.valorLimiteCredito().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O limite de crédito deve ser maior que zero.");
        }
        if (request.diaFechamentoFatura() == null || request.diaFechamentoFatura() < 1 || request.diaFechamentoFatura() > 31) {
            throw new IllegalArgumentException("Dia de fechamento da fatura deve ser entre 1 e 31.");
        }
    }
}
