package br.com.akrasia.akimob.app.address.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StreetDTO {

    private Long id;
    private LocalityDTO locality;
    private String name;

}
