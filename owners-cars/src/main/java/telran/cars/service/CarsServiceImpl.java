package telran.cars.service;

import java.awt.geom.IllegalPathStateException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.*;
import telran.cars.exceptions.CarNotFoundException;
import telran.cars.exceptions.IllegalCarsStateExeptions;
import telran.cars.exceptions.IllegalPersonsStateExceptions;
import telran.cars.exceptions.ModelNotFoundExceptions;
import telran.cars.exceptions.PersonNotFoundExeption;
import telran.cars.exceptions.TradeDealIllegalStateException;
import telran.cars.repo.*;
import telran.cars.service.model.Car;
import telran.cars.service.model.CarOwner;
import telran.cars.service.model.Model;
import telran.cars.service.model.ModelYear;
import telran.cars.service.model.TradeDeal;
@Service
@RequiredArgsConstructor
@Slf4j
public class CarsServiceImpl implements CarsService {

	final CarRepo carRepo;
	final CarOwnerRepo carOwnerRepo;
	final ModelRepo modelRepo;
	final TradeDealRepo tradeDealRepo;
	
	@Override
	@Transactional
	public PersonDto addPerson(PersonDto personDto) {
		if (carOwnerRepo.existsById(personDto.id())) {
			throw new IllegalPersonsStateExceptions();
		}
		CarOwner carOwner = CarOwner.of(personDto);
		carOwnerRepo.save(carOwner);
		log.debug("person{} has been saved", personDto);
		return personDto;
	}

	@Override
	@Transactional
	public CarDto addCar(CarDto carDto) {
		if (carRepo.existsById(carDto.number())) {
			throw new IllegalCarsStateExeptions();
		}
		Model model = modelRepo.findById(new ModelYear(carDto.model(), carDto.year()))
				.orElseThrow(() -> new ModelNotFoundExceptions());
		Car car = Car.of(carDto);
		car.setModel(model);
		carRepo.save(car);
		log.debug("car{} has been saved", carDto);
		return carDto;
	}

	@Override
	@Transactional
	public PersonDto updatePerson(PersonDto personDto) {
		CarOwner carOwner = carOwnerRepo.findById(personDto.id())
				.orElseThrow(()-> new PersonNotFoundExeption());
		carOwner.setEmail(personDto.email());
		log.debug("person email {} has been saved", personDto);
		return personDto;
	}

	@Override
	@Transactional
	public PersonDto deletePerson(long id) {
		// TODO Auto-generated method stub HW63
		return null;
	}

	@Override
	@Transactional
	public CarDto deleteCar(String carNumber) {
		// TODO Auto-generated method stub HW63
		return null;
	}

	@Override
	public TradeDealDto purchase(TradeDealDto tradeDeal) {
		Car car = carRepo.findById(tradeDeal.carNumber())
				.orElseThrow(() -> new CarNotFoundException());
		CarOwner carOwner = null;
		Long personId = tradeDeal.personId();
		if (personId != null) {
			carOwner = carOwnerRepo.findById(personId)
					.orElseThrow(() -> new PersonNotFoundExeption());
			if (car.getCarOwner().getId() == personId) {
				throw new TradeDealIllegalStateException();
			}
		}
		TradeDeal tradeDealEnt = new TradeDeal();
		tradeDealEnt.setCar(car);
		tradeDealEnt.setCarOwner(carOwner);
		tradeDealEnt.setDate(LocalDate.parse(tradeDeal.date()));
		return tradeDeal;
	}

	@Override
	public List<CarDto> getOwnerCars(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonDto getCarOwner(String carNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> mostPopularModels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelDto addModel(ModelDto modelDto) {
		// TODO Auto-generated method stub HW63
		return null;
	}

}
