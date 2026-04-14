package com.restaurantes.repository;

import com.restaurantes.model.Employee;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.WorkLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;
import java.util.List;

// https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html
// findBy lo solemos cuando queremos un solo objeto
// findAllBy lo solemos cuando queremos una lista
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


  List<Employee> findAllByLevel(WorkLevel level);

    List<Employee> findAllByActive(Boolean active);

    List<Employee> findByRestaurant_Name(String name);



    // crear sentencias personalizadas
    @Query(value = "select (current_date - e.startDate) from Employee e where e.nif = ?1")
    Duration findWorkDaysByNif(String nif);

    List<Employee> findAllByActiveTrue();

    List<Employee> findAllByActiveTrueAndRestaurantName(String name);

}
