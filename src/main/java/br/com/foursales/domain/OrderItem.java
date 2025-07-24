package br.com.foursales.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(optional = false)
    private Order order;
    @ManyToOne(optional = false)
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
}