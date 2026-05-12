
## PROCESO DE INICIAR Y FINALIZAR UN PEDIDO

Como cliente de un restaurante quiero iniciar un pedido, pedir platos de la carta y finalizar pedido.


1. restaurant-detail.html botón "Hacer pedido" 

2. OrderController
    * @GetMapping   /orders/new?restaurantId=1

3. order-form.html
   * numero comensales
   * numero mesa
   * observaciones/alergias/sugerencias

4. order-detail.html
    * mostrar platos y botón añadir platos al pedido

5. OrderController
    * @PostMapping /orders/{id}/lines

6. Finalizar pedido
    * @GetMapping /orders/{id}/finish

7. order-detail.html

8. OrderControllerTest
    * crear pedido
    * añadir platos a pedido
    * finalizar pedido

