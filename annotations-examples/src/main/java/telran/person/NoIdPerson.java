package telran.person;

import telran.reflect.Index;

public class NoIdPerson extends Person{
	
		Long idLong;

		@Index
		String firstName;
		
		@Index
		String lastName;
		
		@Index
		String hobby;
		
	}

