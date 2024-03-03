package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import telran.cars.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.jdbc.Sql;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import telran.cars.dto.*;
import telran.cars.exceptions.*;
import telran.cars.repo.*;
import telran.cars.service.CarsService;
import telran.cars.service.model.ModelYear;
import telran.cars.service.model.TradeDeal;


@SpringBootTest
@Sql(scripts = {"classpath:test_data.sql"})
class CarsServiceTest {
	private static final String MODEL1 = "model1";
	private static final String MODEL2 = "model2";
	private static final String MODEL3 = "model3";
	private static final String MODEL4 = "model4";
	private static final String CAR_NUMBER_1 = "111-11-111";
	private static final String CAR_NUMBER_2 = "222-11-111";
	private static final  String CAR_NUMBER_3 = "333-11-111";
	private static final  String CAR_NUMBER_4 = "444-44-444";
	private static final  String CAR_NUMBER_5 = "555-55-555";
	private static final Long PERSON_ID_1 = 123l;
	private static final String NAME1 = "name1";
	private static final String BIRTH_DATE_1 = "2000-10-10";
	private static final String EMAIL1 = "name1@gmail.com";
	private static final Long PERSON_ID_2 = 124l;
	private static final String NAME2 = "name2";
	private static final String BIRTH_DATE_2 = "2000-10-10";
	private static final String EMAIL2 = "name2@gmail.com";
	private static final Long PERSON_ID_NOT_EXISTS = 1111111111L;
	
	private static final  String NEW_EMAIL = "name1@tel-ran.co.il";
	private static final  String DATE_TRADE_DEAL_1 = "2024-01-01";
	@Autowired
	CarOwnerRepo carOwnerRepo;
	@Autowired
	CarRepo carRepo;
	@Autowired
	TradeDealRepo tradeDealRepo;
	
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL1, 2020, "red", 1000, CarState.GOOD);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL1, 2020, "silver", 10000, CarState.OLD);
	CarDto car3 = new CarDto(CAR_NUMBER_3, MODEL4, 2023, "white", 0, CarState.NEW);
	CarDto car4 = new CarDto(CAR_NUMBER_4, MODEL4, 2023, "black", 0, CarState.NEW);
	CarDto car5 = new CarDto(CAR_NUMBER_5, MODEL3, 2021, "silver", 5000, CarState.MIDDLE);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXISTS, NAME1, BIRTH_DATE_1, EMAIL1);
	PersonDto personDto1 = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE_1, EMAIL1);
	PersonDto personDto2 = new PersonDto(PERSON_ID_2, NAME2, BIRTH_DATE_2, EMAIL2);
	@Autowired
	CarsService carsService;
	
	
	@Test
	void scriptTest() {
		assertThrowsExactly(IllegalPersonsStateExceptions.class,
				()->carsService.addPerson(personDto1));
		
		
	}
	
	@Test
	@Disabled
	void testAddPerson() {
		assertEquals(personDto, carsService.addPerson(personDto));
		assertThrowsExactly(IllegalPersonsStateExceptions.class,
				()->carsService.addPerson(personDto));
	}

	@Test
	@Disabled
	void testAddCar() {
		assertEquals(car4, carsService.addCar(car4));
		CarDto carNoModel = new CarDto("11111111111", MODEL1, 2018, "green", 100000, CarState.OLD);
		assertThrowsExactly(IllegalCarsStateExeptions.class,
				()->carsService.addCar(car4));
		assertThrowsExactly(ModelNotFoundExceptions.class, () -> carsService.addCar(carNoModel));
		
	}
	@Test
	@Disabled
	void testAddModel() {
		ModelDto modelDtoNew = new ModelDto(MODEL4, 2024, "Company1", 100, 2000);
		assertEquals(modelDtoNew, carsService.addModel(modelDtoNew));
		assertThrowsExactly(ModelIllegalStateExceptions.class, () -> carsService.addModel(modelDtoNew));
	}

	@Test
	void testUpdatePerson() {
		PersonDto personUpdated = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE_1, NEW_EMAIL);
		assertEquals(personUpdated, carsService.updatePerson(personUpdated));
		assertEquals(NEW_EMAIL, carOwnerRepo.findById(PERSON_ID_1).get().getEmail());
		assertThrowsExactly(PersonNotFoundExeption.class,
				() -> carsService.updatePerson(personDto));
	}

	@Test
	@Disabled
	void testDeletePerson() {
		
		assertEquals(personDto1, carsService.deletePerson(PERSON_ID_1));
		assertThrowsExactly(PersonNotFoundExeption.class, () -> carsService.deletePerson(PERSON_ID_1));
		
	}

	@Test
	@Disabled
	void testDeleteCar() {
		
		assertEquals(car1, carsService.deleteCar(CAR_NUMBER_1));
		assertThrowsExactly(CarNotFoundException.class, () -> carsService.deleteCar(CAR_NUMBER_1));
		
	}

	@Test
	@Disabled
	void testPurchaseNewCarOwner() {
		int countDeals = (int)tradeDealRepo.count(); 
		TradeDealDto tradeDealDto = new TradeDealDto(CAR_NUMBER_1, PERSON_ID_2, DATE_TRADE_DEAL_1);
		assertEquals(tradeDealDto, carsService.purchase(tradeDealDto));
		assertEquals(PERSON_ID_2, carRepo.findById(CAR_NUMBER_1).get().getCarOwner().getId());
		TradeDeal tradeDeal = tradeDealRepo.findAll().get(countDeals);
		assertEquals(CAR_NUMBER_1, tradeDeal.getCar().getNumber());
		assertEquals(PERSON_ID_2, tradeDeal.getCarOwner().getId());
		assertEquals(DATE_TRADE_DEAL_1, tradeDeal.getDate().toString());
		
		
		
	}
	@Test
	@Disabled
	void testPurchaseNotFound() {
		TradeDealDto tradeDealCarNotFound = new TradeDealDto(CAR_NUMBER_4, PERSON_ID_1, DATE_TRADE_DEAL_1);
		TradeDealDto tradeDealOwnerNotFound = new TradeDealDto(CAR_NUMBER_1,
				PERSON_ID_NOT_EXISTS, DATE_TRADE_DEAL_1);
		assertThrowsExactly(PersonNotFoundExeption.class, () -> carsService.purchase(tradeDealOwnerNotFound));
		assertThrowsExactly(CarNotFoundException.class, () -> carsService.purchase(tradeDealCarNotFound));
		
	}
	@Test
	@Disabled
	void testPurchaseNoCarOwner() {
		int countDeals = (int)tradeDealRepo.count(); 
		TradeDealDto tradeDealDto = new TradeDealDto(CAR_NUMBER_1,null, DATE_TRADE_DEAL_1);
		assertEquals(tradeDealDto, carsService.purchase(tradeDealDto));
		assertNull(carRepo.findById(CAR_NUMBER_1).get().getCarOwner());
		TradeDeal tradeDeal = tradeDealRepo.findAll().get(countDeals);
		assertEquals(CAR_NUMBER_1, tradeDeal.getCar().getNumber());
		assertNull(tradeDeal.getCarOwner());
		assertEquals(DATE_TRADE_DEAL_1, tradeDeal.getDate().toString());
	}
	@Test
	@Disabled
	void testPurchaseSameOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1,PERSON_ID_1, null);
		assertThrowsExactly(TradeDealIllegalStateException.class,
				() -> carsService.purchase(tradeDeal));
	}

	@Test
	@Disabled
	void testGetOwnerCars() {
		List<CarDto> cars = carsService.getOwnerCars(PERSON_ID_1);
		assertEquals(1, cars.size());
		assertEquals(car1, cars.get(0));
		assertThrowsExactly(NotFoundException.class,
				() -> carsService.getOwnerCars(PERSON_ID_NOT_EXISTS));
	}

	@Test
	@Disabled
	void testGetCarOwner() {
		PersonDto ownerActual = carsService.getCarOwner(CAR_NUMBER_1);
		assertEquals(personDto1, ownerActual);
		assertThrowsExactly(NotFoundException.class, () -> carsService.getCarOwner(CAR_NUMBER_3));
	}
	@Test
	@Disabled
	void testMostPopularModels() {
		carsService.addCar(car3);
		carsService.addCar(car4);
		carsService.addCar(car5);
		carsService.purchase(new TradeDealDto(CAR_NUMBER_3, PERSON_ID_1, null));
		carsService.purchase(new TradeDealDto(CAR_NUMBER_4, PERSON_ID_2, null));
		carsService.purchase(new TradeDealDto(CAR_NUMBER_5, PERSON_ID_2, null));
		List<String> mostPopularModels = carsService.mostPopularModels();
		String[] actual = mostPopularModels.toArray(String[]::new);
		Arrays.sort(actual);
		String[] expected = {
				MODEL1, MODEL2
		};
		assertArrayEquals(expected, actual);
		
	}



	
	

}