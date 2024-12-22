package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model;

import java.math.BigDecimal;

public record Cliente(Long codigo, String nome, String cpfCnpj,BigDecimal limiteCredito, Integer diaFechamentoFatura) {
}
