package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request;

import java.util.Optional;

public class ConsultaVendaRequest {
    private Optional<String> cpfCnpj;
    private String dataInicio;
    private String dataFim;
    private Optional<String> idProduto;

    public ConsultaVendaRequest(String cpfCnpj, String dataInicio, String dataFim, String idProduto) {
        this.cpfCnpj = Optional.of(cpfCnpj);
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.idProduto = Optional.of(idProduto);
    }

    public ConsultaVendaRequest() {
        this.cpfCnpj = Optional.empty();
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.idProduto = Optional.empty();
    }

    public Optional<String> getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(Optional<String> cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
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

    public Optional<String> getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Optional<String> idProduto) {
        this.idProduto = idProduto;
    }
}
