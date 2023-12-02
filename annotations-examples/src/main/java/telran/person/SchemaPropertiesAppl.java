package telran.person;

import java.util.ArrayList;
import java.util.List;

import telran.reflect.SchemaProperties;

public class SchemaPropertiesAppl {

	public static void main(String[] args) throws Exception {
		List <Person> persons = new ArrayList<Person>();
		persons.add( new PersonNormal());
		persons.add( new NoIdPerson()); 
		persons.add( new TwoIdPerson());
		for(Person p: persons) {
			System.out.println("Test class: " + p.getClass().getName());
			SchemaProperties.displayFieldProperties(p);
			System.out.println("=============");
		}
	}

}
