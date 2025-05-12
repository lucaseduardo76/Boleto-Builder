package com.ifba.model.boleto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Titulo {
    private String numeroDocumento;
    private LocalDate dataVencimento;
    private Double valor;
}
