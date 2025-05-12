package com.ifba.builder;

import com.ifba.model.boleto.Banco;
import com.ifba.model.boleto.Boleto;
import com.ifba.model.boleto.ContaBancaria;
import com.ifba.model.entidade.Entidade;
import lombok.Getter;

import java.time.LocalDate;



@Getter
public class BancoBrasilBoletoBuilder implements BoletoBuilder {
    private final Boleto boleto;

    public BancoBrasilBoletoBuilder() {
        this.boleto = new Boleto();
    }

    public void buildBeneficiario(Entidade Beneficiario) {
        boleto.setBeneficiario(Beneficiario);
    }

    public void buildSacado(Entidade Sacado) {
        boleto.setSacado(Sacado);
    }

    public void buildTitulo(String doc, LocalDate vencimento, double valor) {
        boleto.getTitulo().setNumeroDocumento(doc);
        boleto.getTitulo().setDataVencimento(vencimento);
        boleto.getTitulo().setValor(valor);
    }

    public void buildDadosBancarios(ContaBancaria contaBancaria) {
        boleto.setConta(contaBancaria);
    }

    public void buildBanco() {
        boleto.setBanco(Banco.BANCO_DO_BRASIL);
    }


    public String gerarCampoLivre(String agencia, String conta, String nossoNumero, String carteira) {
        return String.format("%04d%08d%011d%02d",
                Integer.parseInt(agencia),
                Integer.parseInt(conta),
                Long.parseLong(nossoNumero),
                Integer.parseInt(carteira));
    }



}
