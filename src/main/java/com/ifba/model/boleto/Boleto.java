package com.ifba.model.boleto;

import com.ifba.model.entidade.Entidade;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Boleto {
    private Entidade beneficiario;
    private Entidade sacado;
    private Titulo titulo = new Titulo();
    private ContaBancaria conta;
    private Banco banco;
    private String linhaDigitavel;
    private String codigoBarras;
    private String numeroUnico = (int)(Math.random() * 99999) + 1 + "";


    public void exibirResumo() {
        System.out.println("Boleto do banco: " + banco);
        System.out.println("Beneficiário: " + beneficiario);
        System.out.println("Sacado: " + sacado);
        System.out.println("Vencimento: " + titulo);
        System.out.println("Valor: R$ " + titulo.getDataVencimento());
        System.out.println("Linha Digitável: " + linhaDigitavel);
        System.out.println("Código de Barras: " + codigoBarras);
    }
}