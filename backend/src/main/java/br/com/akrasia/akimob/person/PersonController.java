package br.com.akrasia.akimob.person;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.person.dtos.PersonCreateDTO;
import br.com.akrasia.akimob.person.dtos.PersonResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping
    @PreAuthorize("hasAuthority('person_add')")
    public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody @Valid PersonCreateDTO personCreateDTO) {
        PersonResponseDTO person = personService.createPerson(personCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(person);
    }
}
