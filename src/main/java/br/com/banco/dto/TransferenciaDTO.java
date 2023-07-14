package br.com.banco.dto;

import java.time.OffsetDateTime;

import br.com.banco.models.Transferencia;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferenciaDTO {

    private Long id;
    private OffsetDateTime dataTransferencia;
    private Double valor;
    private String tipo;
    private String nomeOperadorTransacao;
    private Long conta;

    public TransferenciaDTO() {
    }

    public TransferenciaDTO(Transferencia transferencia) {
        this.id = transferencia.getId();
        this.dataTransferencia = transferencia.getDataTransferencia();
        this.valor = transferencia.getValor();
        this.tipo = transferencia.getTipo();
        this.nomeOperadorTransacao = transferencia.getNomeOperadorTransacao();
        this.conta = transferencia.getConta().getIdConta();
    }

}
