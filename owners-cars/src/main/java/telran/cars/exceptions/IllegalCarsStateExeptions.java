package telran.cars.exceptions;

import telran.cars.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class IllegalCarsStateExeptions extends IllegalStateException {
	public IllegalCarsStateExeptions() {
		super(ServiceExceptionMessages.PERSON_ALREADY_EXISTS);
	}
	
}
