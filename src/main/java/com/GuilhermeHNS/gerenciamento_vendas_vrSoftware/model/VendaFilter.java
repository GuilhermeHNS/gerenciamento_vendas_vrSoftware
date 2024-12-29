package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model;

import java.time.LocalDate;
import java.util.Optional;

public class VendaFilter {
    private Optional<Long> idCliente;
    private String dataInicio;
    private String dataFim;
    private Optional<Long> idProduto;

    public VendaFilter(Long idCliente, String dataInicio, String dataFim, Long idProduto) {
        this.idCliente = Optional.of(idCliente);
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.idProduto = Optional.of(idProduto);
    }

    public VendaFilter() {
        this.idCliente = Optional.empty();
        this.dataInicio = "";
        this.dataFim = "";
        this.idProduto = Optional.empty();
    }

    public Optional<Long> getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Optional<Long> idCliente) {
        this.idCliente = idCliente;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Optional<Long> getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Optional<Long> idProduto) {
        this.idProduto = idProduto;
    }
}
