package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.response.HistoricoVendaClienteResponse;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Venda;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.VendaFilter;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface VendasDAO {
    BigDecimal getValorDisponivel(Long idCliente, String dataFechamento, String dataFinal) throws SQLException;
    void createVenda(Venda venda) throws SQLException;
    List<HistoricoVendaClienteResponse> getHistoricoVendasCliente(VendaFilter vendaFilter) throws SQLException;
}
