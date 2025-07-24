package br.com.foursales.service;

import br.com.foursales.dto.OrderRequestDTO;
import br.com.foursales.dto.OrderResponseDTO;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponseDTO create(OrderRequestDTO dto);
    OrderResponseDTO pay(UUID id);
    List<OrderResponseDTO> listByUser();
}