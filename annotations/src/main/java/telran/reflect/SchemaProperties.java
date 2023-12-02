package telran.reflect;

import java.lang.reflect.Field;

public class SchemaProperties {
	public static void displayFieldProperties (Object obj) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		Field idField = null;
		try {
			for (Field f : fields) {
				if (f.isAnnotationPresent(Id.class)) {
					if (idField != null) {
						throw new IllegalStateException("Field Id must be only one");
					}
					idField = f;
				}
				if (f.isAnnotationPresent(Index.class)) {
					System.out.println("Field " + f.getName()+ " is INDEX");
				}
			} 
			if (idField == null) {
				throw new IllegalStateException("No field ID found");
			}
			System.out.println("Field " + idField.getName()+ " is ID");
		} catch (IllegalStateException error) {
			System.out.println(error.getMessage());
		}
	}
}
