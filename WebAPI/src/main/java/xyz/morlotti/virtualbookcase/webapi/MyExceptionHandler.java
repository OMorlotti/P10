package xyz.morlotti.virtualbookcase.webapi;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(UsernameNotFoundException.class)
	public void springHandleUsernameNotFound(HttpServletResponse response) throws IOException
	{
		response.sendError(HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public void springIntegrityConstraintViolation(HttpServletResponse response) throws IOException
	{
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		Map<String, String> errors = e.getBindingResult().getAllErrors().stream().collect(Collectors.toMap(
				x -> ((FieldError) x).    getField     (),
				x -> ((FieldError) x).getDefaultMessage()
		));

		return new ResponseEntity(errors, headers, status);
	}
}
