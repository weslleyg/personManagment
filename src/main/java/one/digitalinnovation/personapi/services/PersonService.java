package one.digitalinnovation.personapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.dtos.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entities.Person;
import one.digitalinnovation.personapi.mappers.PersonMapper;
import one.digitalinnovation.personapi.repositories.PersonRepository;

@Service
public class PersonService {
  PersonRepository personRepository;
  private final PersonMapper personMapper = PersonMapper.INSTANCE;

  @Autowired
  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public MessageResponseDTO savePerson(PersonDTO personDTO) {

    Person personToSave = personMapper.toModel(personDTO);

    Person savedPerson = this.personRepository.save(personToSave);

    return MessageResponseDTO.builder().message("Created person with ID " + savedPerson.getId()).build();
  }
}
