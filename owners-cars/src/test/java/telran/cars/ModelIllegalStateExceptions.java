package telran.cars;

import telran.cars.api.ServiceExceptionMessages;

public class ModelIllegalStateExceptions extends IllegalStateException {

	public ModelIllegalStateExceptions() {
		super(ServiceExceptionMessages.MODEL_ILLEGAL_STATE);
		
	}

	

}
