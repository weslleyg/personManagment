package one.digitalinnovation.personapi.services;

import one.digitalinnovation.personapi.dtos.mappers.PersonMapper;
import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.dtos.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entities.Person;
import one.digitalinnovation.personapi.exceptions.PersonNotFoundException;
import one.digitalinnovation.personapi.repositories.PersonRepository;
import one.digitalinnovation.personapi.utils.PersonUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static one.digitalinnovation.personapi.utils.PersonUtils.createFakeDTO;
import static one.digitalinnovation.personapi.utils.PersonUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    PersonDTO personDTO = createFakeDTO();
    Person expectedSavedPerson = createFakeEntity();

    when(this.personMapper.toModel(personDTO)).thenReturn(expectedSavedPerson);
    when(this.personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);

    MessageResponseDTO expectedSuccessMessage = createExpectedMessageResponse(expectedSavedPerson.getId(),
        "Created person with ID ");

    MessageResponseDTO successMessage = this.personService.save(personDTO);

    assertEquals(expectedSuccessMessage, successMessage);
  }

  @Test
  void testGivenNoDataThenListAllPerson() {
    List<Person> expectedRegisteredPersons = Collections.singletonList(PersonUtils.createFakeEntity());
    PersonDTO personDTO = createFakeDTO();

    when(this.personRepository.findAll()).thenReturn(expectedRegisteredPersons);
    when(this.personMapper.toDTO(expectedRegisteredPersons.get(0))).thenReturn(personDTO);

    List<PersonDTO> expectedPersonDTOList = this.personService.listAll();

    assertFalse(expectedPersonDTOList.isEmpty());
    assertEquals(expectedPersonDTOList.get(0).getId(), personDTO.getId());
  }

  @Test
  void testGivenValidPersonIdThenReturnThisPerson() throws PersonNotFoundException {
    Person expectedPerson = createFakeEntity();
    PersonDTO expectedPersonDTO = createFakeDTO();
    expectedPersonDTO.setId(expectedPerson.getId());

    when(this.personRepository.findById(expectedPerson.getId())).thenReturn(Optional.of(expectedPerson));
    when(this.personMapper.toDTO(expectedPerson)).thenReturn(expectedPersonDTO);

    PersonDTO personDTO = this.personService.listById(expectedPerson.getId());

    assertEquals(expectedPersonDTO, personDTO);

    assertEquals(expectedPerson.getId(), personDTO.getId());
    assertEquals(expectedPerson.getFirstName(), personDTO.getFirstName());
  }

  @Test
  void testGivenInvalidPersonIdThenThrowException() {
    var invalidPersonId = 1L;

    when(this.personRepository.findById(invalidPersonId)).thenReturn(Optional.ofNullable(any(Person.class)));

    assertThrows(PersonNotFoundException.class, () -> this.personService.listById(invalidPersonId));
  }

  @Test
  void testGivenValidPersonIdAndUpdateThenReturnSuccessOnUpdate() throws PersonNotFoundException {
    var updatedPersonId = 2L;

    PersonDTO updatePersonDTORequest = createFakeDTO();
    updatePersonDTORequest.setId(updatedPersonId);
    updatePersonDTORequest.setLastName("Pereira");

    Person expectedPersonToUpdate = createFakeEntity();
    expectedPersonToUpdate.setId(updatedPersonId);

    Person expectedPersonUpdated = createFakeEntity();
    expectedPersonUpdated.setId(updatedPersonId);
    expectedPersonToUpdate.setLastName(updatePersonDTORequest.getLastName());

    when(this.personRepository.findById(updatedPersonId)).thenReturn(Optional.of(expectedPersonUpdated));
    when(this.personMapper.toModel(updatePersonDTORequest)).thenReturn(expectedPersonUpdated);
    when(this.personRepository.save(any(Person.class))).thenReturn(expectedPersonUpdated);

    MessageResponseDTO successMessage = this.personService.update(updatedPersonId, updatePersonDTORequest);

    MessageResponseDTO expectedSuccessMessage = createExpectedMessageResponse(expectedPersonUpdated.getId(),
        "Updated person with ID ");

    assertEquals(expectedSuccessMessage, successMessage);

  }

  @Test
  void testGivenInvalidPersonIdAndUpdateThenThrowExceptionOnUpdate() throws PersonNotFoundException {
    var invalidPersonId = 1L;

    PersonDTO updatePersonRequest = createFakeDTO();
    updatePersonRequest.setId(invalidPersonId);
    updatePersonRequest.setLastName("Tester");

    when(this.personRepository.findById(invalidPersonId)).thenReturn(Optional.ofNullable(any(Person.class)));

    assertThrows(PersonNotFoundException.class, () -> this.personService.update(invalidPersonId, updatePersonRequest));
  }

  @Test
  void testGivenPersonIdThenReturnSuccessOnDelete() throws PersonNotFoundException {
    var deletePersonId = 1L;

    Person expectedPersonToDelete = createFakeEntity();

    when(this.personRepository.findById(deletePersonId)).thenReturn(Optional.of(expectedPersonToDelete));
    this.personService.delete(deletePersonId);

    verify(this.personRepository, times(1)).deleteById(deletePersonId);
  }

  @Test
  void testGivenInvalidPersonIdThenThrowExceptionOnDelete() throws PersonNotFoundException {
    var invalidPersonId = 1L;

    when(this.personRepository.findById(invalidPersonId)).thenReturn(Optional.ofNullable(any(Person.class)));

    assertThrows(PersonNotFoundException.class, () -> personService.delete(invalidPersonId));
  }

  private MessageResponseDTO createExpectedMessageResponse(Long id, String message) {
    return MessageResponseDTO.builder().message(message + id).build();
  }
}
