package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers.ClienteController;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ClienteDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl.ClienteDAOImpl;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl.ClienteServiceImpl;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view.ClientesForm;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view.MenuForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ClienteDAO clienteDAO = new ClienteDAOImpl();
        ClienteService clienteService = new ClienteServiceImpl(clienteDAO);
        ClienteController clienteController = new ClienteController(clienteService);

        ClientesForm clientesForm = new ClientesForm(clienteController);
        MenuForm menuForm = new MenuForm(clientesForm);
        menuForm.setContentPane(menuForm.getMenuPanel());
        menuForm.setSize(600, 600);
        menuForm.setResizable(false);
        menuForm.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menuForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuForm.setVisible(true);
    }
}