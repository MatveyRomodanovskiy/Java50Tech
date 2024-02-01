package telran.cars.exceptions.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;
import telran.cars.exceptions.NotFoundException;


@ControllerAdvice
@Slf4j
public class CarsExceptionsController {
	public static String JSON_TYPE_MISMATCH_MESSAGE = "JSON contains field with type mismatch";	


	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<String> notFoundHandlerEntity (NotFoundException e){
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<String> badRequestHandlerEntity (IllegalStateException e){
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler (MethodArgumentNotValidException.class)
	ResponseEntity<String> methodValidationExceptionHandler (MethodArgumentNotValidException e){
		return new ResponseEntity<String>( e.getAllErrors().stream()
				.map(er -> er.getDefaultMessage())
				.collect(Collectors.joining(";")), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler (HandlerMethodValidationException.class)
	ResponseEntity<String> validHandlerMethodValidationException (HandlerMethodValidationException e){
		return new ResponseEntity<String>( e.getAllErrors().stream()
				.map(er -> er.getDefaultMessage())
				.collect(Collectors.joining(";")), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	ResponseEntity<String> methodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
		String message = JSON_TYPE_MISMATCH_MESSAGE;
		return returnResponse(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<String> jsonFieldTypeMismatchException(HttpMessageNotReadableException e) {
		String message = JSON_TYPE_MISMATCH_MESSAGE;
		
		return returnResponse(message, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> returnResponse(String message, HttpStatus status) {
		log.error(message);
		return new ResponseEntity<String>(message, status);
	}
}
