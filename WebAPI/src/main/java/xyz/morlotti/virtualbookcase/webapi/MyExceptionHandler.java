package xyz.morlotti.virtualbookcase.webapi;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import xyz.morlotti.virtualbookcase.webapi.exceptions.*;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(APIInvalidValueException.class)
	public ResponseEntity<String> springHandleAPIInvalidValueException(APIInvalidValueException e) throws IOException
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(APINotAuthorizedException.class)
	public ResponseEntity<String> springHandleAPINotAuthorizedException(APINotAuthorizedException e) throws IOException
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(APINotDeletedException.class)
	public ResponseEntity<String> springHandleAPINotDeletedException(APINotDeletedException e) throws IOException
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(APINotFoundException.class)
	public ResponseEntity<String> springHandleAPINotFoundException(APINotFoundException e) throws IOException
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(APINotModifiedException.class)
	public ResponseEntity<String> springHandleAPINotModifiedException(APINotModifiedException e) throws IOException
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<String> springHandleUsernameNotFound(UsernameNotFoundException e) throws IOException
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> springHandleBadCredentialsException(BadCredentialsException e) throws IOException
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<String> springIntegrityConstraintViolation(SQLIntegrityConstraintViolationException e) throws IOException
	{
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
