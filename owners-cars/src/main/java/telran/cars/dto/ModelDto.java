package telran.cars.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import static telran.cars.api.ValidationConstants.*;
@Getter
@AllArgsConstructor
public class ModelDto {
	@NotEmpty(message=MISSING_MODEL_NAME_MESSAGE)
	String modelName;
	@NotEmpty(message=MISSING_MODEL_YEAR_MESSAGE)
	@Min(value=MIN_MODEL_YEAR, message=WRONG_MIN_YEAR)
	Integer modelYear;
	@NotEmpty(message=MISSING_COMPANY_MESSAGE)
	String company;
	Integer enginePower;
	Integer engineCapacity;
}
