package br.com.foursales.service.impl;

import br.com.foursales.dto.AuthRequestDTO;
import br.com.foursales.dto.AuthResponseDTO;
import br.com.foursales.domain.User;
import br.com.foursales.exception.InvalidCredentialsException;
import br.com.foursales.config.JwtUtil;
import br.com.foursales.repository.UserRepository;
import br.com.foursales.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder  = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO dto) {
        User user = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponseDTO(token);
    }
}
