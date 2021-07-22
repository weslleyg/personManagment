package one.digitalinnovation.personapi.services;

import static org.mockito.Mockito.any;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.dtos.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entities.Person;
import one.digitalinnovation.personapi.mappers.PersonMapper;
import one.digitalinnovation.personapi.repositories.PersonRepository;
import one.digitalinnovation.personapi.utils.PersonUtils;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

  @Mock
  private PersonRepository personRepository;

  @Mock
  private PersonMapper personMapper;

  @InjectMocks
  private PersonService personService;

  @Test
  void testGivenPersonDTOThenReturnSavedMessage() {
    PersonDTO personDTO = PersonUtils.createFakeDTO();
    Person expectedSavedPerson = PersonUtils.createFakeEntity();

    Mockito.when(this.personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);

    MessageResponseDTO expectedSuccessMessage = createExpectedMessageResponse(expectedSavedPerson.getId(),
        "Created person with ID ");

    MessageResponseDTO successMessage = this.personService.save(personDTO);

    Assertions.assertEquals(expectedSuccessMessage, successMessage);
  }

  @Test
  void testGivenNoDataThenListAllPerson() {
    List<Person> expectedRegisteredPersons = Collections.singletonList(PersonUtils.createFakeEntity());
    PersonDTO personDTO = PersonUtils.createFakeDTO();

    Mockito.when(this.personRepository.findAll()).thenReturn(expectedRegisteredPersons);

    List<PersonDTO> expectedPersonDTOList = this.personService.listAll();

    Assertions.assertFalse(expectedPersonDTOList.isEmpty());
    Assertions.assertEquals(expectedPersonDTOList.get(0).getId(), personDTO.getId());
  }

  private MessageResponseDTO createExpectedMessageResponse(Long id, String message) {
    return MessageResponseDTO.builder().message(message + id).build();
  }
}
