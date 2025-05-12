package com.ifba.model.entidade;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Entidade {

    private String nome;
    private String razaoSocial;
    private String cnpjOrCpf;
    private Endereco endereco;
}
