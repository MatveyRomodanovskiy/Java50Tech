package telran.exceptions;

import telran.students.service.ServiceErrorMessages;

@SuppressWarnings("serial")
public class NotFoundException  extends RuntimeException{

	public NotFoundException() {
		super(ServiceErrorMessages.STUDENT_NOT_FOUND);
	}
}
