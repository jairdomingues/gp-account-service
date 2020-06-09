package br.com.greenpay.core.system.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

	@ExceptionHandler(CustomGenericNotFoundException.class)
	public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {
		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(dataFormatada());
		errors.setError(ex.getMessage());
		errors.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(CustomGenericUnauthorizedException.class)
	public ResponseEntity<CustomErrorResponse> customHandleUnauthorized(Exception ex, WebRequest request) {
		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(dataFormatada());
		errors.setError(ex.getMessage());
		errors.setStatus(HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);

	}

	private String dataFormatada() {
		// Obtém LocalDateTime de agora
		LocalDateTime agora = LocalDateTime.now();
		System.out.println("LocalDateTime antes de formatar: " + agora);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String agoraFormatado = agora.format(formatter);
		System.out.println("LocalDateTime depois de formatar: " + agoraFormatado);
		return agoraFormatado;
	}
}
