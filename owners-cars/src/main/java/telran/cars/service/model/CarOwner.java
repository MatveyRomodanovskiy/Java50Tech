package telran.cars.service.model;

import java.time.LocalDate;
import java.util.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.cars.dto.PersonDto;
import jakarta.persistence.*;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "car_owners")
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class CarOwner {
	@Id
	Long id;
	String name;
	@Column(nullable = false, name = "birth_date")
	@Temporal(TemporalType.DATE)
	LocalDate birthDate;
	String email;
	
	public static CarOwner of(PersonDto personDto) {
		return new CarOwner(personDto.id(), personDto.name(),
				LocalDate.parse(personDto.birthDate()), personDto.email());
		
	}
	public PersonDto build() {
		return new PersonDto(id, name, birthDate.toString(), email);
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
