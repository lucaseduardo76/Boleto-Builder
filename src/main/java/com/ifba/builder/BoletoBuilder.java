package com.ifba.builder;

import com.ifba.model.boleto.Boleto;
import com.ifba.model.boleto.ContaBancaria;
import com.ifba.model.entidade.Entidade;

import java.time.LocalDate;

public interface BoletoBuilder {
    void buildBeneficiario(Entidade beneficiario);
    void buildSacado(Entidade sacado);
    void buildTitulo(String numeroDocumento, LocalDate vencimento, double valor);
    void buildDadosBancarios(ContaBancaria contaBancaria);
    void buildBanco();
    void buildLinhaDigitavel();
    void buildCodigoBarras();

    Boleto getBoleto();

    String gerarCampoLivre(String agencia, String descricao, String numeroUnico, String carteira);
}
