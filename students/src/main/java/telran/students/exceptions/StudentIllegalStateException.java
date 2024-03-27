package telran.students.exceptions;

import org.springframework.stereotype.Service;

import telran.students.service.ServiceErrorMessages;

@SuppressWarnings("serial")
public class StudentIllegalStateException extends IllegalStateException {
	public StudentIllegalStateException() {
		super(ServiceErrorMessages.STUDENT_ALREADY_EXISTS);
	}
}
