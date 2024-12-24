package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuForm extends JFrame {
    private JPanel menuPanel;
    private JMenuBar JMenuBar;
    private JMenu menuCliente;
    private JMenu menuVendas;
    private JMenuItem menuItemCliente;
    private JMenu produtosMenu;
    private JMenuItem produtosMenuItem;

    private final ClientesForm clientesForm;
    private final ProdutosForm produtosForm;

    public MenuForm(ClientesForm clientesForm, ProdutosForm produtosForm) {
        this.clientesForm = clientesForm;
        this.produtosForm = produtosForm;
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
    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }
}
