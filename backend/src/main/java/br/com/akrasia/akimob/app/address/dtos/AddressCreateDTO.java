package br.com.akrasia.akimob.app.address.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressCreateDTO {

    private Long id;
    private StreetDTO street;
    private String addressNumber;
    private String complement;
    private String postalCode;

}
