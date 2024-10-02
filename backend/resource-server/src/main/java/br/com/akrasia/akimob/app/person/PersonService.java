package br.com.akrasia.akimob.app.person;

import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.app.address.AddressService;
import br.com.akrasia.akimob.app.person.dtos.LegalEntityCreateDTO;
import br.com.akrasia.akimob.app.person.dtos.NaturalPersonCreateDTO;
import br.com.akrasia.akimob.app.person.dtos.PersonCreateDTO;
import br.com.akrasia.akimob.app.person.dtos.PersonResponseDTO;
import br.com.akrasia.akimob.app.person.entities.LegalEntity;
import br.com.akrasia.akimob.app.person.entities.NaturalPerson;
import br.com.akrasia.akimob.app.person.entities.Person;
import br.com.akrasia.akimob.app.person.entities.Person.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;
    private final AddressService addressService;
    
    public PersonResponseDTO createPerson(PersonCreateDTO personCreateDTO) {
        log.info("Creating person: {}", personCreateDTO.getName());

        Person person;
        switch (personCreateDTO.getType()) {
            case Type.NATURAL:
                person = createNaturalPerson(personCreateDTO);
                break;
            case Type.LEGAL:
                person = createLegalEntity(personCreateDTO);
                break;
            default:
                log.error("Invalid person type: {}", personCreateDTO.getType());
                throw new IllegalArgumentException("Invalid person type");
        }

        person.setName(personCreateDTO.getName());
        person.setAddress(addressService.findOrCreateAddress(personCreateDTO.getAddress()));
        person.setPhoneNumber(personCreateDTO.getPhoneNumber());
        person.setEmail(personCreateDTO.getEmail());
        // person.setPhotoUrl(personCreateDTO.getPhotoUrl());
        person.setObservation(personCreateDTO.getObservation());

        Person savedPerson = personRepository.save(person);
        log.info("Person created: {}", savedPerson.getId());
        return new PersonResponseDTO(savedPerson);
    }

    private Person createNaturalPerson(PersonCreateDTO personCreateDTO) {
        NaturalPerson naturalPerson = new NaturalPerson();
        naturalPerson.setType(personCreateDTO.getType());
        NaturalPersonCreateDTO naturalPersonCreateDTO = personCreateDTO.getNaturalPerson();

        if (naturalPersonCreateDTO == null) {
            return naturalPerson;
        }

        naturalPerson.setPersonalIdentifier(naturalPersonCreateDTO.getPersonalIdentifier());
        naturalPerson.setGender(naturalPersonCreateDTO.getGender());
        naturalPerson.setBirthdate(naturalPersonCreateDTO.getBirthdate());        

        return naturalPerson;
    }

    private Person createLegalEntity(PersonCreateDTO personCreateDTO) {
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setType(personCreateDTO.getType());
        LegalEntityCreateDTO legalEntityCreateDTO = personCreateDTO.getLegalEntity();

        if (legalEntityCreateDTO == null) {
            return legalEntity;
        }

        legalEntity.setBusinessIdentifier(legalEntityCreateDTO.getBusinessIdentifier());
        legalEntity.setRegisteredName(legalEntityCreateDTO.getRegisteredName());

        return legalEntity;
    }

}
