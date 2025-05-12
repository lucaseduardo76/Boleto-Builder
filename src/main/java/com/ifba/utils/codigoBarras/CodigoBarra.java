package com.ifba.utils.codigoBarras;

import com.ifba.builder.BoletoBuilder;
import com.ifba.model.boleto.Boleto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CodigoBarra {
    public static String calcularFatorVencimento(LocalDate vencimento) {
        LocalDate base = LocalDate.of(1997, 10, 7);
        long dias = ChronoUnit.DAYS.between(base, vencimento);
        return String.format("%04d", dias);
    }

    public static String formatarValor(Double valor) {
        BigDecimal valorBig = BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_EVEN);
        String valorFormatado = valorBig.toString().replace(".", "").replace(",", "");
        return String.format("%010d", Long.parseLong(valorFormatado));
    }


    public static int calcularDigitoVerificadorModulo11(String codigo) {
        int soma = 0;
        int peso = 2;
        for (int i = codigo.length() - 1; i >= 0; i--) {
            int num = Character.getNumericValue(codigo.charAt(i));
            soma += num * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;
        int digito = 11 - resto;
        if (digito == 0 || digito == 10 || digito == 11) {
            return 1;
        }
        return digito;
    }

    public static String gerarCodigoBarras(Boleto boleto, BoletoBuilder boletoBuilder) {
        String banco = boleto.getBanco().getCodigo(); // ex: "001"
        String moeda = "9";
        String fatorVencimento = CodigoBarra.calcularFatorVencimento(boleto.getTitulo().getDataVencimento());
        String valor = formatarValor(boleto.getTitulo().getValor());

        String campoLivre = boletoBuilder.gerarCampoLivre(
                boleto.getConta().getAgencia(),
                boleto.getConta().getNumero(),
                boleto.getNumeroUnico(),
                boleto.getConta().getCarteira()
        );

        String parcial = banco + moeda + fatorVencimento + valor + campoLivre;

        int digito = calcularDigitoVerificadorModulo11(parcial);

        return banco + moeda + digito + fatorVencimento + valor + campoLivre;
    }



}
