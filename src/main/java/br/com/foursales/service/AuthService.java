package br.com.foursales.service;

import br.com.foursales.dto.AuthRequestDTO;
import br.com.foursales.dto.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO authenticate(AuthRequestDTO dto);
}