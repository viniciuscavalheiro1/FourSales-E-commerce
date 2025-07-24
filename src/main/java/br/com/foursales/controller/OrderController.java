package br.com.foursales.controller;

import br.com.foursales.dto.OrderRequestDTO;
import br.com.foursales.dto.OrderResponseDTO;
import br.com.foursales.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @Operation(summary = "Criar novo pedido (status PENDENTE)")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody OrderRequestDTO dto) {
        return ResponseEntity.status(201).body(orderService.create(dto));
    }

    @Operation(summary = "Processar pagamento do pedido")
    @PostMapping("/{id}/pagamento")
    public ResponseEntity<OrderResponseDTO> pay(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.pay(id));
    }

    @Operation(summary = "Listar pedidos do usuário autenticado")
    @GetMapping
    public List<OrderResponseDTO> listUserOrders() {
        return orderService.listByUser();
    }
}