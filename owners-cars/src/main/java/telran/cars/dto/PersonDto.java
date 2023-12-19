package telran.cars.dto;

import jakarta.validation.constraints.*;
import static telran.cars.api.ValidationConstants.*;

public record PersonDto(@NotNull(message = MISSING_PERSON_ID) @Min(value = MIN_PERSON_ID_NUMBER, message = WRONG_ID_NUMBER_MIN) 
@Max(value = MAX_PERSON_ID_NUMBER, message = WRONG_ID_NUMBER_MAX) Long id, @NotEmpty(message = MISSING_NAME) String name, 
@NotEmpty(message = MISSING_BD) @Pattern(regexp = BD_REGEXP, message = WRONG_DATE_FORMAT)
String birthDate, @NotEmpty(message = EMAIL_EMPTY) @Email(message = WRONG_EMAIL) String email) {

}
