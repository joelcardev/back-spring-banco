package br.com.banco.handles;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.beanvalidation.IntegrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import br.com.banco.dto.CampoErroDto;
import br.com.banco.dto.ErroDto;
import br.com.banco.enums.TipoCodigoErro;
import br.com.banco.exceptions.ApiValidationException;
import br.com.banco.exceptions.NegocioException;
import br.com.banco.exceptions.RegistroNaoEncontradoException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErroDto> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException ex) {
		ErroDto erro = new ErroDto(TipoCodigoErro.VALOR_INVALIDO, "Requisição inválida. Método não suportado.");
		return ResponseEntity.badRequest().body(erro);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErroDto> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		String message = extractDetailedMessage(ex);
		ErroDto erro = new ErroDto(TipoCodigoErro.VALOR_INVALIDO, String.format(
				"Requisição inválida. Verifique se o JSON informado está correto. Detalhes técnicos: %s", message));
		return ResponseEntity.badRequest().body(erro);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErroDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<CampoErroDto> erros = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			erros.add(new CampoErroDto(fieldName, errorMessage));
		});
		ErroDto erro = new ErroDto(TipoCodigoErro.VALOR_INVALIDO, erros);
		return ResponseEntity.badRequest().body(erro);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErroDto> handleValidationConstraint(ConstraintViolationException ex) {
		List<CampoErroDto> erros = new ArrayList<>();
		ex.getConstraintViolations().forEach(error -> {
			String errorMessage = error.getMessage();
			Path propertyPath = error.getPropertyPath();
			String property = propertyPath.toString();
			erros.add(new CampoErroDto(property, errorMessage));
		});
		ErroDto erro = new ErroDto(TipoCodigoErro.VALOR_INVALIDO, erros);
		return ResponseEntity.badRequest().body(erro);
	}

	@ExceptionHandler(RegistroNaoEncontradoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErroDto> handleRegistroNaoEncontradoException(Exception ex, WebRequest request) {
		ErroDto erro = new ErroDto(TipoCodigoErro.DADO_NAO_ENCONTRADO, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}

	@ExceptionHandler(ApiValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErroDto> handleNegocioException(ApiValidationException ex) {
		ErroDto erro = new ErroDto(TipoCodigoErro.VIOLACAO_REGRA_DE_NEGOCIO, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}

	@ExceptionHandler(NegocioException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErroDto> handleNegocioException(NegocioException ex) {
		ErroDto erro = new ErroDto(TipoCodigoErro.VIOLACAO_REGRA_DE_NEGOCIO, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}

	@ExceptionHandler(IntegrationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErroDto> handleNegocioException(IntegrationException ex) {
		ErroDto erro = new ErroDto(TipoCodigoErro.VIOLACAO_REGRA_DE_NEGOCIO, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErroDto> handleGenericError(Exception ex) {
		ErroDto erro = new ErroDto(TipoCodigoErro.ERRO_INTERNO, "Erro interno do servidor");
		log.error(ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
	}

	private String extractDetailedMessage(Throwable e) {
		final String message = e.getMessage();
		if (message == null) {
			return "";
		}
		final int tailIndex = StringUtils.indexOf(message, "; nested exception is");
		if (tailIndex == -1) {
			return message;
		}
		return StringUtils.left(message, tailIndex);
	}
}