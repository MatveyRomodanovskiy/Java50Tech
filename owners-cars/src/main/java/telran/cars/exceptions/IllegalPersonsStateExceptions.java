package telran.cars.exceptions;

import telran.cars.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class IllegalPersonsStateExceptions extends IllegalStateException {
	public IllegalPersonsStateExceptions() {
		super(ServiceExceptionMessages.CAR_ALREADY_EXISTS);
	}
	
}
