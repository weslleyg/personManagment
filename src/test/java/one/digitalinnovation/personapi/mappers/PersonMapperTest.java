package one.digitalinnovation.personapi.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import one.digitalinnovation.personapi.dtos.mappers.PersonMapper;
import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.dtos.request.PhoneDTO;
import one.digitalinnovation.personapi.entities.Person;
import one.digitalinnovation.personapi.entities.Phone;
import one.digitalinnovation.personapi.utils.PersonUtils;

@SpringBootTest
public class PersonMapperTest {

  @Autowired
  private PersonMapper personMapper;

  @Test
  void testGivenPersonDTOThenReturnPersonEntity() {
    PersonDTO personDTO = PersonUtils.createFakeDTO();
    Person person = this.personMapper.toModel(personDTO);

    assertEquals(personDTO.getFirstName(), person.getFirstName());
    assertEquals(personDTO.getLastName(), person.getLastName());
    assertEquals(personDTO.getCpf(), person.getCpf());

    PhoneDTO phoneDTO = personDTO.getPhones().get(0);
    Phone phone = person.getPhones().get(0);

    assertEquals(phoneDTO.getType(), phone.getType());
    assertEquals(phoneDTO.getNumber(), phone.getNumber());
  }

  @Test
  void testGivenPersonEntityThenReturnPersonDTO() {
    Person person = PersonUtils.createFakeEntity();
    PersonDTO personDTO = this.personMapper.toDTO(person);

    assertEquals(person.getFirstName(), personDTO.getFirstName());
    assertEquals(person.getLastName(), personDTO.getLastName());
    assertEquals(person.getCpf(), personDTO.getCpf());

    PhoneDTO phoneDTO = personDTO.getPhones().get(0);
    Phone phone = person.getPhones().get(0);

    assertEquals(phone.getType(), phoneDTO.getType());
    assertEquals(phone.getNumber(), phoneDTO.getNumber());
  }
}
