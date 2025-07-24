package br.com.foursales.dto;

import br.com.foursales.domain.Category;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ProductResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private int stock;
    private Instant createdAt;
    private Instant updatedAt;
}
