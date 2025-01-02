package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers.ProdutoController;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.UpdateProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProdutosForm extends JFrame {
    private JPanel painelProduto;
    private JTabbedPane tabbedPane1;
    private JTextField fieldConsultaDescricao;
    private JTable tableProdutos;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton salvarButton;
    private JButton cancelarButton;
    private JTextField fieldDescricao;
    private JTextField fieldPreco;
    private JButton limparButton;
    private JCheckBox ativoCheckBox;
    private final ProdutoController produtoController;
    private final int INICIAL = 0;
    private final int EDITANDO = 1;
    private final int NOVO = 2;
    private Long idProdutoEditado = 0L;
    private int ESTADO_ATUAL = INICIAL;
    private static Timer debounceTimer;

    public ProdutosForm(ProdutoController produtoController) {

        this.produtoController = produtoController;
        inicializarComponentes();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                listarTodosProdutos();
            }
        });
        limparButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpaCampos();
            }
        });
        novoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ESTADO_ATUAL = NOVO;
                habilitarBotoes();
            }
        });
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterProdutoRequest request = new RegisterProdutoRequest(
                        fieldDescricao.getText(),
                        fieldPreco.getText(),
                        ativoCheckBox.isSelected()
                );
                produtoController.salvarProduto(request);
                JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
                limpaCampos();
                ESTADO_ATUAL = INICIAL;
                habilitarBotoes();
                listarTodosProdutos();
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ESTADO_ATUAL = INICIAL;
                limpaCampos();
                habilitarBotoes();
                listarTodosProdutos();
            }
        });
        tableProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectedRow = tableProdutos.getSelectedRow();
                if (selectedRow != -1) {
                    String codigo = tableProdutos.getValueAt(selectedRow, 0).toString();
                    Produto produto = produtoController.consultaProduto(codigo);
                    idProdutoEditado = produto.codigo();
                    fieldDescricao.setText(produto.descricao());
                    fieldPreco.setText(produto.preco().toString());
                    ativoCheckBox.setSelected(produto.ativo());
                    ESTADO_ATUAL = EDITANDO;
                    habilitarBotoes();
                    tabbedPane1.setSelectedIndex(1);
                }
            }
        });
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateProdutoRequest request = new UpdateProdutoRequest(
                        idProdutoEditado,
                        fieldDescricao.getText(),
                        fieldPreco.getText(),
                        ativoCheckBox.isSelected()
                );
                produtoController.atualizaProduto(request);
                JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!");
                limpaCampos();
                ESTADO_ATUAL = INICIAL;
                habilitarBotoes();
                listarTodosProdutos();
            }
        });
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int resposta = JOptionPane.showConfirmDialog(
                        null,
                        "Você tem certeza que deseja excluir esse produto?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (resposta == JOptionPane.YES_OPTION) {
                    limpaCampos();
                    produtoController.inativarProduto(idProdutoEditado);
                    ESTADO_ATUAL = INICIAL;
                    habilitarBotoes();
                    listarTodosProdutos();
                }
            }
        });
        fieldConsultaDescricao.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String desc = fieldConsultaDescricao.getText();
                if (debounceTimer != null) {
                    debounceTimer.cancel();
                }
                debounceTimer = new Timer();
                debounceTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        List<Produto> produtoList = produtoController.listarProdutoPorDescricao(desc);
                        DefaultTableModel dados = (DefaultTableModel) tableProdutos.getModel();
                        dados.setNumRows(0);
                        produtoList.stream()
                                .forEach(produto -> {
                                    dados.addRow(new Object[]{
                                            produto.codigo(),
                                            produto.descricao(),
                                            produto.preco(),
                                            produto.ativo() ? "Ativo" : "Inativo"
                                    });
                                });
                    }
                }, 300);
            }
        });
    }

    private void inicializarComponentes() {
        this.setContentPane(painelProduto);
        this.setResizable(false);
        this.setSize(536, 457);
        String[] colunas = {"Código", "Descrição", "Preço", "Status"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableProdutos.setModel(modeloTabela);
        tableProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        habilitarBotoes();
        ativoCheckBox.setSelected(true);
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
                fieldDescricao.setEditable(false);
                fieldPreco.setEditable(false);
                ativoCheckBox.setEnabled(false);
                tabbedPane1.setSelectedIndex(0);
                tableProdutos.clearSelection();
                break;
            case EDITANDO:
                novoButton.setEnabled(false);
                editarButton.setEnabled(true);
                excluirButton.setEnabled(true);
                salvarButton.setEnabled(false);
                limparButton.setEnabled(true);
                fieldPreco.setEditable(true);
                fieldDescricao.setEditable(true);
                tabbedPane1.setSelectedIndex(1);
                cancelarButton.setEnabled(true);
                ativoCheckBox.setEnabled(true);
                break;
            case NOVO:
                novoButton.setEnabled(false);
                editarButton.setEnabled(false);
                excluirButton.setEnabled(false);
                salvarButton.setEnabled(true);
                limparButton.setEnabled(true);
                fieldDescricao.setEditable(true);
                fieldPreco.setEditable(true);
                tabbedPane1.setSelectedIndex(1);
                cancelarButton.setEnabled(true);
                ativoCheckBox.setEnabled(true);
                break;
            default:
        }
    }

    private void limpaCampos() {
        fieldPreco.setText("");
        fieldDescricao.setText("");
    }

    private void listarTodosProdutos() {
        List<Produto> produtoList;
        produtoList = produtoController.listarTodosOsProdutos();
        DefaultTableModel dados = (DefaultTableModel) tableProdutos.getModel();
        dados.setNumRows(0);
        produtoList.stream()
                .forEach(produto -> {
                    dados.addRow(new Object[]{
                            produto.codigo(),
                            produto.descricao(),
                            produto.preco(),
                            produto.ativo() ? "Ativo" : "Inativo"
                    });
                });
    }

}
