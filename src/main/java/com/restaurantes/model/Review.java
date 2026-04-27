package com.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "reviews")
@AllArgsConstructor
@ToString(exclude = {"restaurant", "dish"})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String content;

    private Integer rating;

    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne
    private Restaurant restaurant;
    @ManyToOne
    private Dish dish;
}
