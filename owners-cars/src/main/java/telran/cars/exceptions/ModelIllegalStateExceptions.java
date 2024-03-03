package telran.cars.exceptions;

import telran.cars.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class ModelIllegalStateExceptions extends IllegalStateException {

	public ModelIllegalStateExceptions() {
		super(ServiceExceptionMessages.MODEL_ILLEGAL_STATE);
		
	}

	

}
