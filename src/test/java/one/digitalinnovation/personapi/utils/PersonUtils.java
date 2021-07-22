package one.digitalinnovation.personapi.utils;

import java.time.LocalDate;
import java.util.Collections;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.entities.Person;

public class PersonUtils {
  private static final String FIRST_NAME = "Giulio";
  private static final String LAST_NAME = "Santos";
  private static final String CPF_NUMBER = "915.271.720-82";
  private static final long PERSON_ID = 1L;
  public static final LocalDate BIRTH_DATE = LocalDate.of(2000, 9, 17);

  public static PersonDTO createFakeDTO() {
    return PersonDTO.builder().firstName(FIRST_NAME).lastName(LAST_NAME).cpf(CPF_NUMBER).birthDate("17-09-2000")
        .phones(Collections.singletonList(PhoneUtils.createFakeDTO())).build();
  }

  public static Person createFakeEntity() {
    return Person.builder().id(PERSON_ID).firstName(FIRST_NAME).lastName(LAST_NAME).cpf(CPF_NUMBER)
        .birthDate(BIRTH_DATE).phones(Collections.singletonList(PhoneUtils.createFakeEntity())).build();
  }

  public static String asJsonString(PersonDTO personDTO) {
    try {

      ObjectMapper object = new ObjectMapper();

      object.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      object.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      object.registerModules(new JavaTimeModule());

      return object.writeValueAsString(personDTO);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
