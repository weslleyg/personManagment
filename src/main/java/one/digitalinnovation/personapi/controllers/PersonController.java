package one.digitalinnovation.personapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import one.digitalinnovation.personapi.dtos.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entities.Person;
import one.digitalinnovation.personapi.repositories.PersonRepository;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {

  private PersonRepository personRepository;

  @Autowired
  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @PostMapping
  public MessageResponseDTO create(@RequestBody Person person) {
    Person savedPerson = this.personRepository.save(person);
    return MessageResponseDTO.builder().message("Created person with ID " + savedPerson.getId()).build();
  }

  @GetMapping
  public String list() {
    return "Hello world";
  }
}
