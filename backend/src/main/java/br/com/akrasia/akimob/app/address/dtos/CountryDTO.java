package br.com.akrasia.akimob.app.address.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CountryDTO {

    private Short id;
    private Short isoNumeric;
    private String iso2;
    private String name;

}
