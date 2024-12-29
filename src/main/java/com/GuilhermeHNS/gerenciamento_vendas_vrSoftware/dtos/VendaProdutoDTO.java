package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos;

public class VendaProdutoDTO {
    private String codigo;
    private String preco;
    private String quantidade;

    public VendaProdutoDTO(String codigo, String preco, String quantidade) {
        this.codigo = codigo;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }
}
