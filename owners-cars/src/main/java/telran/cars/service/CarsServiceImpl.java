package telran.cars.service;

import java.util.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.*;
import telran.cars.exceptions.NotFoundException;
import telran.cars.service.model.*;
@Slf4j
@Service("carsService")
@Scope("prototype")
public class CarsServiceImpl implements CarsService {
HashMap<Long, CarOwner> owners = new HashMap<>();
HashMap<String, Car> cars = new HashMap<>();
HashMap<String, Integer> modelsPurchaseAmounts = new HashMap<>();
	@Override
	synchronized public PersonDto addPerson(PersonDto personDto) {
		long id = personDto.id();
		
		if(owners.containsKey(id)) {
			throw new IllegalStateException(String.format("person  %d already exists", id));
		}
		owners.put(id, new CarOwner(personDto));
		log.debug("added person with id {}", id);
		return personDto;
	}

	@Override
	synchronized public CarDto addCar(CarDto carDto) {
		String carNumber = carDto.number();
		if(cars.containsKey(carNumber)) {
			throw new IllegalStateException(String.format("car %s already exists", carNumber));
		}
		cars.put(carNumber, new Car(carDto));
		log.debug("added car {}", carNumber);
		return carDto;
	}

	@Override
	synchronized public PersonDto updatePerson(PersonDto personDto) {
		long id = personDto.id();
		hasCarOwner(id);
		CarOwner carOwner = owners.get(id);
		String oldEmail = carOwner.getEmail();
		String newEmail = personDto.email();
		if(newEmail.equals(oldEmail)) {
			log.warn("nothing to update");
		} else {
			carOwner.setEmail(newEmail);
			log.debug("person {}, old mail - {}, new mail - {}", id, oldEmail, newEmail);
		}
		return personDto;
	}

	@Override
	synchronized public PersonDto deletePerson(long id) {
		
		hasCarOwner(id);
		CarOwner carOwner = owners.get(id);
		List<Car> cars = carOwner.getCars();
		cars.forEach(c -> c.setOwner(null));
		owners.remove(id);
		log.debug("person {} has been deleted", id);
		return carOwner.build();
	}

	private void hasCarOwner(long id) {
		if(!owners.containsKey(id)) {
			throw new NotFoundException(String.format("person %d doesn't exists", id));
		}
	}

	@Override
	synchronized public CarDto deleteCar(String carNumber) {
		hasCar(carNumber);
		Car car = cars.get(carNumber);
		CarOwner carOwner = car.getOwner();
		
		if (carOwner != null) {
			carOwner.getCars().remove(car);
		}
		cars.remove(carNumber);
		log.debug("car {} has been deleted", carNumber);
		return car.build();
	}

	private void hasCar(String carNumber) {
		if(!cars.containsKey(carNumber)) {
			throw new NotFoundException(String.format("car %s doesn't exists", carNumber));
		}
	}

	@Override
	synchronized public TradeDealDto purchase(TradeDealDto tradeDeal) {
		log.debug("purchase: received car {}, owner {}", tradeDeal.carNumber(), tradeDeal.personId());
		Long personId = tradeDeal.personId();
		
		CarOwner carOwner = null;
		String carNumber = tradeDeal.carNumber();
		hasCar(carNumber);
		Car car = cars.get(carNumber);
		CarOwner oldOwner = car.getOwner();
		checkSameOwner(personId, oldOwner);
		if(oldOwner != null) {
			oldOwner.getCars().remove(car);
		}
		if(personId != null) {
			
			log.debug("new owner {}", personId);
			hasCarOwner(personId);
			carOwner = owners.get(personId);
			carOwner.getCars().add(car);
		} else {
			log.debug("no new owner");
		}
		car.setOwner(carOwner);
		modelsPurchaseAmounts.merge(car.getModel(), 1, Integer::sum);
		return tradeDeal;
	}

	private void checkSameOwner(Long personId, CarOwner oldOwner) {
		if((oldOwner == null && personId == null) ||
				(oldOwner != null && personId == oldOwner.getId())) {
			throw new IllegalStateException("trade deal with same owner");
		}
		
	}

	@Override
	synchronized public List<CarDto> getOwnerCars(long id) {
		log.debug("getOwnerCars for owner {}", id);
		hasCarOwner(id);
		return owners.get(id).getCars().stream().map(Car::build).toList();
	}

	@Override
	synchronized public PersonDto getCarOwner(String carNumber) {
		log.debug("getCarOwner for car {}", carNumber);
		hasCar(carNumber);
		Car car = cars.get(carNumber);
		CarOwner carOwner = car.getOwner();
		PersonDto res = null;
		if(carOwner != null) {
			res = carOwner.build();
			log.debug("car belongs to owner {}", carOwner.getId());
		} else {
			log.debug("car belongs to no one");
		}
		return res;
	}

	@Override
	synchronized public List<String> mostPopularModels() {
		int maxAmount = Collections.max(modelsPurchaseAmounts.values());
		log.trace("map of amounts {}", modelsPurchaseAmounts);
		log.debug("maximal amount of purchases is {}", maxAmount);
		return modelsPurchaseAmounts.entrySet().stream()
				.filter(e -> e.getValue() == maxAmount)
				.map(e -> e.getKey()).toList();
	}

}