package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import telran.cars.dto.*;
import telran.cars.exceptions.NotFoundException;
import telran.cars.service.CarsService;


@SpringBootTest
class CarsServiceTest {
	private static final String MODEL1 = "model1";
	private static final String MODEL2 = "model2";
	private static final String MODEL3 = "model3";
	private static final String CAR_NUMBER_1 = "111-11-111";
	private static final String CAR_NUMBER_2 = "222-22-222";
	private static final  String CAR_NUMBER_3 = "333-33-333";
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
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL1);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL1);
	CarDto car3 = new CarDto(CAR_NUMBER_3, MODEL2);
	CarDto car4 = new CarDto(CAR_NUMBER_4, MODEL2);
	CarDto car5 = new CarDto(CAR_NUMBER_5, MODEL3);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXISTS, NAME1, BIRTH_DATE_1, EMAIL1);
	PersonDto personDto1 = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE_1, EMAIL1);
	PersonDto personDto2 = new PersonDto(PERSON_ID_2, NAME2, BIRTH_DATE_2, EMAIL2);
	@Autowired
	ApplicationContext ctx;
	CarsService carsService;

	@BeforeEach
	void setUp() {
		
			carsService = ctx.getBean("carsService", CarsService.class);
			carsService.addCar(car1);
			carsService.addCar(car2);
			carsService.addPerson(personDto1);
			carsService.addPerson(personDto2);
			carsService.purchase(new TradeDealDto(CAR_NUMBER_1, PERSON_ID_1));
			carsService.purchase(new TradeDealDto(CAR_NUMBER_2, PERSON_ID_2));
		
		
	}
	
	

	@Test
	void testAddPerson() {
		assertEquals(personDto, carsService.addPerson(personDto));
		assertThrowsExactly(IllegalStateException.class,
				()->carsService.addPerson(personDto1));
		List<CarDto> cars = carsService.getOwnerCars(personDto.id());
		assertTrue(cars.isEmpty());
		assertEquals(personDto, carsService.deletePerson(personDto.id()));
	}

	@Test
	void testAddCar() {
		assertEquals(car3, carsService.addCar(car3));
		assertThrowsExactly(IllegalStateException.class,
				()->carsService.addCar(car1));
		PersonDto person = carsService.getCarOwner(CAR_NUMBER_3);
		assertNull(person);
	}

	@Test
	void testUpdatePerson() {
		PersonDto personUpdated = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE_1, NEW_EMAIL);
		assertEquals(personUpdated, carsService.updatePerson(personUpdated));
		assertEquals(personUpdated, carsService.getCarOwner(CAR_NUMBER_1));
		assertThrowsExactly(NotFoundException.class,
				() -> carsService.updatePerson(personDto));
	}

	@Test
	void testDeletePerson() {
		List<CarDto> cars = carsService.getOwnerCars(PERSON_ID_1);
		assertEquals(personDto1, carsService.deletePerson(PERSON_ID_1));
		assertThrowsExactly(NotFoundException.class, () -> carsService.deletePerson(PERSON_ID_1));
		cars.forEach(c -> assertNull(carsService.getCarOwner(c.number())));
	}

	@Test
	void testDeleteCar() {
		Long id = carsService.getCarOwner(CAR_NUMBER_1).id();
		assertEquals(car1, carsService.deleteCar(CAR_NUMBER_1));
		assertThrowsExactly(NotFoundException.class, () -> carsService.deleteCar(CAR_NUMBER_1));
		assertFalse(carsService.getOwnerCars(id).contains(car1));
	}

	@Test
	void testPurchaseNewCarOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1, PERSON_ID_2);
		assertEquals(tradeDeal, carsService.purchase(tradeDeal));
		assertEquals(personDto2, carsService.getCarOwner(CAR_NUMBER_1));
		assertFalse(carsService.getOwnerCars(PERSON_ID_1).contains(car1));
		assertTrue(carsService.getOwnerCars(PERSON_ID_2).contains(car1));
		
	}
	@Test
	void testPurchaseNotFound() {
		TradeDealDto tradeDealCarNotFound = new TradeDealDto(CAR_NUMBER_3, PERSON_ID_1);
		TradeDealDto tradeDealOwnerNotFound = new TradeDealDto(CAR_NUMBER_1,
				PERSON_ID_NOT_EXISTS);
		assertThrowsExactly(NotFoundException.class, () -> carsService.purchase(tradeDealOwnerNotFound));
		assertThrowsExactly(NotFoundException.class, () -> carsService.purchase(tradeDealCarNotFound));
		
	}
	@Test
	void testPurchaseNoCarOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1,null);
		assertEquals(tradeDeal, carsService.purchase(tradeDeal));
		assertFalse(carsService.getOwnerCars(PERSON_ID_1).contains(car1));
		assertNull(carsService.getCarOwner(CAR_NUMBER_1));
	}
	@Test
	void testPurchaseSameOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1,PERSON_ID_1);
		assertThrowsExactly(IllegalStateException.class,
				() -> carsService.purchase(tradeDeal));
	}

	@Test
	void testGetOwnerCars() {
		List<CarDto> cars = carsService.getOwnerCars(PERSON_ID_1);
		assertEquals(1, cars.size());
		assertEquals(car1, cars.get(0));
		assertThrowsExactly(NotFoundException.class,
				() -> carsService.getOwnerCars(PERSON_ID_NOT_EXISTS));
	}

	@Test
	void testGetCarOwner() {
		PersonDto ownerActual = carsService.getCarOwner(CAR_NUMBER_1);
		assertEquals(personDto1, ownerActual);
		assertThrowsExactly(NotFoundException.class, () -> carsService.getCarOwner(CAR_NUMBER_3));
	}
	@Test
	void testMostPopularModels() {
		carsService.addCar(car3);
		carsService.addCar(car4);
		carsService.addCar(car5);
		carsService.purchase(new TradeDealDto(CAR_NUMBER_3, PERSON_ID_1));
		carsService.purchase(new TradeDealDto(CAR_NUMBER_4, PERSON_ID_2));
		carsService.purchase(new TradeDealDto(CAR_NUMBER_5, PERSON_ID_2));
		List<String> mostPopularModels = carsService.mostPopularModels();
		String[] actual = mostPopularModels.toArray(String[]::new);
		Arrays.sort(actual);
		String[] expected = {
				MODEL1, MODEL2
		};
		assertArrayEquals(expected, actual);
		
		}



	
	

}