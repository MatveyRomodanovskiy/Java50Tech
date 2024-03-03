package telran.cars.api;

import java.time.LocalDate;

public interface ValidationConstants {
	String MISSING_CAR_NUMBER_MESSAGE = "Missing car number";
	String CAR_NUMBER_REGEXP = "(\\d{3}-\\d{2}-\\d{3})|(\\d{2}-\\d{3}-\\d{2})";
	String WRONG_CAR_NUMBER_MESSAGE = "Incorrect Car Number";
	String MISSING_CAR_MODEL_MESSAGE = "Missing car model";
	String MISSING_PERSON_ID_MESSAGE = "Missing person ID";
	long MIN_PERSON_ID_VALUE = 100000l;
	long MAX_PERSON_ID_VALUE = 999999l;
	int MIN_MODEL_YEAR = 2000;
	
	String WRONG_MIN_PERSON_ID_VALUE = "Person ID must be greater or equal " + MIN_PERSON_ID_VALUE;
	String WRONG_MAX_PERSON_ID_VALUE = "Person ID must be less or equal " + MAX_PERSON_ID_VALUE;
	String MISSING_PERSON_NAME_MESSAGE = "Missing person name";
	String MISSING_BIRTH_DATE_MESSAGE = "Missing person's birth date";
	String MISSING_MODEL_NAME_MESSAGE = "Missing model name";
	String MISSING_MODEL_YEAR_MESSAGE = "Missing model year";
	String MISSING_COMPANY_MESSAGE = "Missing company name";
	String BIRTH_DATE_REGEXP = "\\d{4}-\\d{2}-\\d{2}";
	String WRONG_DATE_FORMAT = "Wrong date format, must be YYYY-MM-dd";
	String MISSING_PERSON_EMAIL = "Missing email address";
	String WRONG_EMAIL_FORMAT = "Wrong email format";
	String WRONG_MIN_YEAR = "year cannot be less than " + MIN_MODEL_YEAR;
	
}
