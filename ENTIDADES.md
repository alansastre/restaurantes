estado: PENDIENTE, ENPROGRESO, COMPLETADO

* Restaurant
  * Long id
  * String name
  * Double averagePrice
  * Boolean active (default true)
  * foodType: enum (SPANISH, JAPANESE)

* Employee
  * Long id 
  * String nif
  * LocalDate startDate
  * Boolean active 
  * WorkLevel: enum (JUNIOR, SENIOR)
  * Asociaciones:
    * Restaurant restaurant (ManyToOne)
    
* Dish
  * Long id
  * foodType: enum (SPANISH, JAPANESE)
  * String name
  * Double price
  * Boolean active
  * String description
  * String imageUrl (Byte[] o Blob para imagen en base de datos, pero es más complejo)
  * Asociaciones:
    * Restaurant restaurant (ManyToOne)

* Purchase o Booking o Order
  * Long id
  * LocalDateTime dateTime
  * Double totalPrice
  * Asociaciones
    * User user (ManyToOne)
    * List<Dish> dishes (ManyToMany)