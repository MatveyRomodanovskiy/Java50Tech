package telran.cars.api;

public interface ValidationConstants {
	String MISSING_CAR_NUMBER_MESSAGE = "Missing car message";
	String CAR_NUMBER_REGEXP = "(\\d{3}-\\d{2}-\\d{3})|(\\d{2}-\\d{3}-\\d{2})";
	String WRONG_NUMBER_MESSAGE = "Incorrect car number";
	String MISSING_CAR_MODEL_MESSAGE = "Missing car model message";
	String PERSON_WRONG_ID = "Person wrong ID message";
	String MISSING_PERSON_ID = "Missing person ID";
	String WRONG_ID_NUMBER_MIN = "ID less than min";
	long MIN_PERSON_ID_NUMBER = 100000l;
	long MAX_PERSON_ID_NUMBER = 10000000l;
	String WRONG_ID_NUMBER_MAX = "ID greater than MAX";
	String MISSING_NAME = "Name cannot be empty";
	String MISSING_BD = "BD cannot be empty";
	String WRONG_DATE_FORMAT = "Wrong date format";
	String EMAIL_EMPTY = "Empty email";
	String BD_REGEXP = "\\d{4}-\\d{2}-\\d{2}";
	String WRONG_EMAIL = "Wrong email";
}
