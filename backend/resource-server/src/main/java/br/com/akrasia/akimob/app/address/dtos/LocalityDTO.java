package br.com.akrasia.akimob.app.address.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocalityDTO {

    private Integer id;
    private AdministrativeAreaDTO administrativeArea;
    private String name;

}
