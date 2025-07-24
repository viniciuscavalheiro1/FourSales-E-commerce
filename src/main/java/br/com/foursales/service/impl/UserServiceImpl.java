package br.com.foursales.service.impl;

import br.com.foursales.domain.PasswordResetToken;
import br.com.foursales.domain.RoleEntity;
import br.com.foursales.domain.User;
import br.com.foursales.dto.*;
import br.com.foursales.exception.*;
import br.com.foursales.repository.PasswordResetTokenRepository;
import br.com.foursales.repository.RoleRepository;
import br.com.foursales.repository.UserRepository;
import br.com.foursales.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordResetTokenRepository tokenRepo,
                           PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepo = tokenRepo;
        this.encoder = encoder;
    }

    @Override
    public UserResponseDTO create(UserRequestDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Usuário já existe");
        }
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword()));
        Set<RoleEntity> roles = dto.getRoles().stream()
                .map(roleName -> roleRepository.findById(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Role não existe: " + roleName)))
                .collect(Collectors.toSet());
        u.setRoles(roles);
        User saved = userRepository.save(u);
        return new UserResponseDTO(saved.getId(), saved.getUsername());
    }

    @Override
    public UserResponseDTO updatePassword(UpdatePasswordDTO dto) {
        User user = getCurrentUser();
        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Senha atual inválida");
        }
        user.setPassword(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getUsername());
    }

    @Override
    public void forgotPassword(ForgotPasswordDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        String token = UUID.randomUUID().toString();
        PasswordResetToken prt = new PasswordResetToken();
        prt.setUser(user);
        prt.setToken(token);
        prt.setExpiryDate(Instant.now().plusSeconds(3600));
        tokenRepo.save(prt);
        // TODO: enviar token por e-mail se der tempo
    }

    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        PasswordResetToken prt = tokenRepo.findByToken(dto.getToken())
                .orElseThrow(() -> new TokenNotFoundException("Token inválido"));
        if (prt.getExpiryDate().isBefore(Instant.now()))
            throw new TokenExpiredException("Token expirado");
        User user = prt.getUser();
        user.setPassword(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        tokenRepo.delete(prt);
    }

    private User getCurrentUser() {
        throw new UnsupportedOperationException();
    }
}