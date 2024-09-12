package br.com.akrasia.akimob.address.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdministrativeAreaDTO {

    private Integer id;
    private CountryDTO country;
    private String name;

}
