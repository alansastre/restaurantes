package com.restaurantes.repository;

import com.restaurantes.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// JPQL
// https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-querylanguage/persistence-querylanguage.html
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurantId(Long id);
    List<Order> findByUser_Id(Long id);

    // Calcular precio total de un pedido en base a sus líneas de pedido
    // calculate total price based on order lines
    // JPQL
    @Query("""
    select COALESCE(sum(lineaPedido.dish.price * lineaPedido.quantity), 0.0)
    from OrderLine lineaPedido
    where lineaPedido.order.id = ?1
    """)
    double calculateTotalPrice(Long orderId);
}