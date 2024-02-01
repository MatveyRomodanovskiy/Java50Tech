package telran.cars;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static telran.cars.api.ValidationConstants.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.cars.dto.CarDto;
import telran.cars.dto.PersonDto;
import telran.cars.dto.TradeDealDto;
import telran.cars.exceptions.*;
import telran.cars.exceptions.controller.CarsExceptionsController;
import telran.cars.service.CarsService;

record PersonDtoIdString(String id, String name, String bDate, String email) {
	
};

@WebMvcTest
class CarsControllerTest {
	private static final long PERSON_ID = 1234567l;
	private static final String CAR_NUMBER = "123-00-001";
	private static final String PERSON_NOT_FOUND_MESSAGE = "person not found message";
	private static final String CAR_NOT_FOUND_MESSAGE = "car not found message";
	private static final String CAR_ALREADY_EXISTS_MESSAGE = "car already exists message";
	private static final String PERSON_ALREADY_EXISTS_MESSAGE = "person already exists message";
	private static final String BASE_URL = "http://localhost:8080/cars";
	private static final long WRONG_PERSON_ID = 1l;
	private static final long WRONG_PERSON_MAX_ID = 1000_000_000_000l;
	private static final String WRONG_CAR_NUMBER = "wrong";
	@MockBean
	CarsService carsService;
	@Autowired
	MockMvc mockMvc;
	CarDto carDto = new CarDto(CAR_NUMBER, "Model");
	CarDto carDtoMissingFields = new CarDto(null, null);
	@Autowired
	ObjectMapper mapper;
	PersonDto personDto = new PersonDto(PERSON_ID, "ts", "1970-01-11", "s@sm.se");
	PersonDto testPersonDto = new PersonDto(1234566l, "PersonName", "1970-11-11", "serega@gmail.com");
	PersonDto personDtoUpdated = new PersonDto(10000000l, "ts", "1970-01-11", "s@gmail.com");
	PersonDto personWrongEmail = new PersonDto(PERSON_ID, "ts", "1970-01-11", "s@gmail.");
	PersonDtoIdString personWrongId = new PersonDtoIdString ("abc", "victim", "1970-01-11", "s@gmail.com");
	TradeDealDto dealDto = new TradeDealDto(CAR_NUMBER, 123456l);
	List<CarDto> listCarDtos = new ArrayList<CarDto>();
	TradeDealDto tradeDealWrongId = new TradeDealDto(CAR_NUMBER, -10l);
	TradeDealDto tradeDealAllFieldsMissing = new TradeDealDto(null,null);
	PersonDto personAllFieldsMissing = new PersonDto(null, null, null, null);
	private String[] expectedCarMissingFieldsMessages = {
			MISSING_CAR_MODEL_MESSAGE,
			MISSING_CAR_NUMBER_MESSAGE
	};
	private String[] expectedPersonMissingFieldsMessages = {
		MISSING_BD,
		MISSING_NAME,
		MISSING_PERSON_ID,
		EMAIL_EMPTY
	};
	
	@Test
	void testAddCar() throws Exception {
		when(carsService.addCar(carDto)).thenReturn(carDto);
		String jsonCarDto = mapper.writeValueAsString(carDto); //conversion from carDto object to string JSON
		String actualJSON = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.content(jsonCarDto)).andExpect(status().isOk()).andReturn().getResponse()
		.getContentAsString();
		assertEquals(jsonCarDto, actualJSON );
	}

	@Test
	void testAddPerson() throws Exception {
		when(carsService.addPerson(testPersonDto)).thenReturn(testPersonDto);
		String jsonPersonDto = mapper.writeValueAsString(testPersonDto);
		String actualJson = mockMvc.perform(post(BASE_URL + "/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(jsonPersonDto, actualJson);
	}

	@Test
	void testUpdatePerson() throws Exception{
		when(carsService.updatePerson(personDtoUpdated)).thenReturn(personDtoUpdated);
		String jsonPersonDtoUpdated = mapper.writeValueAsString(personDtoUpdated);
		String actualJson = mockMvc.perform(put(BASE_URL + "/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDtoUpdated)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(jsonPersonDtoUpdated, actualJson);
	}
	

	@Test
	void testPurchase() throws Exception {
		when(carsService.purchase(dealDto)).thenReturn(dealDto);
		String jsonDealDto = mapper.writeValueAsString(dealDto);
		String actualJson = mockMvc.perform(put(BASE_URL + "/trade").contentType(MediaType.APPLICATION_JSON)
				.content(jsonDealDto)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(jsonDealDto, actualJson);
	}

	@Test
	void testDeletePerson() throws Exception{
		when(carsService.deletePerson(PERSON_ID)).thenReturn(personDto);
		String jsonPersonDtoDeleted = mapper.writeValueAsString(personDto);
		String actualJson = mockMvc.perform(delete(BASE_URL + "/person/" + PERSON_ID)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(jsonPersonDtoDeleted, actualJson);
	}

	@Test
	void testDeleteCar() throws Exception{
		when(carsService.deleteCar(CAR_NUMBER)).thenReturn(carDto);
		String jsonCarDtoDeleted = mapper.writeValueAsString(carDto);
		String actualJson = mockMvc.perform(delete(BASE_URL  + "/" + CAR_NUMBER)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(jsonCarDtoDeleted,actualJson);
	}

	@Test
	void testGetOwnerCars() throws Exception {
		listCarDtos.add(carDto);
		when(carsService.getOwnerCars(PERSON_ID)).thenReturn(listCarDtos);
		String jsonExpString = mapper.writeValueAsString(listCarDtos);
		String actualJson = mockMvc.perform(get(BASE_URL + "/person/" + PERSON_ID)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(jsonExpString,actualJson);
	}

	@Test
	void testGetCarOwner()throws Exception{
		when(carsService.getCarOwner(CAR_NUMBER)).thenReturn(personDto);
		String jsonPersonDto = mapper.writeValueAsString(personDto);
		String actualJson = mockMvc.perform(get(BASE_URL + "/" + CAR_NUMBER)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(jsonPersonDto,actualJson);
	}
	
	/*
	 * Alternative flows - service Exception
	 */
	@Test
	void testDeleteNotFoundPerson() throws Exception {
		when(carsService.deletePerson(PERSON_ID)).thenThrow(new NotFoundException(PERSON_NOT_FOUND_MESSAGE));
		String actualJson = mockMvc.perform(delete(BASE_URL + "/person/" + PERSON_ID)).andExpect(status().isNotFound()).andReturn().getResponse()
				.getContentAsString();
//				.getErrorMessage();
		assertEquals(PERSON_NOT_FOUND_MESSAGE, actualJson);
	}
	
	@Test
	void testUpdateNotFoundPerson() throws Exception {
		when(carsService.updatePerson(personDtoUpdated)).thenThrow(new NotFoundException(PERSON_NOT_FOUND_MESSAGE));
		String jsonPersonDtoUpdated = mapper.writeValueAsString(personDtoUpdated);
		String actualJson = mockMvc.perform(put(BASE_URL + "/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDtoUpdated)).andExpect(status().isNotFound()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(PERSON_NOT_FOUND_MESSAGE, actualJson);
	}
	
	@Test
	void testDeleteNotFoundCar() throws Exception{
		when(carsService.deleteCar(CAR_NUMBER)).thenThrow(new NotFoundException(CAR_NOT_FOUND_MESSAGE));
		String actualJsonString = mockMvc.perform(delete(BASE_URL  + "/" +  CAR_NUMBER)).andExpect(status().isNotFound()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(CAR_NOT_FOUND_MESSAGE,actualJsonString);
	}
	
	void testAddAlreadyExistsCar() throws Exception {
		when(carsService.addCar(carDto)).thenThrow(new IllegalStateException(CAR_ALREADY_EXISTS_MESSAGE));
		String jsonCarDto = mapper.writeValueAsString(carDto); //conversion from carDto object to string JSON
		String actualJSON = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.content(jsonCarDto)).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(CAR_ALREADY_EXISTS_MESSAGE, actualJSON );
	}
	
	@Test
	void testAddPersonAlreadyExists() throws Exception{
		when(carsService.addPerson(personDto)).thenThrow(new IllegalStateException(PERSON_ALREADY_EXISTS_MESSAGE));
		String jsonPersonDto = mapper.writeValueAsString(personDto);
		String actualJson = mockMvc.perform(post(BASE_URL + "/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(PERSON_ALREADY_EXISTS_MESSAGE, actualJson);
	}
	
	/*
	 * Alternative flows - validation
	 */
	
	@Test
	void addPersonWrongEmailTest() throws Exception {
		String jsonPersonDto = mapper.writeValueAsString(personWrongEmail);
		String actualJson = mockMvc.perform(post(BASE_URL + "/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(WRONG_EMAIL, actualJson);
	}
	
	@Test
	void deletePersonWrongMinIDTest() throws Exception {
		String actualJson = mockMvc.perform(delete(BASE_URL + "/person/" + WRONG_PERSON_ID)).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(WRONG_ID_NUMBER_MIN, actualJson);
	}
	
	void deletePersonWrongMaxIDTest() throws Exception {
		String actualJson = mockMvc.perform(delete(BASE_URL + "/person/" + WRONG_PERSON_MAX_ID)).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(WRONG_ID_NUMBER_MAX, actualJson);
	}
	
	
	@Test
	void deleteCarNumberWrongIDTest() throws Exception {
		String actualJsonString = mockMvc.perform(delete(BASE_URL  + "/" +  WRONG_CAR_NUMBER)).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(WRONG_NUMBER_MESSAGE,actualJsonString);
	}
	
	@Test
	void addPersonWrongIdTypeTest() throws Exception {
		wrongPersonDataRequest(personWrongId, CarsExceptionsController.JSON_TYPE_MISMATCH_MESSAGE);
	}
	
	private void wrongPersonDataRequest(Object personDtoWrongData, String expectedMessage) throws  Exception {
		String jsonPersonDto = mapper.writeValueAsString(personDtoWrongData); //conversion from carDto object to string JSON
		String actualJson = mockMvc.perform(post(BASE_URL + "/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(expectedMessage, actualJson);
	}
	
	@Test
	void addCarMissingFields() throws Exception {
		String jsonCarDto = mapper.writeValueAsString(carDtoMissingFields); //conversion from carDto object to string JSON
		String response = mockMvc.perform(post("http://localhost:8080/cars").contentType(MediaType.APPLICATION_JSON)
				.content(jsonCarDto)).andExpect(status().isBadRequest()).andReturn().getResponse()
		.getContentAsString();
		allFieldsMissingTest(expectedCarMissingFieldsMessages , response);
	}
	@Test
	void addPersonMissingFields() throws Exception {
		String jsonPersonDto = mapper.writeValueAsString(personAllFieldsMissing); //conversion from carDto object to string JSON
		String response = mockMvc.perform(post("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		allFieldsMissingTest(expectedPersonMissingFieldsMessages , response);
	}
	private void allFieldsMissingTest(String [] expectedMessages, String response) {
		Arrays.sort(expectedMessages);
		String [] actualMessages = response.split(";");
		Arrays.sort(actualMessages);
		assertArrayEquals(expectedMessages, actualMessages);
	}
}
