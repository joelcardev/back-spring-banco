package br.com.banco.exceptions;

import java.util.List;

import br.com.banco.dto.CampoErroDto;

public class RegistroNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<CampoErroDto> camposErroDto;

	public RegistroNaoEncontradoException(String message, Throwable cause, List<CampoErroDto> camposErroDto) {
		super(message, cause);
		this.camposErroDto = camposErroDto;
	}

	public RegistroNaoEncontradoException(String message, Throwable cause) {
		super(message, cause);
	}

	public RegistroNaoEncontradoException(String message, List<CampoErroDto> camposErroDto) {
		super(message);
		this.camposErroDto = camposErroDto;
	}

	public RegistroNaoEncontradoException(String message) {
		super(message);
	}

	public RegistroNaoEncontradoException(List<CampoErroDto> camposErroDto) {
		super();
		this.camposErroDto = camposErroDto;
	}

	public RegistroNaoEncontradoException() {
		super();
	}

	public List<CampoErroDto> getCamposErroDto() {
		return camposErroDto;
	}

	public void setCamposErroDto(List<CampoErroDto> camposErroDto) {
		this.camposErroDto = camposErroDto;
	}

}
