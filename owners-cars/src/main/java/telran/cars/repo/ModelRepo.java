package telran.cars.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.cars.dto.ModelNameAmount;
import telran.cars.service.model.*;

public interface ModelRepo extends JpaRepository<Model, ModelYear> {

	@Query("""
			select td.car.model.modelYear.name from TradeDeal as td group by td.car.model.modelYear.name having count(*) = 
			(select max(count) from (select count(*) as count from TradeDeal as td1 group by td1.car.model.modelYear.name))""" )
		List<String> findMostSoldModelNames();
	
	@Query("""
			select model.modelYear.name as name, count(*) as amount from Car as cars group by model.modelYear.name order by count(*) desc limit :nModels""")
	List<ModelNameAmount> findMostPopularModelNames(int nModels);
	@Query("""
			select car.model.modelYear.name as name, count(*) as amount from CarOwner co join Car car on car.carOwner.id = co.id
			where co.birthDate between :birthDate1 and :birthDate2 GROUP BY car.model.modelYear.name order by count(*) desc limit :nModels
			""")
	List<ModelNameAmount> findPopularModelNameOwnerAges(int nModels,
			LocalDate birthDate1, LocalDate birthDate2);

	
}
