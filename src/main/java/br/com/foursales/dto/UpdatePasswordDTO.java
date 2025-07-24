package br.com.foursales.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdatePasswordDTO {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}