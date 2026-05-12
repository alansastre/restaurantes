

## FUNCIONALIDAD ESCRIBIR RESEÑA SOBRE RESTAURANTE O PLATO

1. Botón Añadir reseña
   * restaurant-detail.html
   * dish-detail.html

2. ReviewController
   * @GetMapping  /reviews/new?restaurantId=1
   * @GetMapping  /reviews/new?dish=1
   * @GetMapping /reviews/edit/1

3. review-form.html: titulo, mensaje, rating (1-5), botón enviar

4. ReviewController
   * @PostMapping("reviews")  reviewRepository.save(review) y redirect a detail

5. ReviewControllerTest
   * create   mockMvc.perform(post("/reviews"))
   * edit   mockMvc.perform(post("/reviews"))