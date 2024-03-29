package telran.cars.controller;

import static telran.cars.api.ValidationConstants.CAR_NUMBER_REGEXP;
import static telran.cars.api.ValidationConstants.MAX_PERSON_ID_NUMBER;
import static telran.cars.api.ValidationConstants.MIN_PERSON_ID_NUMBER;
import static telran.cars.api.ValidationConstants.MISSING_CAR_NUMBER_MESSAGE;
import static telran.cars.api.ValidationConstants.MISSING_PERSON_ID;
import static telran.cars.api.ValidationConstants.WRONG_ID_NUMBER_MAX;
import static telran.cars.api.ValidationConstants.WRONG_ID_NUMBER_MIN;
import static telran.cars.api.ValidationConstants.WRONG_NUMBER_MESSAGE;

import java.util.List;


import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.cars.service.CarsService;
import telran.cars.dto.*;
import telran.cars.exceptions.controller.CarsExceptionsController;

@RestController
@RequestMapping("cars")
@RequiredArgsConstructor
@Slf4j
public class CarsController {
	final CarsService carsService;
	@PostMapping
	CarDto addCar(@RequestBody @Valid CarDto carDto) {
		log.debug("addCar: received car data: {}", carDto);
		//annotation @RequstBody informs Spring about conversion of JSON inside a request to the given parameter
		return carsService.addCar(carDto);
	}
	@PostMapping("person")
	PersonDto addPerson(@RequestBody @Valid PersonDto personDto) {
		log.debug("addPerson: received personData data: {}", personDto);
		return carsService.addPerson(personDto);
	}
	@PutMapping("person")
	PersonDto updatePerson(@RequestBody @Valid PersonDto personDto) {
		log.debug("updatePerson: received personData data: {}", personDto);
		return carsService.updatePerson(personDto);
	}
	@PutMapping("trade")
	TradeDealDto purchase(@RequestBody @Valid TradeDealDto tradeDealDto) {
		log.debug("purchase: received trade deal data: {}", tradeDealDto);
		return carsService.purchase(tradeDealDto);
	}
	@DeleteMapping("person/{id}")
	PersonDto deletePerson(@PathVariable(name="id")  @NotNull(message = MISSING_PERSON_ID) @Min(value = MIN_PERSON_ID_NUMBER, message = WRONG_ID_NUMBER_MIN) 
	@Max(value = MAX_PERSON_ID_NUMBER, message = WRONG_ID_NUMBER_MAX) long id) {
//		try {
//			return carsService.deletePerson(id);
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "person not found message", null);
//		}
		log.debug("delete person: person with ID {}", id);
		return carsService.deletePerson(id);
	}
	@DeleteMapping("{carNumber}")
	CarDto deleteCar(@PathVariable(name="carNumber") @NotEmpty (message = MISSING_CAR_NUMBER_MESSAGE) @Pattern(regexp = CAR_NUMBER_REGEXP, message = WRONG_NUMBER_MESSAGE) String carNumber) {
		log.debug("delete car: car with number {}", carNumber);
		return carsService.deleteCar(carNumber);
		
	}
	@GetMapping("person/{id}")
	List<CarDto> getOwnerCars(@PathVariable @NotNull(message = MISSING_PERSON_ID) @Min(value = MIN_PERSON_ID_NUMBER, message = WRONG_ID_NUMBER_MIN) 
	@Max(value = MAX_PERSON_ID_NUMBER, message = WRONG_ID_NUMBER_MAX) long id) {
		List<CarDto> res = carsService.getOwnerCars(id);
		if(res.isEmpty()) {
			log.warn("getOwnerCars: no cars for person with id {}", id );
		}
		else {
			log.trace("getOwnerCars: cars of person with id {} {}", id, res);
		}
		return res;
	}
	@GetMapping("{carNumber}")
	PersonDto getCarOwner(@PathVariable @NotEmpty (message = MISSING_CAR_NUMBER_MESSAGE) @Pattern(regexp = CAR_NUMBER_REGEXP, message = WRONG_NUMBER_MESSAGE) String carNumber) {
		
		log.debug("getCarOwner: received car number {}", carNumber);
		return carsService.getCarOwner(carNumber);
	}
}
