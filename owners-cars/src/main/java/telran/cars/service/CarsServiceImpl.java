package telran.cars.service;

import java.awt.geom.IllegalPathStateException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.*;
import telran.cars.exceptions.*;
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
				.orElseThrow(()-> new PersonNotFoundException());
		carOwner.setEmail(personDto.email());
		log.debug("person email {} has been saved", personDto);
		return personDto;
	}

	@Override
	@Transactional
	public PersonDto deletePerson(long id) {
		CarOwner victim = carOwnerRepo.findById(id)
				.orElseThrow(() -> new PersonNotFoundException());
		
		carOwnerRepo.delete(victim);
		return victim.build();
	}

	@Override
	@Transactional
	public CarDto deleteCar(String carNumber) {
		Car victimCar = carRepo.findById(carNumber)
				.orElseThrow(()-> new CarNotFoundException());
		List<TradeDeal> victimDeals = tradeDealRepo.findByCarNumber(carNumber);
		if (!victimDeals.isEmpty()) {
			victimDeals.forEach(d-> tradeDealRepo.delete(d));
		}
		carRepo.delete(victimCar);
		return victimCar.build();
	}



	@Override
	@Transactional
	public TradeDealDto purchase(TradeDealDto tradeDealDto) {
		Car car = carRepo.findById(tradeDealDto.carNumber())
				.orElseThrow(() -> new CarNotFoundException());
		CarOwner carOwner = null;
		Long personId = tradeDealDto.personId();
		if ( personId != null) {
			log.debug("ID of new car's owner is {}", personId);
			carOwner = carOwnerRepo.findById(personId)
					.orElseThrow(() -> new PersonNotFoundException());
			
		}
				if ((car.getCarOwner()!=null && car.getCarOwner().getId() == personId) || (car.getCarOwner()== null && personId == null)) {
					throw new TradeDealIllegalStateException();
				 
			}
		
		TradeDeal tradeDeal = new TradeDeal();
		tradeDeal.setCar(car);
		tradeDeal.setCarOwner(carOwner);
		tradeDeal.setDate(LocalDate.parse(tradeDealDto.date()));
		car.setCarOwner(carOwner);
		tradeDealRepo.save(tradeDeal);
		log.debug("trade: {} has been saved", tradeDealDto);
		return tradeDealDto;
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
		Model newModel = Model.of(modelDto);
		if (modelRepo.existsById(newModel.getModelYear())) {
			throw new ModelIllegalStateExceptions();
		}
		modelRepo.save(newModel);
		return modelDto;
	}

}
