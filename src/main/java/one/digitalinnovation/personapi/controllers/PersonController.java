package one.digitalinnovation.personapi.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.dtos.response.MessageResponseDTO;
import one.digitalinnovation.personapi.services.PersonService;

@RestController
@RequestMapping("/api/v1/person")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonController {

  PersonService personService;

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public MessageResponseDTO create(@RequestBody @Valid PersonDTO personDTO) {
    return this.personService.savePerson(personDTO);
  }

  @GetMapping
  public List<PersonDTO> listAll() {
    return this.personService.listAll();
  }
}
