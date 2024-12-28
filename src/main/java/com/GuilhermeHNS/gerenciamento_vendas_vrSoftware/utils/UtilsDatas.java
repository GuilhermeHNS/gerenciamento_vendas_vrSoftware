package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.utils;

import java.time.LocalDate;
import java.time.YearMonth;

public class UtilsDatas {

    public static LocalDate calculaDataValida(YearMonth anoMes, int diaFechamento) {
        int ultimoDiaMes = anoMes.lengthOfMonth();
        int diaValido = Math.min(diaFechamento, ultimoDiaMes);
        return LocalDate.of(anoMes.getYear(), anoMes.getMonth(), diaValido);
    }
}
