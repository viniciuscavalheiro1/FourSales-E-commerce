package br.com.foursales.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AverageTicketDTO {
    private String username;
    private BigDecimal averageTicket;
}