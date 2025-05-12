package com.ifba.model.boleto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContaBancaria {
    private String agencia;
    private String numero;
    private ContaEnum tipoConta;
    private String carteira;
}
