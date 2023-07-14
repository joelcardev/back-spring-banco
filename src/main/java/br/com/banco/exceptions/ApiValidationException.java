package br.com.banco.exceptions;

import java.util.List;

import br.com.banco.dto.CampoErroDto;

public class ApiValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<CampoErroDto> camposErroDto;

	public ApiValidationException(String message, Throwable cause, List<CampoErroDto> camposErroDto) {
		super(message, cause);
		this.camposErroDto = camposErroDto;
	}

	public ApiValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiValidationException(String message, List<CampoErroDto> camposErroDto) {
		super(message);
		this.camposErroDto = camposErroDto;
	}

	public ApiValidationException(String message) {
		super(message);
	}

	public ApiValidationException(List<CampoErroDto> camposErroDto) {
		super();
		this.camposErroDto = camposErroDto;
	}

	public ApiValidationException() {
		super();
	}

	public List<CampoErroDto> getCamposErroDto() {
		return camposErroDto;
	}

	public void setCamposErroDto(List<CampoErroDto> camposErroDto) {
		this.camposErroDto = camposErroDto;
	}

}
