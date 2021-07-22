package one.digitalinnovation.personapi.controllers;

import static one.digitalinnovation.personapi.utils.PersonUtils.asJsonString;
import static one.digitalinnovation.personapi.utils.PersonUtils.createFakeDTO;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.dtos.response.MessageResponseDTO;
import one.digitalinnovation.personapi.exceptions.PersonNotFoundException;
import one.digitalinnovation.personapi.services.PersonService;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

  private static final String PERSON_API_URL_PATH = "/api/v1/person";

  private PersonController personController;

  private MockMvc mockMvc;

  @Mock
  private PersonService personService;

  @BeforeEach
  void setUp() {
    this.personController = new PersonController(this.personService);
    mockMvc = MockMvcBuilders.standaloneSetup(this.personController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView()).build();
  }

  @Test
  void testWhenPostIsCalledThenAPersonShouldBeCreated() throws Exception {
    PersonDTO expectedPersonDTO = createFakeDTO();
    MessageResponseDTO expectedResponseMessage = createMessageResponse("Created Person with ID ", 1L);

    when(this.personService.save(expectedPersonDTO)).thenReturn(expectedResponseMessage);

    mockMvc
        .perform(
            post(PERSON_API_URL_PATH).contentType(MediaType.APPLICATION_JSON).content(asJsonString(expectedPersonDTO)))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
  }

  @Test
  void testWhenGetIsCalledThenReturnAPersonList() throws Exception {
    var expectedValidId = 1L;
    PersonDTO expectedPersonDTO = createFakeDTO();
    expectedPersonDTO.setId(expectedValidId);

    List<PersonDTO> expectedPersonDTOList = Collections.singletonList(expectedPersonDTO);

    when(this.personService.listAll()).thenReturn(expectedPersonDTOList);

    mockMvc.perform(get(PERSON_API_URL_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id", is(1))).andExpect(jsonPath("$[0].firstName", is("Giulio")))
        .andExpect(jsonPath("$[0].lastName", is("Santos")));
  }

  @Test
  void testWhenGetWithValidIdIsCalledThenReturnAPerson() throws Exception {
    var expectedValidId = 1L;
    PersonDTO expectedPersonDTO = createFakeDTO();
    expectedPersonDTO.setId(expectedValidId);

    when(this.personService.listById(expectedValidId)).thenReturn(expectedPersonDTO);

    mockMvc.perform(get(PERSON_API_URL_PATH + "/" + expectedValidId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.firstName", is("Giulio")))
        .andExpect(jsonPath("$.lastName", is("Santos")));
  }

  @Test
  void testWhenGetWithInvalidIdIsCalledThenReturnAErrorMessage() throws Exception {
    var expectedValidId = 1L;
    PersonDTO expectedPersonDTO = createFakeDTO();
    expectedPersonDTO.setId(expectedValidId);

    when(this.personService.listById(expectedValidId)).thenThrow(PersonNotFoundException.class);

    mockMvc.perform(get(PERSON_API_URL_PATH + "/" + expectedValidId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void testWhenPutIsCalledThenAPersonShouldBeUpdated() throws Exception {
    var expectedValidId = 1L;
    PersonDTO expectedPersonDTO = createFakeDTO();
    MessageResponseDTO expectedResponseMessage = createMessageResponse("Updated Person with ID ", 1L);

    when(this.personService.update(expectedValidId, expectedPersonDTO)).thenReturn(expectedResponseMessage);

    mockMvc
        .perform(put(PERSON_API_URL_PATH + "/" + expectedValidId).contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(expectedPersonDTO)))
        .andExpect(status().isOk()).andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
  }

  @Test
  void testWhenDeleteIsCalledThenAPersonShouldBeDeleted() throws Exception {
    var expectedValidId = 1L;

    mockMvc.perform(delete(PERSON_API_URL_PATH + "/" + expectedValidId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  private MessageResponseDTO createMessageResponse(String message, Long id) {
    return MessageResponseDTO.builder().message(message + id).build();
  }
}
