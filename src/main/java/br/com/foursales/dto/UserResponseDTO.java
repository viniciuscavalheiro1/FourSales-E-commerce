package br.com.foursales.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Data
public class UserResponseDTO {
    private UUID id;
    private String username;

    public UserResponseDTO(UUID id, String username) {
        this.id = id;
        this.username = username;
    }
}
