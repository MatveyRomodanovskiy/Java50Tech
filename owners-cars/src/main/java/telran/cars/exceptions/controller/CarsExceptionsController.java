package telran.cars.exceptions.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import telran.cars.exceptions.NotFoundException;

@ControllerAdvice
public class CarsExceptionsController {

	
	
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
}
