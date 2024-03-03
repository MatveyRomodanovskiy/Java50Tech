package telran.cars.exceptions;

import telran.cars.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class ModelNotFoundExceptions extends NotFoundException {

	public ModelNotFoundExceptions() {
		super(ServiceExceptionMessages.MODEL_NOT_FOUND);
	}

}
