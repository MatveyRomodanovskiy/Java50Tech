package telran.cars.exceptions;

import telran.cars.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class PersonNotFoundExeption extends NotFoundException {

	public PersonNotFoundExeption() {
		super(ServiceExceptionMessages.PERSON_NOT_FOUND);
		
	}

}
