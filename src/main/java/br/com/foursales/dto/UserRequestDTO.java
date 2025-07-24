package br.com.foursales.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Size(min = 1)
    private Set<String> roles;
}