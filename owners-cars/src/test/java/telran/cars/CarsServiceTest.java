package telran.cars;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import telran.cars.exceptions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import telran.cars.dto.*;
import telran.cars.repo.*;
import telran.cars.service.CarsService;
import telran.cars.service.model.*;
record ModelNameAmountTest(String name, long amount) implements Comparable<ModelNameAmountTest> {

	@Override
	public int compareTo(ModelNameAmountTest o) {
		int res = Long.compare(o.amount, amount);
		if (res == 0) {
			res = name.compareTo(o.name);
		}
		return res;
	}}

@SpringBootTest
@Sql(scripts = {"classpath:test_data.sql"})
class CarsServiceTest {
	private static final String SERVICE_TEST = "Service Test: ";
	//Model names
	private static final String MODEL1 = "model1";
	private static final String MODEL2 = "model2";
	private static final String MODEL3 = "model3";
	private static final String MODEL4 = "model4";
	/******************************************************/
	//Car numbers
	private static final String CAR_NUMBER_1 = "111-11-111";
	private static final String CAR_NUMBER_2 = "222-11-111";
	private static final  String CAR_NUMBER_3 = "333-11-111";
	private static final  String CAR_NUMBER_4 = "444-44-444";
	private static final  String CAR_NUMBER_5 = "555-55-555";
	/***********************************************************/
	//Person id's
	private static final Long PERSON_ID_NOT_EXISTS = 1111111111L;
	private static final Long PERSON_ID_1 = 123l;
	private static final Long PERSON_ID_2 = 124l;
	private static final Long PERSON_ID_3 = 125l;
	private static final Long PERSON_ID_4 = 126l;
	private static final Long PERSON_ID_5 = 127l;
	/**************************************************************/
	//Person's names
	private static final String NAME1 = "name1";
	private static final String NAME2 = "name2";
	private static final String NAME3 = "name3";
	private static final String NAME4 = "name4";
	private static final String NAME5 = "name5";
	/*********************************************************************/
	//Birth days
	private static final String BIRTH_DATE_1 = "2000-10-10";
	private static final String BIRTH_DATE_2 = "2003-10-10";
	private static final String BIRTH_DATE_3 = "1970-01-01";
	private static final String BIRTH_DATE_4 = "1975-10-10";
	private static final String BIRTH_DATE_5 = "2004-10-10";
	/*********************************************************************/
	//email's
	private static final String EMAIL1 = "name1@gmail.com";
	private static final  String NEW_EMAIL = "name1@tel-ran.co.il";
	private static final String EMAIL3 = "name3@gmail.com";
	private static final String EMAIL4 = "name4@gmail.com";
	private static final String EMAIL5 = "name5@gmail.com";
	private static final String EMAIL2 = "name2@gmail.com";
	/****************************************************************************/
	//Trade deal's dates
	private static final  String DATE_TRADE_DEAL_1 = "2023-03-10";
	private static final  String DATE_TRADE_DEAL_2 = "2023-03-20";
	private static final  String DATE_TRADE_DEAL_3 = "2023-04-0";
	private static final  String DATE_TRADE_DEAL_4 = "2023-11-20";
	private static final  String DATE_TRADE_DEAL_5 = "2023-11-20";
	
	/*******************************************************************************/
	//Repo's
	@Autowired
	CarOwnerRepo carOwnerRepo;
	@Autowired
	CarRepo carRepo;
	@Autowired
	TradeDealRepo tradeDealRepo;
	/*******************************************************************************/
	//Cars
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL1, 2020, "red", 1000, CarState.GOOD);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL1, 2020, "silver", 10000, CarState.OLD);
	CarDto car3 = new CarDto(CAR_NUMBER_3, MODEL4, 2023, "white", 0, CarState.NEW);
	CarDto car4 = new CarDto(CAR_NUMBER_4, MODEL4, 2023, "black", 0, CarState.NEW);
	CarDto car5 = new CarDto(CAR_NUMBER_5, MODEL3, 2021, "black", 5000, CarState.MIDDLE);
	/***************************************************************************************/
	//Persons
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXISTS, NAME1, BIRTH_DATE_1, EMAIL1);
	PersonDto personDto1 = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE_1, EMAIL1);
	PersonDto personDto2 = new PersonDto(PERSON_ID_2, NAME2, BIRTH_DATE_2, EMAIL2);
	PersonDto personDto3 = new PersonDto(PERSON_ID_3, NAME3, BIRTH_DATE_3, EMAIL3);
	PersonDto personDto4 = new PersonDto(PERSON_ID_4, NAME4, BIRTH_DATE_4, EMAIL4);
	PersonDto personDto5 = new PersonDto(PERSON_ID_5, NAME5, BIRTH_DATE_5, EMAIL5);
	/***********************************************************************************/
	//Trade Deals
	TradeDealDto tradeDealDto4 = new TradeDealDto(CAR_NUMBER_4, PERSON_ID_4, DATE_TRADE_DEAL_4);
	TradeDealDto tradeDealDto5 = new TradeDealDto(CAR_NUMBER_5, PERSON_ID_5, DATE_TRADE_DEAL_5);
	
	//service bean
	@Autowired
	CarsService carsService;
	/***************************************************************************************/
	@Test
	@DisplayName(SERVICE_TEST + TestNames.ADD_PERSON)
	void testAddPerson() {
		assertEquals(personDto, carsService.addPerson(personDto));
		assertThrowsExactly(IllegalPersonsStateExceptions.class,
				()->carsService.addPerson(personDto));
		CarOwner carOwner = carOwnerRepo.findById(personDto.id()).orElse(null);
		assertEquals(personDto, carOwner.build());
	
	}

	@Test
	@DisplayName(SERVICE_TEST + TestNames.ADD_CAR)
	void testAddCar() {
		assertEquals(car4, carsService.addCar(car4));
		CarDto carNoModel = new CarDto("11111111111", MODEL1, 2018, "green", 100000, CarState.OLD);
		assertThrowsExactly(IllegalCarsStateExeptions.class,
				()->carsService.addCar(car4));
		assertThrowsExactly(ModelNotFoundExceptions.class, () -> carsService.addCar(carNoModel));
		
	}
	@Test
	@DisplayName(SERVICE_TEST + TestNames.ADD_MODEL)
	void testAddModel() {
		ModelDto modelDtoNew = new ModelDto(MODEL4, 2024, "Company1", 100, 2000);
		assertEquals(modelDtoNew, carsService.addModel(modelDtoNew));
		assertThrowsExactly(ModelIllegalStateExceptions.class, () -> carsService.addModel(modelDtoNew));
	}

	@Test
	@DisplayName(SERVICE_TEST + TestNames.UPDATE_PERSON)
	void testUpdatePerson() {
		PersonDto personUpdated = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE_1, NEW_EMAIL);
		assertEquals(personUpdated, carsService.updatePerson(personUpdated));
		assertEquals(NEW_EMAIL, carOwnerRepo.findById(PERSON_ID_1).get().getEmail());
		assertThrowsExactly(PersonNotFoundException.class,
				() -> carsService.updatePerson(personDto));
	}

	@Test
	@DisplayName(SERVICE_TEST + TestNames.DELETE_PERSON)
	void testDeletePerson() {
		
		assertEquals(personDto1, carsService.deletePerson(PERSON_ID_1));
		assertThrowsExactly(PersonNotFoundException.class, () -> carsService.deletePerson(PERSON_ID_1));
		
	}

	@Test
	@DisplayName(SERVICE_TEST + TestNames.DELETE_CAR)
	void testDeleteCar() {
		
		assertEquals(car1, carsService.deleteCar(CAR_NUMBER_1));
		assertThrowsExactly(CarNotFoundException.class, () -> carsService.deleteCar(CAR_NUMBER_1));
		
	}

	@Test
	@DisplayName(SERVICE_TEST + TestNames.PURCHASE_NEW_OLD_OWNERS)
	void testPurchaseNewCarOwnerWithOldOwner() {
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
	@DisplayName(SERVICE_TEST + TestNames.PURCHASE_ONLY_NEW_OWNER)
	void testPurchaseNewCarOwnerWithoutOldOwner() {
		int countDeals = (int)tradeDealRepo.count(); 
		carsService.addCar(car4);
		TradeDealDto tradeDealDto = new TradeDealDto(CAR_NUMBER_4, PERSON_ID_2, DATE_TRADE_DEAL_1);
		assertEquals(tradeDealDto, carsService.purchase(tradeDealDto));
		assertEquals(PERSON_ID_2, carRepo.findById(CAR_NUMBER_4).get().getCarOwner().getId());
		TradeDeal tradeDeal = tradeDealRepo.findAll().get(countDeals);
		assertEquals(CAR_NUMBER_4, tradeDeal.getCar().getNumber());
		assertEquals(PERSON_ID_2, tradeDeal.getCarOwner().getId());
		assertEquals(DATE_TRADE_DEAL_1, tradeDeal.getDate().toString());
		
		
		
	}
	@Test
	@DisplayName(SERVICE_TEST + TestNames.PURCHASE_NOT_FOUND)
	void testPurchaseNotFound() {
		TradeDealDto tradeDealCarNotFound = new TradeDealDto(CAR_NUMBER_4, PERSON_ID_1, DATE_TRADE_DEAL_1);
		TradeDealDto tradeDealOwnerNotFound = new TradeDealDto(CAR_NUMBER_1,
				PERSON_ID_NOT_EXISTS, DATE_TRADE_DEAL_1);
		assertThrowsExactly(PersonNotFoundException.class, () -> carsService.purchase(tradeDealOwnerNotFound));
		assertThrowsExactly(CarNotFoundException.class, () -> carsService.purchase(tradeDealCarNotFound));
		
	}
	@Test
	@DisplayName(SERVICE_TEST + TestNames.PURCHASE_NO_NEW_OWNER)
	void testPurchaseNoNewCarOwner() {
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
	@DisplayName(SERVICE_TEST + TestNames.PURCHASE_SAME_OWNER)
	void testPurchaseSameOwner() {
		TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER_1,PERSON_ID_1, DATE_TRADE_DEAL_1);
		assertThrowsExactly(TradeDealIllegalStateException.class,
				() -> carsService.purchase(tradeDeal));
		carsService.addCar(car4);
		TradeDealDto tradeDealNoOwners = new TradeDealDto(CAR_NUMBER_4,null, DATE_TRADE_DEAL_1);
		assertThrowsExactly(TradeDealIllegalStateException.class,
				() -> carsService.purchase(tradeDealNoOwners));
	}

	@Test
	/**
	 * test of the method getOwnerCars
	 * the method has been written at CW #64
	 */
	@DisplayName(SERVICE_TEST + TestNames.GET_OWNER_CARS)
	void testGetOwnerCars() {
		carsService.purchase(new TradeDealDto(CAR_NUMBER_2, PERSON_ID_1, DATE_TRADE_DEAL_1));
		List<CarDto> carsActual1 = carsService.getOwnerCars(PERSON_ID_1);
		List<CarDto> carsActual2 = carsService.getOwnerCars(PERSON_ID_2);
		CarDto[] carsExpected1 = {
				car1, car2
		};
		assertArrayEquals(carsExpected1, carsActual1.toArray(CarDto[]::new));
		assertTrue(carsActual2.isEmpty());
		
	}

	@Test
	/**
	 * test of the method getCarOwner
	 * the method has been written at CW #64
	 */
	@DisplayName(SERVICE_TEST + TestNames.GET_CAR_OWNER)
	void testGetCarOwner() {
		carsService.addCar(car4);
		assertNull(carsService.getCarOwner(CAR_NUMBER_4));
		assertEquals(personDto1, carsService.getCarOwner(CAR_NUMBER_1));
		assertThrowsExactly(CarNotFoundException.class,
				()->carsService.getCarOwner(CAR_NUMBER_5));
		
	}
	@Test
	/**
	 * test of the method mostSoldModelNames
	 * the method has been written at CW #64
	 */
	@DisplayName(SERVICE_TEST + TestNames.MODEL_NAMES_MOST_SOLD)
	void testMostSoldModelNames() {
		setUpStatistics();
		List<String> modelNamesActual = carsService.mostSoldModelNames();
		String[] modelNamesExpected = {
				"model1", "model4"
		};
		assertArrayEquals(modelNamesExpected, modelNamesActual.toArray(String[]::new));
		
	}
	
	@Test
	/**
	 * test of the method mostPopularModelNames
	 * the method has been written at CW #64
	 */
	@DisplayName(SERVICE_TEST + TestNames.MODEL_NAMES_MOST_POPULAR)
	void testMostPopularModelNames() {
		setUpStatistics();
		List<ModelNameAmount> list = carsService.mostPopularModelNames(2);
		ModelNameAmountTest[] expected = {
				new ModelNameAmountTest(MODEL1, 2),
				new ModelNameAmountTest(MODEL4, 2)
			};
		modelNameAmountsTest(list, expected);
		
	}

	private void modelNameAmountsTest(List<ModelNameAmount> list, ModelNameAmountTest[] expected) {
		ModelNameAmountTest[] actual = list 
				.stream()
				.map(ma -> new ModelNameAmountTest(ma.getName(),ma.getAmount()))
				.sorted().toArray(ModelNameAmountTest[]::new);
		
		assertArrayEquals(expected, actual);
	}
	//tests for the methods of the HW #64
	@Test
	@DisplayName(SERVICE_TEST + TestNames.TRADE_DEALS_COUNT_MONTH_MODEL)
	void testCountTradeDealAtMonthModel() {
		setUpStatistics();
		assertEquals(2, carsService.countTradeDealAtMonthModel(MODEL1, 3, 2023));
	}
	@Test
	@DisplayName(SERVICE_TEST + TestNames.MODEL_NAMES_MOST_POPULAR_OWNER_AGES)
	void testMostPopularModelNameByOwnerAges() {
		setUpStatistics();
		List<ModelNameAmount> list = carsService.mostPopularModelNameByOwnersAge(2, 30, 60);
		ModelNameAmountTest[] expected = {
				new ModelNameAmountTest(MODEL4, 2),
				new ModelNameAmountTest(MODEL1, 1)
		};
		modelNameAmountsTest(list, expected);
	}
	@Test
	@DisplayName(SERVICE_TEST + TestNames.COLOR_MOST_POPULR_MODEL)
	void testOneMostPopularColorModel() {
		setUpStatistics();
		assertEquals("red", carsService.oneMostPopularColorModel(MODEL1));
	}
	@Test
	@DisplayName(SERVICE_TEST + TestNames.ENGINE_POWER_CAPACITY_MIN_AGES)
	void testMinEnginePowerCapacityByOwnerAges() {
		setUpStatistics();
		EnginePowerCapacity epc = carsService.minEnginePowerCapacityByOwnerAges(30, 60);
		int minPowerActual = epc.getPower();
		int minCapacityActual = epc.getCapacity();
		int minPowerExpected = 84;
		int minCapacityExpected = 1300;
		assertEquals(minPowerExpected, minPowerActual);
		assertEquals(minCapacityExpected, minCapacityActual);
	}
	private void setUpStatistics() {
		carsService.addCar(car4);
		carsService.addCar(car5);
		carsService.addPerson(personDto4);
		carsService.addPerson(personDto5);
		carsService.purchase(tradeDealDto4);
		carsService.purchase(tradeDealDto5);
		
		
	}



	
	

}