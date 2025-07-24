package br.com.foursales.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TopBuyerDTO {
    private String username;
    private BigDecimal totalSpent;
}