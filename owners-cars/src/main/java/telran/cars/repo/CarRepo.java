package telran.cars.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.cars.dto.EnginePowerCapacity;
import telran.cars.dto.ModelNameAmount;
import telran.cars.service.model.*;

public interface CarRepo extends JpaRepository<Car, String> {
List <Car> findByCarOwnerId(long id);

@Query("""
		select color from Car where model.modelYear.name=:model group by color order by count(*) desc, color asc  limit 1""")
String findOneMostPopularColorModel(String model);
/***********************************************************/

@Query("""
		select min(car.model.enginePower) as power, min(car.model.engineCapacity) as capacity from Car car where carOwner.birthDate between :birthDate1 and :birthDate2
		""")
EnginePowerCapacity findMinPowerCapcityOwnerBirthDates(LocalDate birthDate1, LocalDate birthDate2);

}
