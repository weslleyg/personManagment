package one.digitalinnovation.personapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.dtos.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entities.Person;
import one.digitalinnovation.personapi.mappers.PersonMapper;
import one.digitalinnovation.personapi.repositories.PersonRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

  private PersonRepository personRepository;

  private final PersonMapper personMapper = PersonMapper.INSTANCE;

  public MessageResponseDTO savePerson(PersonDTO personDTO) {

    Person personToSave = personMapper.toModel(personDTO);

    System.out.println(personToSave.getPhones());
    System.out.println(personDTO.getPhones());

    Person savedPerson = this.personRepository.save(personToSave);

    return MessageResponseDTO.builder().message("Created person with ID " + savedPerson.getId()).build();
  }

  public List<PersonDTO> listAll() {
    List<Person> allPersons = this.personRepository.findAll();

    return allPersons.stream().map(personMapper::toDTO).collect(Collectors.toList());
  }
}
