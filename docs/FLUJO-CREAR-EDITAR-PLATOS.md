

## FUNCIONALIDAD CREAR/EDITAR UN PLATO EN UN RESTAURANTE (OK)

1. dish-list.html  botón de Crear plato  /dishes/new

2. DishController
    * @GetMapping  /dishes/new

3. dish-form.html
    * name, description, price, type, restaurant, botón Guardar

4. DishController
    * @PostMapping   /dishes

5. dish-detail.html
    * Botón editar   /dishes/edit/{id}

6. DishController
    * @GetMapping    /dishes/edit/{id}

7. dish-form.html
    * aparece con los campos ya rellenos y solo hay que editar el que queramos
    * botón guardar

8. DishControllerTest
    * create
    * edit