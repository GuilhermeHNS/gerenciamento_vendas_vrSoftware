package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers.ClienteController;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers.ProdutoController;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers.VendasController;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ClienteDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ProdutoDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasProdutoDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl.ClienteDAOImpl;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl.ProdutoDAOImpl;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl.VendasDAOImpl;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.impl.VendasProdutoDAOImpl;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ProdutoService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.VendasService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl.ClienteServiceImpl;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl.ProdutoServiceImpl;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl.VendasServiceImpl;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view.ClientesForm;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view.MenuForm;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view.ProdutosForm;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view.VendasForm;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ClienteDAO clienteDAO = new ClienteDAOImpl();
        MenuForm menuForm = getMenuForm(clienteDAO);
        menuForm.setContentPane(menuForm.getMenuPanel());
        menuForm.setSize(600, 600);
        menuForm.setResizable(false);
        menuForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuForm.setVisible(true);
    }

    @NotNull
    private static MenuForm getMenuForm(ClienteDAO clienteDAO) {
        ClienteService clienteService = new ClienteServiceImpl(clienteDAO);
        ClienteController clienteController = new ClienteController(clienteService);
        ClientesForm clientesForm = new ClientesForm(clienteController);

        ProdutoDAO produtoDAO = new ProdutoDAOImpl();
        ProdutoService produtoService = new ProdutoServiceImpl(produtoDAO);
        ProdutoController produtoController = new ProdutoController(produtoService);
        ProdutosForm produtosForm = new ProdutosForm(produtoController);

        VendasDAO vendasDAO = new VendasDAOImpl();
        VendasProdutoDAO vendasProdutoDAO = new VendasProdutoDAOImpl();
        VendasService vendasService = new VendasServiceImpl(clienteService, vendasDAO, vendasProdutoDAO);
        VendasController vendasController = new VendasController(vendasService);

        VendasForm vendasForm = new VendasForm(clienteController, produtoController, vendasController);

        MenuForm menuForm = new MenuForm(clientesForm, produtosForm, vendasForm);
        return menuForm;
    }
}