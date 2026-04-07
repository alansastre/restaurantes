package com.restaurantes;

import jakarta.persistence.*;
import lombok.Getter;


// agregar las anotaciones como en Book
@Entity
@Getter
@Table(name = "restaurantes")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    Double averagePrice;

    Boolean active;

    public Restaurant(String name, Double averagePrice) {
        this.name = name;
        this.averagePrice = averagePrice;
        this.active = true;
    }

    public Restaurant(String name) {
        this.name = name;
    }

    public Restaurant() {
    }
}
