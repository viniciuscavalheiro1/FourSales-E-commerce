package br.com.foursales.controller;

import br.com.foursales.dto.*;
import br.com.foursales.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Criar novo usuário")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(201).body(userService.create(dto));
    }

    @Operation(summary = "Atualizar senha (usuário autenticado)")
    @PutMapping("/password")
    public ResponseEntity<UserResponseDTO> updatePassword(@RequestBody UpdatePasswordDTO dto) {
        return ResponseEntity.ok(userService.updatePassword(dto));
    }

    @Operation(summary = "Esqueci minha senha")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        userService.forgotPassword(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Resetar senha com token")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(dto);
        return ResponseEntity.ok().build();
    }
}