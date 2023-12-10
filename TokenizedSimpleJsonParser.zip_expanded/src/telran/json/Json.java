
package telran.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is simple JSON Parser, providing recursive parsing of JSONs representing objects and arrays.
 * The supposed format of all values are quoted strings, inheritance and generic types are not covered.
 * @author Daniel Z.
 */
public class Json{
	private static final String DEFAULT_DATE_FORMATTER = "dd/MM/yyyy";
	static Map<Class<?>, Function<String,?>> parsersByType = new IdentityHashMap<>();
	static String dateFormatString = DEFAULT_DATE_FORMATTER;
	static {
        parsersByType.put(byte.class, Byte::parseByte);
        parsersByType.put(Byte.class, Byte::parseByte);
        parsersByType.put(short.class, Short::parseShort);
        parsersByType.put(Short.class, Short::parseShort);
        parsersByType.put(int.class, Integer::parseInt);
        parsersByType.put(Integer.class, Integer::parseInt);
        parsersByType.put(long.class, Long::parseLong);
        parsersByType.put(Long.class, Long::parseLong);

        parsersByType.put(float.class, Float::parseFloat);
        parsersByType.put(Float.class,  Float::parseFloat);
        parsersByType.put(double.class, Double::parseDouble);
        parsersByType.put(Double.class, Double::parseDouble);
        
        parsersByType.put(boolean.class, Boolean::parseBoolean);
        parsersByType.put(Boolean.class,  Boolean::parseBoolean);
        
		parsersByType.put(String.class, Function.identity());
		
        parsersByType.put(LocalDate.class, v -> LocalDate.parse(v, DateTimeFormatter.ofPattern(dateFormatString)));
	}
	
	public <T> T parse(String json, Class<T> type) throws Exception{
		SimpleJsonTokenizer  tokenizer = new SimpleJsonTokenizer(json);
		T res =  parseValue(tokenizer, type);
		if (tokenizer.hasNext()) {
			throw new RuntimeException("Json has excessive fields");
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T parseValue(SimpleJsonTokenizer tokenizer, Class<T> type) throws Exception{
		Function<String,?> parser = parsersByType.get(type);
		if (parser != null) {
			return (T)parser.apply(stripQuotes(tokenizer.next()));
		}
		if(type.isArray()) {
			return parseArray(tokenizer, type);
		}
		return parseObject(tokenizer, type);
	}
		
	@SuppressWarnings("unchecked")
	private <T> T parseArray(SimpleJsonTokenizer tokenizer, Class<T> arrType) throws Exception {
		List<Object> list = new ArrayList<>();
		Class<?> eltType = arrType.getComponentType();
		assertNextToken(tokenizer, "[");
		while(!tokenizer.peek().equals("]")) {		
            if (list.size()>0) {					// non-first element
            	assertNextToken(tokenizer, ",");
            }
			Object elt = parseValue(tokenizer, eltType);
			list.add(elt);
		}
		tokenizer.next(); // extract ]	
		int eltCount = list.size();
		Object actualArray = Array.newInstance(eltType, eltCount); // creation of generic array using reflection
		return (T) list.toArray((Object[])actualArray);
	}
	
	private <T> T parseObject(SimpleJsonTokenizer tokenizer, Class<T> objType) throws Exception{
		T res = objType.getDeclaredConstructor().newInstance();
		assertNextToken(tokenizer, "{");
		int parsedFields = 0;
		HashMap<String, String> annotatedFieldsHashMap = getAnnotatedFieldsAsJsonField(objType);
		while(!tokenizer.peek().equals("}")) {
			if (parsedFields > 0) {
				assertNextToken(tokenizer, ",");
			}
			Field field = getField(objType, annotatedFieldsHashMap, tokenizer.next());
			if(field.isAnnotationPresent(JsonFormat.class)){
            		setDateField(tokenizer, res, field);
               } else {
            	   	setField(tokenizer, res, field);
               }
            parsedFields++;
		}
		tokenizer.next();  // extract }	
		return res;
	}

	private <T> Field getField(Class<T> objType, HashMap<String, String> annotatedFieldsHashMap, String jsonFieldName)
			throws NoSuchFieldException {
		Field field;
		if(annotatedFieldsHashMap.get(stripQuotes(jsonFieldName)) != null) {
			String key = annotatedFieldsHashMap.get(stripQuotes(jsonFieldName));
			field =objType.getDeclaredField(key);
		} else {
			field =objType.getDeclaredField(stripQuotes(jsonFieldName));
		}
		return field;
	}

	private <T> HashMap<String, String> getAnnotatedFieldsAsJsonField(Class<T> objType) {
		Field[] fields = objType.getDeclaredFields();
		HashMap<String, String> annotatedFieldsHashMap = new HashMap<String, String>();
		annotatedFieldsHashMap = (HashMap<String, String>) Arrays.stream(fields)
				.filter(f -> f.isAnnotationPresent(JsonField.class))
				.collect(Collectors.toMap(f -> f.getAnnotation(JsonField.class).value().toString(), f -> f.getName().toString()));
		return annotatedFieldsHashMap;
	}

	private <T> void setDateField(SimpleJsonTokenizer tokenizer, T res, Field field)
			throws Exception, IllegalAccessException {
		dateFormatString =(field.getAnnotation(JsonFormat.class).value());
		setField(tokenizer, res, field);
		dateFormatString = DEFAULT_DATE_FORMATTER;
	}

	private <T> void setField(SimpleJsonTokenizer tokenizer, T res, Field field)
			throws Exception, IllegalAccessException {
		assertNextToken(tokenizer, ":");            
		Object value = parseValue(tokenizer, field.getType());
		field.setAccessible(true);
		field.set(res, value);
	}
	
	private void assertNextToken(SimpleJsonTokenizer tokenizer, String expectedToken) {
		String nextToken = tokenizer.next();
		if (! nextToken.equals(expectedToken)) {
			throw new RuntimeException("Unexpected token " + nextToken + " instead of " + expectedToken);
		}
	}
	
	private String stripQuotes(String quotedString) {
		if (!quotedString.matches("\".*\"")) {
			throw new RuntimeException("Invalid token instead of quoted string: " + quotedString);
		}
		return quotedString.substring(1, quotedString.length()-1);
	}	
}


