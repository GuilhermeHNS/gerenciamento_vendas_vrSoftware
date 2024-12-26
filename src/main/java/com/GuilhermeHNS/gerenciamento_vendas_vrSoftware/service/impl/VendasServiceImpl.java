package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.VendasDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.ConsultaVendaRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterVendaRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.response.HistoricoVendaClienteResponse;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ValidationException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.ProdutoVenda;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Venda;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.VendaFilter;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.VendasService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.utils.ExibeJPanelError.exibeError;

public class VendasServiceImpl implements VendasService {
    private final ClienteService clienteService;
    private final VendasDAO vendasDAO;

    public VendasServiceImpl(ClienteService clienteService, VendasDAO vendasDAO) {
        this.clienteService = clienteService;
        this.vendasDAO = vendasDAO;
    }

    @Override
    public void createVenda(RegisterVendaRequest request) {
        try {
            Cliente cliente = clienteService.getClienteByDoc(request.cpfCnpjCliente());
            validaCadastroDeVenda(cliente, request);
            List<ProdutoVenda> produtoList = request.vendaProdutoList().stream()
                    .map(produto -> new ProdutoVenda(Long.parseLong(produto.codigo()), Integer.parseInt(produto.quantidade()), new BigDecimal(produto.preco())))
                    .collect(Collectors.toList());

            Venda venda = new Venda(cliente.codigo(), produtoList);
            vendasDAO.createVenda(venda);
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


    private LocalDate calculaDataFechamento(int diaFechamento) {
        LocalDate dataAtual = LocalDate.now();
        YearMonth anoMesFechamento = diaFechamento < dataAtual.getDayOfMonth()
                ? YearMonth.of(dataAtual.getYear(), dataAtual.getMonth())
                : YearMonth.of(dataAtual.getYear(), dataAtual.getMonth().minus(1));

        int ultimoDiaMes = anoMesFechamento.lengthOfMonth();
        int diaValido = Math.min(diaFechamento, ultimoDiaMes);
        return LocalDate.of(anoMesFechamento.getYear(), anoMesFechamento.getMonth(), diaValido);
    }

    private void validaCadastroDeVenda(Cliente cliente, RegisterVendaRequest request) throws SQLException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataFechamento = calculaDataFechamento(cliente.diaFechamentoFatura());

        String dataFechamentoFormatada = dataFechamento.plusDays(1).atStartOfDay().toString().replace("T", " ");
        String dataFinal = LocalDate.now().atStartOfDay().toString().replace("T", " ");

        BigDecimal valorCreditoCliente = cliente.limiteCredito();
        BigDecimal valorGasto = vendasDAO.getValorDisponivel(cliente.codigo(), dataFechamentoFormatada, dataFinal);
        BigDecimal valorVenda = request.vendaProdutoList().stream()
                .map(produto -> new BigDecimal(produto.preco()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (valorVenda.add(valorGasto).compareTo(valorCreditoCliente) > 0) {
            throw new ValidationException("O limite de crédito foi excedido. \n Valor disponivel: R$" + valorCreditoCliente.subtract(valorGasto)
                    + "\n e o próximo fechamento ocorre em " + dataFechamento.plusMonths(1).format(dateFormatter));
        }
    }
}
