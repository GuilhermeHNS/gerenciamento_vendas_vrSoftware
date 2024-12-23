package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuForm extends JFrame{
    private JPanel menuPanel;
    private JMenuBar JMenuBar;
    private JMenu menuCliente;
    private JMenu menuProduto;
    private JMenu menuVendas;
    private JMenuItem menuItemCliente;

    private ClientesForm clientesForm;


    public MenuForm(ClientesForm clientesForm) {
        this.clientesForm = clientesForm;
        menuItemCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientesForm.setVisible(true);
            }
        });
    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }
}
