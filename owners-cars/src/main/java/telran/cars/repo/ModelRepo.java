package telran.cars.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.cars.dto.ModelNameAmount;
import telran.cars.service.model.*;

public interface ModelRepo extends JpaRepository<Model, ModelYear> {

	@Query("""
			select model.modelYear.name from Car as car join TradeDeal as td on car.number = td.car.number group by model.modelYear.name having count(*) = 
			(select max(count) from (select count(*) as count from Car as car1 join TradeDeal as td1 on car1.number = td1.car.number group by model.modelYear.name))""" )
		List<String> findMostSoldModelNames();
	
	@Query("""
			select model.modelYear.name as name, count(*) as amount from Car as cars group by model.modelYear.name order by count(*) desc limit :nModels""")
	List<ModelNameAmount> findMostPopularModelNames(int nModels);
	@Query("""
			select model.modelYear.name as name, count(*) as amount from Car car where car.carOwner.id in 
			(select co.id as nu from CarOwner co  where co.birthDate between :birthDate1 and :birthDate2) 
			GROUP BY model.modelYear.name order by count(*) desc limit :nModels
			""")
	List<ModelNameAmount> findPopularModelNameOwnerAges(int nModels,
			LocalDate birthDate1, LocalDate birthDate2);

	
}
