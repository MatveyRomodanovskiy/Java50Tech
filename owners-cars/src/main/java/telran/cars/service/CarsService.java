package telran.cars.service;

import java.util.List;

import telran.cars.dto.*;

public interface CarsService {
PersonDto addPerson(PersonDto personDto);
CarDto addCar(CarDto carDto);
ModelDto addModel(ModelDto modelDto);
PersonDto updatePerson(PersonDto personDto);
PersonDto deletePerson(long id);
CarDto deleteCar(String carNumber);
TradeDealDto purchase(TradeDealDto tradeDeal);
List<CarDto> getOwnerCars(long id);
PersonDto getCarOwner(String carNumber);
List<String> mostSoldModelNames();
List<ModelNameAmount> mostPopularModelNames(int nModels); //most popular model names according to number of cars
long countTradeDealAtMonthModel (String modelName, int month, int year);
List <ModelNameAmount> mostPopularModelNameByOwnersAge(int nModels, int ageFrom, int ageTo);
String oneMostPopularColorModel(String model);
EnginePowerCapacity minEnginePowerCapacityByOwnerAges(int ageFrom, int ageTo);
}
