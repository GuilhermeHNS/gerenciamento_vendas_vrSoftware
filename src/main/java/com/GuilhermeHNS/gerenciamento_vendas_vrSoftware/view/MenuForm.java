package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuForm extends JFrame {
    private JPanel menuPanel;
    private JMenuBar JMenuBar;
    private JMenu menuCliente;
    private JMenuItem menuItemCliente;
    private JMenu produtosMenu;
    private JMenuItem produtosMenuItem;
    private JMenu vendasMenu;
    private JMenuItem registrarVendaMenuItem;

    private final ClientesForm clientesForm;
    private final ProdutosForm produtosForm;

    private final VendasForm vendasForm;

    public MenuForm(ClientesForm clientesForm, ProdutosForm produtosForm, VendasForm vendasForm) {
        this.clientesForm = clientesForm;
        this.produtosForm = produtosForm;
        this.vendasForm = vendasForm;
        menuItemCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientesForm.setVisible(true);
            }
        });

        produtosMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                produtosForm.setVisible(true);
            }
        });

        registrarVendaMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vendasForm.setVisible(true);
            }
        });
    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }
}
