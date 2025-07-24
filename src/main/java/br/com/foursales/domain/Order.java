package br.com.foursales.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(optional = false)
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Instant createdAt;
    private BigDecimal totalValue;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.totalValue = items.stream()
                .map(i -> i.getUnitPrice().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}