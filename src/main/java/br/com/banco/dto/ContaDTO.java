package br.com.banco.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaDTO {

    private Long idConta;
    private String nomeResponsavel;


    public ContaDTO() {
    }


    public ContaDTO(Long idConta, String nomeResponsavel) {
        this.idConta = idConta;
        this.nomeResponsavel = nomeResponsavel;
    }

}
