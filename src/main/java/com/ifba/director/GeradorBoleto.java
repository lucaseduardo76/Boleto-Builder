package com.ifba.director;

import com.ifba.builder.BoletoBuilder;
import com.ifba.model.boleto.Boleto;

import com.ifba.model.boleto.ContaBancaria;
import com.ifba.model.boleto.ContaEnum;
import com.ifba.model.entidade.Entidade;
import com.ifba.utils.codigoBarras.CodigoBarra;

import java.time.LocalDate;

public class GeradorBoleto {
    private final BoletoBuilder builder;

    public GeradorBoleto(BoletoBuilder builder) {
        this.builder = builder;
    }

    public Boleto gerarBoleto(
            Entidade beneficiario,
            Entidade sacado,
            String numeroDocumento,
            LocalDate vencimento,
            double valor,
            String agencia,
            ContaEnum tipoConta,
            String carteira,
            String numeroConta
    ) {
        builder.buildBeneficiario(beneficiario);
        builder.buildSacado(sacado);
        builder.buildTitulo(numeroDocumento, vencimento, valor);

        ContaBancaria contaBancaria = new ContaBancaria();
        contaBancaria.setAgencia(agencia);
        contaBancaria.setTipoConta(tipoConta);
        contaBancaria.setCarteira(carteira);
        contaBancaria.setNumero(numeroConta);

        builder.buildDadosBancarios(contaBancaria);
        builder.buildBanco();

        builder.getBoleto().setLinhaDigitavel(CodigoBarra.gerarCodigoBarras(builder.getBoleto(), builder));
        builder.getBoleto().setCodigoBarras(CodigoBarra.gerarCodigoBarras(builder.getBoleto(), builder));
        return builder.getBoleto();

    }
}

