package br.com.akrasia.akimob.client.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCreateDTO {

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

}
