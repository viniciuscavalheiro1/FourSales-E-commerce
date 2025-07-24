package br.com.foursales.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemDTO {
    private UUID productId;
    private int quantity;
    private BigDecimal unitPrice;
}