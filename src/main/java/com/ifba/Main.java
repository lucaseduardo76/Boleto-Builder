package com.ifba;

import com.ifba.builder.BancoBrasilBoletoBuilder;
import com.ifba.builder.BoletoBuilder;
import com.ifba.builder.BradescoBoletoBuilder;
import com.ifba.builder.ItauBoletoBuilder;
import com.ifba.director.GeradorBoleto;
import com.ifba.model.boleto.Boleto;
import com.ifba.model.boleto.ContaEnum;
import com.ifba.model.entidade.Endereco;
import com.ifba.model.entidade.Entidade;
import com.ifba.utils.codigoBarras.CodigoBarra;
import com.ifba.utils.geradorPdf.BoletoPdfGenerator;

import java.time.LocalDate;


import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Criar o builder e o gerador
        BoletoBuilder builder = null;
        while (builder == null) {
            System.out.println("Escolha o banco para emissão do boleto: ");
            System.out.println("1 - Banco do Brasil ");
            System.out.println("2 - Banco Bradesco");
            System.out.println("3 - Banco Itaú");
            int optionBanco = scanner.nextInt();

            builder = switch (optionBanco) {
                case 1 -> new BancoBrasilBoletoBuilder();
                case 2 -> new BradescoBoletoBuilder();
                case 3 -> new ItauBoletoBuilder();
                default -> null;
            };
        }
        GeradorBoleto gerador = new GeradorBoleto(builder);

        // Solicitar dados do beneficiário
        System.out.println("Digite os dados do beneficiário:");

        Endereco enderecoBeneficiario = new Endereco();
        scanner.nextLine();
        System.out.print("Rua do beneficiário: ");
        enderecoBeneficiario.setRua(scanner.nextLine());
        System.out.print("Bairro do beneficiário: ");
        enderecoBeneficiario.setBairro(scanner.nextLine());
        System.out.print("Cidade do beneficiário: ");
        enderecoBeneficiario.setCidade(scanner.nextLine());
        System.out.print("Estado do beneficiário: ");
        enderecoBeneficiario.setEstado(scanner.nextLine());
        System.out.print("CEP do beneficiário: ");
        enderecoBeneficiario.setCep(scanner.nextLine());

        Entidade beneficiario = new Entidade();
        System.out.print("Nome do beneficiário: ");
        beneficiario.setNome(scanner.nextLine());
        System.out.print("Razão Social do beneficiário: ");
        beneficiario.setRazaoSocial(scanner.nextLine());
        System.out.print("CNPJ ou CPF do beneficiário: ");
        beneficiario.setCnpjOrCpf(scanner.nextLine());
        beneficiario.setEndereco(enderecoBeneficiario);

        // Solicitar dados do sacado
        System.out.println("\nDigite os dados do sacado:");

        Endereco enderecoSacado = new Endereco();
        System.out.print("Rua do sacado: ");
        enderecoSacado.setRua(scanner.nextLine());
        System.out.print("Bairro do sacado: ");
        enderecoSacado.setBairro(scanner.nextLine());
        System.out.print("Cidade do sacado: ");
        enderecoSacado.setCidade(scanner.nextLine());
        System.out.print("Estado do sacado: ");
        enderecoSacado.setEstado(scanner.nextLine());
        System.out.print("CEP do sacado: ");
        enderecoSacado.setCep(scanner.nextLine());

        Entidade sacado = new Entidade();
        System.out.print("Nome do sacado: ");
        sacado.setNome(scanner.nextLine());
        sacado.setRazaoSocial(null);
        System.out.print("CNPJ ou CPF do sacado: ");
        sacado.setCnpjOrCpf(scanner.nextLine());
        sacado.setEndereco(enderecoSacado);

        // Solicitar dados do boleto
        System.out.print("\nNúmero do documento (ex: 123456789): ");
        String numeroDocumento = scanner.nextLine();

        LocalDate dataVencimento = LocalDate.now().plusMonths(1);


        System.out.print("Valor do boleto: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();  // Consumir a nova linha pendente após o nextDouble()

        System.out.print("Nosso número (ex: 1234): ");
        String nossoNumero = scanner.nextLine();

        System.out.print("Tipo de conta (1 - Conta Corrente, 2 - Conta Poupança): ");
        int tipoContaInt = scanner.nextInt();
        ContaEnum tipoConta = tipoContaInt == 1 ? ContaEnum.CONTA_CORRENTE : ContaEnum.CONTA_POUPANCA;
        scanner.nextLine(); // Consumir a nova linha pendente após o nextInt()

        System.out.print("Agência (ex: 261013): ");
        String agencia = scanner.nextLine();

        // Gerar boleto
        Boleto boleto = gerador.gerarBoleto(
                beneficiario,
                sacado,
                numeroDocumento,
                dataVencimento,
                valor,
                nossoNumero,
                tipoConta,
                agencia,
                "261013" // Essa informação está fixa no código original
        );

        // Gerar PDF do boleto
        BoletoPdfGenerator.gerarBoletoPdf(boleto, "boleto" + boleto.getNumeroUnico() + ".pdf");

        scanner.close();  // Fechar o scanner ao final
    }
}
