package telran.cars;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.cars.dto.CarDto;
import telran.cars.dto.PersonDto;
import telran.cars.service.CarsService;

@WebMvcTest
class CarsControllerTest {
	@MockBean
	CarsService carsService;
	@Autowired
	MockMvc mockMvc;
	CarDto carDto = new CarDto("car", "Model");
	@Autowired
	ObjectMapper mapper;
	PersonDto personDto = new PersonDto(1000000, "ts", "01-01-1970", "s@s.s");
	
	@Test
	void testAddCar() throws Exception {
		when(carsService.addCar(carDto)).thenReturn(carDto);
		String jsonCarDto = mapper.writeValueAsString(carDto); //conversion from carDto object to string JSON
		String actualJSON = mockMvc.perform(post("http://localhost:8080/cars").contentType(MediaType.APPLICATION_JSON)
				.content(jsonCarDto)).andExpect(status().isOk()).andReturn().getResponse()
		.getContentAsString();
		assertEquals(jsonCarDto, actualJSON );
	}

	@Test
	void testAddPerson() throws Exception {
		when(carsService.addPerson(personDto)).thenReturn(personDto);
		String jsonPersonDto = mapper.writeValueAsString(personDto);
		String actualJsonString = mockMvc.perform(post("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(jsonPersonDto, actualJsonString);
	}

	@Test
	void testUpdatePerson() {
		//TODO
	}

	@Test
	void testPurchase() {
		//TODO
	}

	@Test
	void testDeletePerson() {
		//TODO
	}

	@Test
	void testDeleteCar() {
		//TODO
	}

	@Test
	void testGetOwnerCars() {
		//TODO
	}

	@Test
	void testGetCarOwner() {
		//TODO
	}

}
