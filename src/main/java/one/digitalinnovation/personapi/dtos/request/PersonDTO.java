package one.digitalinnovation.personapi.dtos.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.digitalinnovation.personapi.entities.Phone;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

  private Long id;

  @NotEmpty
  @Size(min = 2, max = 100)
  private String firstName;

  @NotEmpty
  @Size(min = 2, max = 100)
  private String lastName;

  @NotEmpty
  private String cpf;

  private String birthDate;

  @Valid
  @NotEmpty
  private List<Phone> phone;
}
