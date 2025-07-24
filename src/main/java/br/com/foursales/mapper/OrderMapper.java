package br.com.foursales.mapper;

import br.com.foursales.domain.Order;
import br.com.foursales.dto.OrderItemDTO;
import br.com.foursales.dto.OrderResponseDTO;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default OrderResponseDTO toDto(Order order) {
        if (order == null) {
            return null;
        }
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus().name());
        dto.setCreatedAt(order.getCreatedAt());
        BigDecimal total = order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalValue(total);
        List<OrderItemDTO> items = order.getItems().stream()
                .map(item -> {
                    OrderItemDTO itemDto = new OrderItemDTO();
                    itemDto.setProductId(item.getProduct().getId());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setUnitPrice(item.getUnitPrice());
                    return itemDto;
                })
                .collect(Collectors.toList());
        dto.setItems(items);
        return dto;
    }
}
