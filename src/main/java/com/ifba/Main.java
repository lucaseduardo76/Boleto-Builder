package com.ifba;

import com.ifba.builder.BancoBrasilBoletoBuilder;
import com.ifba.builder.BoletoBuilder;
import com.ifba.director.GeradorBoleto;
import com.ifba.model.boleto.Boleto;
import com.ifba.model.boleto.ContaEnum;
import com.ifba.model.entidade.Endereco;
import com.ifba.model.entidade.Entidade;
import com.ifba.utils.codigoBarras.CodigoBarra;
import com.ifba.utils.geradorPdf.BoletoPdfGenerator;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        BoletoBuilder builder = new BancoBrasilBoletoBuilder();
        GeradorBoleto gerador = new GeradorBoleto(builder);

        // Criar endereço para beneficiário
        Endereco enderecoBeneficiario = new Endereco();
        enderecoBeneficiario.setRua("Av. Brasil");
        enderecoBeneficiario.setBairro("Centro");
        enderecoBeneficiario.setCidade("Salvador");
        enderecoBeneficiario.setEstado("BA");
        enderecoBeneficiario.setCep("40000-000");

        // Criar beneficiário
        Entidade beneficiario = new Entidade();
        beneficiario.setNome("Empresa XYZ LTDA");
        beneficiario.setRazaoSocial("Empresa XYZ Comércio de Produtos LTDA");
        beneficiario.setCnpjOrCpf("12.345.678/0001-99");
        beneficiario.setEndereco(enderecoBeneficiario);

        // Criar endereço para sacado
        Endereco enderecoSacado = new Endereco();
        enderecoSacado.setRua("Rua das Flores");
        enderecoSacado.setBairro("Jardins");
        enderecoSacado.setCidade("São Paulo");
        enderecoSacado.setEstado("SP");
        enderecoSacado.setCep("01000-000");

        // Criar sacado
        Entidade sacado = new Entidade();
        sacado.setNome("João da Silva");
        sacado.setRazaoSocial(null); // Pessoa física, pode ser nulo
        sacado.setCnpjOrCpf("123.456.789-00");
        sacado.setEndereco(enderecoSacado);

        // Gerar boleto
        Boleto boleto = gerador.gerarBoleto(
                beneficiario,
                sacado,
                "123456789",
                LocalDate.of(2025, 6, 15),
                259.90,
                "1234",
                ContaEnum.CONTA_CORRENTE,
                "17",
                "261013"
        );

        boleto.exibirResumo();

        BoletoPdfGenerator.gerarBoletoPdf(boleto, "boleto"+boleto.getNumeroUnico()+".pdf");


    }
}
