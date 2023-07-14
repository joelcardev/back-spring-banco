package br.com.banco.dto;


import java.io.Serializable;
import java.util.List;

import br.com.banco.enums.TipoCodigoErro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErroDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private TipoCodigoErro codigo;

    private String mensagem;

    private List<CampoErroDto> mensagens;

    public ErroDto(TipoCodigoErro codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public ErroDto(TipoCodigoErro codigo, List<CampoErroDto> mensagens) {
        this.codigo = codigo;
        this.mensagens = mensagens;
    }
}
