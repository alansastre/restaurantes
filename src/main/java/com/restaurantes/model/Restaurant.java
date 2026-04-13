package com.restaurantes.model;


import jakarta.persistence.*;
import lombok.*;

// agregar las anotaciones como en Book
/*
insert into restaurantes (name) values ('Pacos Bar');
insert into restaurantes (name, average_price, active) values ('Pepe Bar', 25.40, TRUE);
update restaurantes set average_price = 43.21 where id = 3;
update restaurantes set average_price = 43.21;
select * from restaurantes;
delete from restaurantes where id = 1;
 */
@Entity
@Getter // lombok
@Setter // lombok
@ToString // lombok
@NoArgsConstructor // lombok: crea el constructor vacío sin argumentos
@AllArgsConstructor // lombok: crea el constructor con todos los params
@Builder
@Table(name = "restaurantes")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Double averagePrice;

    @Column(columnDefinition = "BOOLEAN DEFAULT true") // por defecto será true
    private Boolean active = true;

    // Tipo comida: SPANISH, JAPANESE, MEXICAN
    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    public Restaurant(String name, Double averagePrice) {
        this.name = name;
        this.averagePrice = averagePrice;
        this.active = true;
    }

    public Restaurant(String name) {
        this.name = name;
    }

}
