package telran.person;
import telran.reflect.Id;
import telran.reflect.Index;

public class PersonNormal extends Person{	
	@Id
	Long idLong;

	@Index
	String firstName;
	
	@Index
	String lastName;
	
	@Index
	String hobby;
	
}
