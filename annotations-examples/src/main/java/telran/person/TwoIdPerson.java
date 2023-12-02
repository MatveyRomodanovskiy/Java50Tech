package telran.person;

import telran.reflect.Id;
import telran.reflect.Index;

public class TwoIdPerson extends Person{
	@Id
	Long idLong;

	@Id
	String firstName;
	
	@Id
	String lastName;
	
	@Index
	String hobby;
}
