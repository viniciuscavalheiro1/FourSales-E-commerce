package br.com.foursales.dto;

import br.com.foursales.domain.Category;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ProductRequestDTO {

    @NotBlank
    private String name;

    private String description;

    @NotNull @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull
    private Category category;

    @NotNull @Min(0)
    private Integer stock;
}
