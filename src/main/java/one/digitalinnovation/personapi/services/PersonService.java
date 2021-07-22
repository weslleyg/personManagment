package one.digitalinnovation.personapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.dtos.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entities.Person;
import one.digitalinnovation.personapi.exceptions.PersonNotFoundException;
import one.digitalinnovation.personapi.mappers.PersonMapper;
import one.digitalinnovation.personapi.repositories.PersonRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

  private PersonRepository personRepository;

  private final PersonMapper personMapper = PersonMapper.INSTANCE;

  public MessageResponseDTO save(PersonDTO personDTO) {
    Person personToSave = personMapper.toModel(personDTO);

    Person savedPerson = this.personRepository.save(personToSave);

    return this.createMessageResponseDTO(savedPerson.getId(), "Created person with ID ");
  }

  public List<PersonDTO> listAll() {
    List<Person> allPersons = this.personRepository.findAll();

    return allPersons.stream().map(personMapper::toDTO).collect(Collectors.toList());
  }

  public PersonDTO listById(Long id) throws PersonNotFoundException {
    Person person = this.verifyIfExists(id);

    return this.personMapper.toDTO(person);
  }

  public void delete(Long id) throws PersonNotFoundException {
    this.verifyIfExists(id);

    this.personRepository.deleteById(id);
  }

  public MessageResponseDTO update(Long id, PersonDTO personDTO) throws PersonNotFoundException {
    this.verifyIfExists(id);

    Person personToUpdate = personMapper.toModel(personDTO);

    Person updatedPerson = this.personRepository.save(personToUpdate);

    return this.createMessageResponseDTO(updatedPerson.getId(), "Updated person with ID ");
  }

  private Person verifyIfExists(Long id) throws PersonNotFoundException {
    return this.personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
  }

  private MessageResponseDTO createMessageResponseDTO(Long id, String message) {
    return MessageResponseDTO.builder().message(message + id).build();
  }
}
