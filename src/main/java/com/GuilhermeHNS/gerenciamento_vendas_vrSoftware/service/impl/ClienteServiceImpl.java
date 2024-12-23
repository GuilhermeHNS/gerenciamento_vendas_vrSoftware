package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ClienteDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterUpdateClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ClienteAlreadyExistException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ClienteValidationException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {

    private ClienteDAO clienteDAO;

    public ClienteServiceImpl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void createCliente(RegisterUpdateClienteRequest request) {
        try {
            verificaDadosParaPersistenciaDeCliente(request);
            Optional<Cliente> existingCliente = clienteDAO.getClienteByCpfCnpj(request.cpfCnpj());
            if (existingCliente.isPresent()) {
                exibeError("Cliente já existente!");
                throw new ClienteAlreadyExistException();
            }
            Cliente cliente = new Cliente(-1L, request.name(), request.cpfCnpj(), new BigDecimal(request.valorLimiteCredito()), Integer.parseInt(request.diaFechamentoFatura()));
            clienteDAO.createCliente(cliente);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente getClienteByDoc(String cpfCnpj) {
        try {
            Optional<Cliente> cliente = clienteDAO.getClienteByCpfCnpj(cpfCnpj);
            return cliente.orElseThrow(() -> new RuntimeException("Cliente not found!"));
        } catch (SQLException e) {
            exibeError("Não foi possível obter o cliente!");
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateCliente(RegisterUpdateClienteRequest request) {
        verificaDadosParaPersistenciaDeCliente(request);
        try {
            Cliente cliente = new Cliente(-1L, request.name(), request.cpfCnpj(), new BigDecimal(request.valorLimiteCredito()), Integer.parseInt(request.diaFechamentoFatura()));
            clienteDAO.updateCliente(cliente);
        } catch (SQLException e) {
            exibeError("Não foi possível atualizar o cliente!");
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCliente(String cpfCnpj) {
        try {
            clienteDAO.deleteCliente(cpfCnpj);
        } catch (SQLException e) {
            exibeError("Não foi possível deleter o cliente!");
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> getAllClientes() {
        try {
            return clienteDAO.getAllClientes();
        } catch (SQLException e) {
            exibeError("Não foi possível efetuar a busca dos clientes!");
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    private void verificaDadosParaPersistenciaDeCliente(RegisterUpdateClienteRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new ClienteValidationException("Nome do cliente não pode ser vazio.");
        }
        if (request.cpfCnpj() == null || request.cpfCnpj().isBlank()) {
            throw new ClienteValidationException("CPF/CNPJ não pode ser vazio.");
        }
        if (request.valorLimiteCredito() == null || request.valorLimiteCredito().isBlank() ||new BigDecimal(request.valorLimiteCredito()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new ClienteValidationException("O limite de crédito deve ser maior que zero.");
        }
        if (request.diaFechamentoFatura() == null || request.diaFechamentoFatura().isBlank() ||Integer.parseInt(request.diaFechamentoFatura()) < 1 || Integer.parseInt(request.diaFechamentoFatura()) > 31) {
            throw new ClienteValidationException("Dia de fechamento da fatura deve ser entre 1 e 31.");
        }
    }

    private void exibeError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

}
