package telran.cars.dto;

import jakarta.validation.constraints.*;
import static telran.cars.api.ValidationConstants.*;

public record TradeDealDto(@NotEmpty(message = MISSING_CAR_NUMBER_MESSAGE) 
@Pattern(regexp = CAR_NUMBER_REGEXP) String carNumber, @Min(value = MIN_PERSON_ID_NUMBER, message = WRONG_ID_NUMBER_MIN) 
@Max(value = MAX_PERSON_ID_NUMBER, message = WRONG_ID_NUMBER_MAX) Long personId) {

}
