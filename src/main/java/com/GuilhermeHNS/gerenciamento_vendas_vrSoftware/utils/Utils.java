package com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.utils;

import javax.swing.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.regex.Pattern;

public class Utils {
    public static LocalDate calculaDataValida(YearMonth anoMes, int diaFechamento) {
        int ultimoDiaMes = anoMes.lengthOfMonth();
        int diaValido = Math.min(diaFechamento, ultimoDiaMes);
        return LocalDate.of(anoMes.getYear(), anoMes.getMonth(), diaValido);
    }

    public static void exibeJPanel(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static boolean validaCPFouCNPJ(String input) {
        String regexCPF = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";
        String regexCNPJ = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$";

        return Pattern.matches(regexCPF, input) || Pattern.matches(regexCNPJ, input);
    }

}
