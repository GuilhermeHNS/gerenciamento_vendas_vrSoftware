package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers.ClienteController;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers.ProdutoController;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.controllers.VendasController;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.VendaProdutoDTO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterVendaRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.view.documents.FieldNumberDocument;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.utils.Utils.exibeJPanel;

public class VendasForm extends JFrame {
    private JTextField codProdutoField;
    private JTextField descricaoField;
    private JTextField precoField;
    private JTextField qtdField;
    private JButton pesquisarProdutoBtn;
    private JTextField qtdProdutosField;
    private JTextField valorTotalField;
    private JTable tableProdutos;
    private JPanel vendasPanel;
    private JTextField cpfCnpjField;
    private JButton adicionarButton;
    private JTextField nomeField;
    private JButton pesquisarClienteButton;
    private JButton efetuarVendaButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton cancelarButton;
    private Cliente cliente = null;
    private Produto produto = null;
    private List<VendaProdutoDTO> produtosAdicionados = new ArrayList<>();
    private Integer countProdutoCarrinho = 0;
    private BigDecimal valorTotal = new BigDecimal(0.00);
    private final ClienteController clienteController;
    private final ProdutoController produtoController;
    private final VendasController vendasController;
    private final int INICIAL = 0;
    private final int EDITANDO = 1;
    private int ESTADO_ATUAL = INICIAL;
    private int linhaEditada = -1;


    public VendasForm(ClienteController clienteController, ProdutoController produtoController, VendasController vendasController) {
        this.clienteController = clienteController;
        this.produtoController = produtoController;
        this.vendasController = vendasController;
        inicializaComponentes();
        pesquisarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpfCnpj = cpfCnpjField.getText();
                cliente = clienteController.consultaCliente(cpfCnpj);
                nomeField.setText(cliente.nome());
            }
        });
        pesquisarProdutoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codProduto = codProdutoField.getText();
                produto = produtoController.consultaProduto(codProduto);
                descricaoField.setText(produto.descricao());
                precoField.setText(produto.preco().toString());
            }
        });
        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean alreadyAdd = produtosAdicionados.stream()
                        .anyMatch(p -> Long.parseLong(p.getCodigo()) == produto.codigo());

                if (!alreadyAdd) {
                    valorTotal = valorTotal.add(produto.preco().multiply(new BigDecimal(qtdField.getText())));
                    produtosAdicionados.add(new VendaProdutoDTO(produto.codigo().toString(), produto.preco().toString(), qtdField.getText()));
                    DefaultTableModel dados = (DefaultTableModel) tableProdutos.getModel();
                    dados.addRow(new Object[]{
                            produto.codigo(),
                            produto.descricao(),
                            produto.preco(),
                            qtdField.getText()
                    });
                    countProdutoCarrinho++;
                    qtdProdutosField.setText(countProdutoCarrinho.toString());
                    produto = null;
                    limpaCamposProduto();
                    valorTotalField.setText(valorTotal.toString());
                } else {
                    exibeJPanel("Produto já adicionado ao carrinho!");
                }
            }
        });
        tableProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectedRow = tableProdutos.getSelectedRow();
                if (selectedRow != -1) {
                    linhaEditada = selectedRow;
                    String codigo = tableProdutos.getValueAt(selectedRow, 0).toString();
                    String desc = tableProdutos.getValueAt(selectedRow, 1).toString();
                    String preco = tableProdutos.getValueAt(selectedRow, 2).toString();
                    String qtd = tableProdutos.getValueAt(selectedRow, 3).toString();
                    codProdutoField.setText(codigo);
                    descricaoField.setText(desc);
                    precoField.setText(preco);
                    qtdField.setText(qtd);
                    ESTADO_ATUAL = EDITANDO;
                    habilitaCampos();
                }
            }
        });
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer qtdAtualizada = Integer.parseInt(qtdField.getText());
                DefaultTableModel modeloTabela = (DefaultTableModel) tableProdutos.getModel();
                modeloTabela.setValueAt(qtdAtualizada, linhaEditada, 3);
                String idProdutoSelecionado = modeloTabela.getValueAt(linhaEditada, 0).toString();
                produtosAdicionados.stream()
                        .filter(p -> p.getCodigo().equals(idProdutoSelecionado))
                        .findFirst()
                        .ifPresent(p -> p.setQuantidade(qtdAtualizada.toString()));
                calculaValorTotal();
                ESTADO_ATUAL = INICIAL;
                habilitaCampos();
                limpaCamposProduto();
            }
        });
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableProdutos.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel modeloTabela = (DefaultTableModel) tableProdutos.getModel();
                    Long codigo = Long.parseLong(tableProdutos.getValueAt(selectedRow, 0).toString());
                    produtosAdicionados.removeIf(p -> Long.parseLong(p.getCodigo()) == codigo);
                    modeloTabela.removeRow(selectedRow);
                    countProdutoCarrinho--;
                    qtdProdutosField.setText(countProdutoCarrinho.toString());
                    calculaValorTotal();
                    ESTADO_ATUAL = INICIAL;
                    habilitaCampos();
                    limpaCamposProduto();
                }
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ESTADO_ATUAL = INICIAL;
                habilitaCampos();
                limpaCamposProduto();
            }
        });
        efetuarVendaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterVendaRequest request = new RegisterVendaRequest(cliente.cpfCnpj(), produtosAdicionados);
                vendasController.registerVenda(request);
                JOptionPane.showMessageDialog(null, "Venda registrada com sucesso!");
                limpaTodosOsCampos();
            }
        });
    }

    private void inicializaComponentes() {
        this.setContentPane(vendasPanel);
        this.setResizable(false);
        this.setSize(1215, 750);

        String[] colunas = {"Código", "Descrição", "Preço", "Quantidade"};
        Object[][] emptyData = {};
        DefaultTableModel modeloTabela = new DefaultTableModel(emptyData, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableProdutos.setModel(modeloTabela);
        valorTotalField.setText(valorTotal.toString());
        qtdProdutosField.setText(countProdutoCarrinho.toString());
        ((AbstractDocument) qtdField.getDocument()).setDocumentFilter(new FieldNumberDocument());
        ((AbstractDocument) codProdutoField.getDocument()).setDocumentFilter(new FieldNumberDocument());
        habilitaCampos();
    }

    private void limpaCamposProduto() {
        codProdutoField.setText("");
        descricaoField.setText("");
        precoField.setText("");
        qtdField.setText("");
    }

    private void habilitaCampos() {
        switch (ESTADO_ATUAL) {
            case INICIAL:
                editarButton.setEnabled(false);
                excluirButton.setEnabled(false);
                cancelarButton.setEnabled(false);
                adicionarButton.setEnabled(true);
                pesquisarProdutoBtn.setEnabled(true);
                codProdutoField.setEditable(true);
                efetuarVendaButton.setEnabled(true);
                break;
            case EDITANDO:
                editarButton.setEnabled(true);
                excluirButton.setEnabled(true);
                cancelarButton.setEnabled(true);
                adicionarButton.setEnabled(false);
                pesquisarProdutoBtn.setEnabled(false);
                codProdutoField.setEditable(false);
                efetuarVendaButton.setEnabled(false);
                break;
            default:
        }
    }

    private void calculaValorTotal() {
        valorTotal = BigDecimal.ZERO;
        DefaultTableModel modeloTabela = (DefaultTableModel) tableProdutos.getModel();
        int rowCount = modeloTabela.getRowCount();
        for (int row = 0; row < rowCount; row++) {
            BigDecimal valorProduto = new BigDecimal(modeloTabela.getValueAt(row, 2).toString());
            BigDecimal qtdProduto = new BigDecimal(modeloTabela.getValueAt(row, 3).toString());
            valorTotal = valorTotal.add(valorProduto.multiply(qtdProduto));
        }
        valorTotalField.setText(valorTotal.toString());
    }

    private void limpaTodosOsCampos() {
        limpaCamposProduto();
        cpfCnpjField.setText("");
        valorTotalField.setText("");
        qtdProdutosField.setText("");
        nomeField.setText("");
        countProdutoCarrinho = 0;
        cliente = null;
        produto = null;
        produtosAdicionados = new ArrayList<>();
        valorTotal = BigDecimal.ZERO;
        DefaultTableModel modeloTabela = (DefaultTableModel) tableProdutos.getModel();
        modeloTabela.setNumRows(0);
    }
}
