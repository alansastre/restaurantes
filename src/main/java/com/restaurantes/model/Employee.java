package com.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/*
insert into employees (nif, level) values ('11A', 'JUNIOR');
insert into employees (nif, level) values ('22B', 'SENIOR');
insert into employees (nif, level) values ('33C', 'INVENT');

SELECT * FROM employees;
 */


@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "employees")
@AllArgsConstructor
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String nif;

    @Column(name = "active")
    private Boolean active;

    // Tipo de empleado: JUNIOR o SENIOR con enum
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('JUNIOR', 'SENIOR') DEFAULT 'SENIOR'")
    private WorkLevel level = WorkLevel.SENIOR;

    private LocalDate startDate = LocalDate.now(); // 2026-04-13
    // LocalDate
    // LocalTime
    // LocalDateTime

    @ToString.Exclude // lombok
    @ManyToOne() // JPA
    private Restaurant restaurant;

}