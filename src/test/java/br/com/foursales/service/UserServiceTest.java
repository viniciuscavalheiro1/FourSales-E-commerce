package br.com.foursales.service;

import br.com.foursales.domain.PasswordResetToken;
import br.com.foursales.domain.RoleEntity;
import br.com.foursales.domain.User;
import br.com.foursales.dto.*;
import br.com.foursales.exception.*;
import br.com.foursales.repository.PasswordResetTokenRepository;
import br.com.foursales.repository.RoleRepository;
import br.com.foursales.repository.UserRepository;
import br.com.foursales.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordResetTokenRepository tokenRepository;
    @Mock private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldPersistUserAndReturnResponse() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("vinicius");
        dto.setPassword("123");
        dto.setRoles(Set.of("USER"));

        RoleEntity role = new RoleEntity();
        User saved = new User();
        saved.setId(UUID.randomUUID());
        saved.setUsername("vinicius");

        when(userRepository.findByUsername("vinicius")).thenReturn(Optional.empty());
        when(roleRepository.findById("USER")).thenReturn(Optional.of(role));
        when(encoder.encode("123")).thenReturn("encoded123");
        when(userRepository.save(any())).thenReturn(saved);

        UserResponseDTO response = userService.create(dto);

        assertNotNull(response);
        assertEquals("vinicius", response.getUsername());
        verify(userRepository).save(any());
    }

    @Test
    void create_whenUsernameExists_shouldThrow() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("existing");

        when(userRepository.findByUsername("existing")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(dto));
    }

    @Test
    void forgotPassword_shouldGenerateAndSaveToken() {
        User user = new User();
        user.setUsername("vinicius");

        when(userRepository.findByUsername("vinicius")).thenReturn(Optional.of(user));

        ForgotPasswordDTO dto = new ForgotPasswordDTO();
        dto.setUsername("vinicius");

        userService.forgotPassword(dto);

        verify(tokenRepository).save(any());
    }

    @Test
    void forgotPassword_whenUserNotFound_shouldThrow() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO();
        dto.setUsername("inexistente");

        when(userRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.forgotPassword(dto));
    }

    @Test
    void resetPassword_shouldUpdateUserPassword() {
        User user = new User();
        user.setUsername("vinicius");

        PasswordResetToken token = new PasswordResetToken();
        token.setToken("abc123");
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusSeconds(3600));

        when(tokenRepository.findByToken("abc123")).thenReturn(Optional.of(token));
        when(encoder.encode("newpass")).thenReturn("encodedNew");

        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setToken("abc123");
        dto.setNewPassword("newpass");

        userService.resetPassword(dto);

        verify(userRepository).save(user);
        verify(tokenRepository).delete(token);
    }

    @Test
    void resetPassword_whenTokenExpired_shouldThrow() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("expired");
        token.setExpiryDate(Instant.now().minusSeconds(10));

        when(tokenRepository.findByToken("expired")).thenReturn(Optional.of(token));

        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setToken("expired");
        dto.setNewPassword("pass");

        assertThrows(TokenExpiredException.class, () -> userService.resetPassword(dto));
    }

    @Test
    void resetPassword_whenTokenNotFound_shouldThrow() {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setToken("notfound");

        when(tokenRepository.findByToken("notfound")).thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class, () -> userService.resetPassword(dto));
    }
}