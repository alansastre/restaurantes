package com.restaurantes.model;

import com.restaurantes.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "orders")
@AllArgsConstructor
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date = LocalDateTime.now();

    private Integer tableNumber;

    private Integer numPeople;

    // debería calcularse en base a sus OrderLine
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDIENTE;

    // asociaicon con restaurante?
    @ToString.Exclude // lombok
    @ManyToOne // JPA
    private Restaurant restaurant;
}
