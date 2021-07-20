package one.digitalinnovation.personapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import one.digitalinnovation.personapi.dtos.request.PersonDTO;
import one.digitalinnovation.personapi.entities.Person;

@Mapper
public interface PersonMapper {
  PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

  @Mapping(target = "birthDate", source = "birthDate", dateFormat = "dd-MM-yyyy")
  Person toModel(PersonDTO personDTO);

  PersonDTO toDTO(Person person);
}
