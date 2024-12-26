package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao;

import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Venda;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface VendasDAO {
    BigDecimal getValorDisponivel(Long idCliente, String dataFechamento, String dataFinal) throws SQLException;
    void createVenda(Venda venda) throws SQLException;
}
