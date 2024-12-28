package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.configuration.DatabaseConnection;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasProdutoDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.Periodo;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.VendaProdutoDTO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.ConsultaVendaRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterVendaRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.UpdateVendaRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.response.HistoricoVendaClienteResponse;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ValidationException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.VendaProdutos;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Venda;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.VendaFilter;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.VendasService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.utils.UtilsDatas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.utils.ExibeJPanelError.exibeError;

public class VendasServiceImpl implements VendasService {
    private final ClienteService clienteService;
    private final VendasDAO vendasDAO;
    private final VendasProdutoDAO vendasProdutoDAO;

    public VendasServiceImpl(ClienteService clienteService, VendasDAO vendasDAO, VendasProdutoDAO vendasProdutoDAO) {
        this.clienteService = clienteService;
        this.vendasDAO = vendasDAO;
        this.vendasProdutoDAO = vendasProdutoDAO;
    }

    @Override
    public void createVenda(RegisterVendaRequest request) {
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                Cliente cliente = clienteService.getClienteByDoc(request.cpfCnpjCliente());
                validaValorExcedido(cliente, request.vendaProdutoList(), true, "");
                Venda venda = new Venda(-1L, cliente.codigo(), "");
                Long idVenda = vendasDAO.createVenda(venda, con);
                List<VendaProdutos> vendaProdutoList = request.vendaProdutoList()
                        .stream().map(produto -> new VendaProdutos(idVenda, Long.parseLong(produto.codigo()), Integer.parseInt(produto.quantidade()), new BigDecimal(produto.preco()))).collect(Collectors.toList());
                vendasProdutoDAO.createVendaProduto(vendaProdutoList, con);
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            exibeError("Erro: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateVenda(UpdateVendaRequest request) {
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                Cliente cliente = clienteService.getClienteByDoc(request.cpfCnpj());
                validaValorExcedido(cliente, request.vendaProdutoList(), false, request.dataCompra());
                Long idVenda = Long.parseLong(request.idVenda());
                vendasProdutoDAO.deleteProdutoByIdVenda(idVenda, con);
                List<VendaProdutos> vendaProdutoList = request.vendaProdutoList()
                        .stream().map(produto -> new VendaProdutos(idVenda, Long.parseLong(produto.codigo()), Integer.parseInt(produto.quantidade()), new BigDecimal(produto.preco()))).collect(Collectors.toList());
                vendasProdutoDAO.createVendaProduto(vendaProdutoList, con);
            } catch (Exception e) {
                con.rollback();
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            exibeError("Erro: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HistoricoVendaClienteResponse> getHistoricoVendasCliente(ConsultaVendaRequest request) {
        try {
            String cpfCnpj = request.cpfCnpj().orElseThrow(() -> new ValidationException("CPF / CNPJ é obrigatório"));
            Cliente cliente = clienteService.getClienteByDoc(cpfCnpj);
            Optional<Long> idProduto = request.idProduto().isPresent() ? Optional.of(Long.parseLong(request.idProduto().get())) : Optional.empty();
            VendaFilter vendaFilter = new VendaFilter(
                    Optional.of(cliente.codigo()),
                    request.dataInicio(),
                    request.dataFim(),
                    idProduto
            );
            return vendasDAO.getHistoricoVendasCliente(vendaFilter);
        } catch (SQLException e) {
            exibeError("Erro: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteVenda(String id) {
        try(Connection con = DatabaseConnection.getConnection()) {
            Long idVenda = Long.parseLong(id);
            con.setAutoCommit(false);
            try{
                vendasProdutoDAO.deleteProdutoByIdVenda(idVenda, con);
                vendasDAO.deleteVenda(idVenda, con);
            } catch (Exception e) {
                con.rollback();
                throw new SQLException(e);
            }
        } catch (SQLException e) {
            exibeError("Erro: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    private LocalDate calculaDataFechamento(int diaFechamento) {
        LocalDate dataAtual = LocalDate.now();
        YearMonth anoMesFechamento = diaFechamento < dataAtual.getDayOfMonth()
                ? YearMonth.of(dataAtual.getYear(), dataAtual.getMonth())
                : YearMonth.of(dataAtual.getYear(), dataAtual.getMonth().minus(1));

        return UtilsDatas.calculaDataValida(anoMesFechamento, diaFechamento);
    }

    private void validaValorExcedido(Cliente cliente, List<VendaProdutoDTO> vendaProdutoList, Boolean isNewVenda, String dataCompra) throws SQLException {
        Periodo periodo = isNewVenda
                ? calcularPeriodoNovaVenda(cliente.diaFechamentoFatura())
                : calcularPeriodoCompra(cliente.diaFechamentoFatura(), dataCompra);

        BigDecimal valorGasto = vendasDAO.getValorDisponivel(cliente.codigo(), periodo.dataInicial(), periodo.dataFinal());
        BigDecimal valorVenda = calcularValorVenda(vendaProdutoList);
        BigDecimal valorCreditoCliente = cliente.limiteCredito();

        if (valorVenda.add(valorGasto).compareTo(valorCreditoCliente) > 0) {
            throw new ValidationException("O limite de crédito foi excedido. \nValor disponível: R$"
                    + valorCreditoCliente.subtract(valorGasto)
                    + "\nPróximo fechamento: " + periodo.dataProximoFechamento());
        }
    }

    private Periodo calcularPeriodoNovaVenda(int diaFechamento) {
        LocalDate dataFechamento = calculaDataFechamento(diaFechamento);
        String dataInicial = dataFechamento.plusDays(1).atStartOfDay().toString().replace("T", " ");
        String dataFinal = LocalDate.now().atTime(LocalTime.MAX).toString().replace("T", " ");
        String proximoFechamento = dataFechamento.plusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return new Periodo(dataInicial, dataFinal, proximoFechamento);
    }

    private Periodo calcularPeriodoCompra(int diaFechamento, String dataCompra) {
        List<LocalDate> periodo = calculaPeriodoCompra(diaFechamento, dataCompra);
        String dataInicial = periodo.get(0).atStartOfDay().toString().replace("T", " ");
        String dataFinal = periodo.get(1).atTime(LocalTime.MAX).toString().replace("T", " ");
        String proximoFechamento = periodo.get(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return new Periodo(dataInicial, dataFinal, proximoFechamento);
    }

    private List<LocalDate> calculaPeriodoCompra(int diaFechamento, String dataCompraString) {
        LocalDate dataCompra = LocalDate.parse(dataCompraString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        YearMonth anoMesInicial = YearMonth.of(dataCompra.getYear(), dataCompra.getMonth());
        YearMonth anoMesFinal;
        if (dataCompra.getDayOfMonth() < diaFechamento) {
            anoMesInicial = anoMesInicial.minusMonths(1);
        }
        anoMesFinal = anoMesInicial.plusMonths(1);
        LocalDate dataInicial = UtilsDatas.calculaDataValida(anoMesInicial, diaFechamento);
        LocalDate dataFinal = UtilsDatas.calculaDataValida(anoMesFinal, diaFechamento);
        return Arrays.asList(dataInicial, dataFinal);
    }

    private BigDecimal calcularValorVenda(List<VendaProdutoDTO> vendaProdutoDTOList) {
        return vendaProdutoDTOList.stream()
                .map(produto -> new BigDecimal(produto.preco()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
