estado: PENDIENTE, ENPROGRESO, COMPLETADO

* Restaurant [OK]
  * Long id
  * String name
  * Double averagePrice
  * Boolean active (default true)
  * foodType: enum (SPANISH, JAPANESE)

* Employee [OK]
  * Long id 
  * String nif
  * LocalDate startDate
  * Boolean active 
  * WorkLevel: enum (JUNIOR, SENIOR)
  * Asociaciones:
    * Restaurant restaurant (ManyToOne)
    
* Paso 1: en el paquete model crear un nuevo archivo class Dish desde Intellij IDEA
  * Dish [NUEVA]
    * Long id
    * DishType dishType: enum (STARTER, MAIN_COURSE, DESSERT)
    * String name
    * Double price
    * Boolean active
    * String description
    * String imageUrl (Byte[] o Blob para imagen en base de datos, pero es más complejo)
    * Asociaciones:
      * Restaurant restaurant (ManyToOne)

* Paso 2: 
  * crear repositorio DishRepository
    * métodos nuevos de consulta:
      * Filtrar los platos de un restaurante por id de restaurante
      * Filtrar los platos de precio menor que
      * Traer platos ordenados por precio ASCendente

+ Paso 3: 
  + crear test DishRepositoryTest
  + beforeEach para crear 4 platos de prueba
  + test para los nuevos métodos de consulta


* Order [NUEVA]
  * Long id
  * LocalDateTime date
  * Integer tableNumber
  * OrderStatus: enum (OPEN, CLOSED, PAID, CANCELLED)
  * Double totalPrice (default 0.0)
  * Asociaciones:
    * Restaurant restaurant (ManyToOne)

* OrderLine [NUEVA]
  * Long id
  * Integer quantity
  * Double priceAtOrder (precio congelado en el momento del pedido)
  * Asociaciones:
    * Dish dish (ManyToOne)
    * Order order (ManyToOne)