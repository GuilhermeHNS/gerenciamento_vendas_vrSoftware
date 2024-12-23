package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers.ClienteController;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterUpdateClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ClientesForm extends JFrame {
    private JTabbedPane tabbedCliente;
    private JPanel panelCliente;
    private JPanel tabCadastro;
    private JPanel tabConsulta;
    private JLabel lbName;
    private JTextField fieldName;
    private JLabel lbValorLimite;
    private JTextField fieldValorCredito;
    private JTextField fieldDiaFechamentoFatura;
    private JButton salvarButton;
    private JLabel lbCpfCnpj;
    private JTextField fieldCpfCnpj;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JTextField fieldConsulta;
    private JButton pesquisarButton;
    private JTable tableClientes;
    private JPanel consultaPanel;
    private JButton cancelarButton;

    private final int INICIAL = 0;
    private final int EDITANDO = 1;
    private final int NOVO = 2;

    private int ESTADO_ATUAL = INICIAL;

    private ClienteController clienteController;

    public ClientesForm(ClienteController clienteController) {
        this.clienteController = clienteController;
        inicializaComponentes();
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest(
                        fieldName.getText(),
                        fieldCpfCnpj.getText(),
                        fieldValorCredito.getText(),
                        fieldDiaFechamentoFatura.getText()
                );
                clienteController.registerCliente(request);
                limpaCampos();
                ESTADO_ATUAL = INICIAL;
                habilitarBotoes();
                listaClientes();
                JOptionPane.showMessageDialog(new JFrame(), "Cliente cadastrado com sucesso!");
            }
        });
        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpfCnpj = fieldConsulta.getText();
                Cliente cliente = clienteController.consultaCliente(cpfCnpj);
                DefaultTableModel dados = (DefaultTableModel) tableClientes.getModel();
                dados.setNumRows(0);
                dados.addRow(new Object[]{
                        cliente.nome(),
                        cliente.cpfCnpj(),
                        cliente.limiteCredito(),
                        cliente.diaFechamentoFatura(),
                });
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                listaClientes();
            }
        });
        novoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ESTADO_ATUAL = NOVO;
                tabbedCliente.setSelectedIndex(1);
                habilitarBotoes();
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ESTADO_ATUAL = INICIAL;
                limpaCampos();
                habilitarBotoes();
            }
        });
        tableClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectedRow = tableClientes.getSelectedRow();
                if (selectedRow != -1) {
                    String cpfCnpj = tableClientes.getValueAt(selectedRow, 1).toString();
                    Cliente cliente = clienteController.consultaCliente(cpfCnpj);
                    fieldName.setText(cliente.nome());
                    fieldCpfCnpj.setText(cliente.cpfCnpj());
                    fieldCpfCnpj.setEnabled(false);
                    fieldValorCredito.setText(cliente.limiteCredito().toString());
                    fieldDiaFechamentoFatura.setText(cliente.diaFechamentoFatura().toString());
                    ESTADO_ATUAL = EDITANDO;
                    tabbedCliente.setSelectedIndex(1);
                    habilitarBotoes();
                }

            }
        });
        limparButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpaCampos();
            }
        });
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest(
                        fieldName.getText(),
                        fieldCpfCnpj.getText(),
                        fieldValorCredito.getText(),
                        fieldDiaFechamentoFatura.getText()
                );
                clienteController.atualizaCliente(request);
                limpaCampos();
                ESTADO_ATUAL = INICIAL;
                habilitarBotoes();
                listaClientes();
                JOptionPane.showMessageDialog(new JFrame(), "Cliente atualizado com sucesso!");

            }
        });
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int resposta = JOptionPane.showConfirmDialog(
                        null,
                        "Você tem certeza que deseja excluir esse cliente?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (resposta == JOptionPane.YES_OPTION) {
                    String cpfCnpj = fieldCpfCnpj.getText();
                    clienteController.deleteCliente(cpfCnpj);
                    limpaCampos();
                    ESTADO_ATUAL = INICIAL;
                    habilitarBotoes();
                    listaClientes();
                }
            }
        });
    }

    private void limpaCampos() {
        if(ESTADO_ATUAL != EDITANDO) {
            fieldCpfCnpj.setText("");
        }
        fieldName.setText("");
        fieldValorCredito.setText("");
        fieldDiaFechamentoFatura.setText("");
        fieldConsulta.setText("");
    }
    private void habilitarBotoes() {
        switch (ESTADO_ATUAL) {
            case INICIAL:
                novoButton.setEnabled(true);
                editarButton.setEnabled(false);
                excluirButton.setEnabled(false);
                salvarButton.setEnabled(false);
                limparButton.setEnabled(false);
                cancelarButton.setEnabled(false);
                fieldName.setEditable(false);
                fieldValorCredito.setEditable(false);
                fieldDiaFechamentoFatura.setEditable(false);
                fieldCpfCnpj.setEditable(false);
                tabbedCliente.setSelectedIndex(0);
                tableClientes.clearSelection();
                break;
            case EDITANDO:
                novoButton.setEnabled(false);
                editarButton.setEnabled(true);
                excluirButton.setEnabled(true);
                salvarButton.setEnabled(false);
                limparButton.setEnabled(true);
                fieldName.setEditable(true);
                fieldValorCredito.setEditable(true);
                fieldDiaFechamentoFatura.setEditable(true);
                fieldCpfCnpj.setEditable(true);
                cancelarButton.setEnabled(true);
                break;
            case NOVO:
                novoButton.setEnabled(false);
                editarButton.setEnabled(false);
                excluirButton.setEnabled(false);
                salvarButton.setEnabled(true);
                limparButton.setEnabled(true);
                fieldName.setEditable(true);
                fieldValorCredito.setEditable(true);
                fieldDiaFechamentoFatura.setEditable(true);
                fieldCpfCnpj.setEditable(true);
                cancelarButton.setEnabled(true);
                break;
            default:
        }
    }

    private void listaClientes() {
        DefaultTableModel dados = (DefaultTableModel) tableClientes.getModel();
        dados.setNumRows(0);
        List<Cliente> clienteList = new ArrayList<>();
        clienteList = clienteController.listaTodosOsClientes();
        clienteList.stream().forEach(cliente -> {
            dados.addRow(new Object[]{
                    cliente.nome(),
                    cliente.cpfCnpj(),
                    cliente.limiteCredito(),
                    cliente.diaFechamentoFatura(),
            });
        });
    }

    private void inicializaComponentes() {
        this.setContentPane(panelCliente);
        this.setSize(600, 600);
        setResizable(false);
        String[] colunas = {"Nome", "CPF/CNPJ", "Limite de Crédito", "Dia de Fechamento"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableClientes.setModel(modeloTabela);
        tableClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        habilitarBotoes();

    }
}
