package br.com.foursales.service;

import br.com.foursales.dto.*;

public interface UserService {
    UserResponseDTO create(UserRequestDTO dto);
    UserResponseDTO updatePassword(UpdatePasswordDTO dto);
    void forgotPassword(ForgotPasswordDTO dto);
    void resetPassword(ResetPasswordDTO dto);
}