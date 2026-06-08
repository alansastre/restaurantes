

## FUNCIONALIDAD ESCRIBIR RESEÑA SOBRE RESTAURANTE O PLATO

1. Botón Añadir reseña OK
   * restaurant-detail.html
   * dish-detail.html

2. ReviewController OK
   * @GetMapping  /reviews/new?restaurantId=1 o /reviews/new?dishId=1

3. review-form.html: titulo, mensaje, rating (1-5), botón enviar OK

4. ReviewController 
   * @PostMapping("reviews")  reviewRepository.save(review) y redirect a detail  OK
   * Corregido revisar error restaurant Bad Request, problema de conversión estaba enviando el objeto entero Restaurant en vez de solo la clave primaria    id STring --> Long

5. ReviewControllerTest OK
   * create   mockMvc.perform(post("/reviews"))
   * edit   mockMvc.perform(post("/reviews"))
   * navigateToNew
   * navigateToEdit