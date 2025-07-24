package br.com.foursales.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MonthlyRevenueDTO {
    private BigDecimal revenue;
    public MonthlyRevenueDTO(BigDecimal revenue) { this.revenue = revenue; }
}