package br.com.foursales.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequestDTO {
    private List<OrderItemDTO> items;
}