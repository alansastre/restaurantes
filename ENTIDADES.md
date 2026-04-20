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
    
* Paso 1: en el paquete model crear un nuevo archivo class Dish desde Intellij IDEA [OK]
  * Dish [OK]
    * Long id
    * DishType dishType: enum (STARTER, MAIN_COURSE, DESSERT)
    * String name
    * Double price
    * Boolean active
    * String description (poner longitud 500)
    * String imageUrl
    * Asociaciones:
      * Restaurant restaurant (@ManyToOne)

* Paso 2:  [OK]
  * crear repositorio DishRepository
    * métodos nuevos de consulta:
      * Filtrar los platos de un restaurante por id de restaurante
      * Filtrar los platos de precio menor que
      * Traer platos ordenados por precio ASCendente

+ Paso 3: [OK]
  + crear test DishRepositoryTest
  + beforeEach para crear 4 platos de prueba
  + test para los nuevos métodos de consulta

* Customer [NUEVA]
  * Long id
  * String username

* Order [OK] + OrderRepository [OK]
  * Long id
  * LocalDateTime date
  * Integer tableNumber
  * Integer numPeople
  * OrderStatus: enum (OPEN, CLOSED, PAID, CANCELLED)
  * Double totalPrice
  * Asociaciones:
    * Restaurant restaurant (ManyToOne)

* OrderRepositoryTest [OK]
  * repasar el calculateTotalPrice [PENDIENTE TESTEAR CON DATOS REALES]

* OrderLine [OK] + OrderLineRepository [OK]
  * Long id
  * Integer quantity
  * Asociaciones:
    * Dish dish (ManyToOne)
    * Order order (ManyToOne)



## Pasos 

* Entidad Order (en paquete model)
* Repositorio OrderRepository (en paquete repository)
  * opcionalmente: métodos de consulta personalizados
* Test OrderRepositoryTest


* OrderController (en paquete controller, Controlador MVC HTML)
  * localhost:8080/pedidos --> @GetMappint("/pedidos") --> finAll() --> H2
* HTML: pedidos.html


* OrderService (en paquete service)
  * lógica de negocio para crear un pedido nuevo, añadir platos a un pedido, cerrar un pedido, etc.
  * OrderServiceTest

* OrderRestController (controlador API REST JSON)


## Controladores MVC

Los controladores MVC son clases que reciben peticiones HTTP del navegador, consultan datos en la base de datos y los muestran en una página HTML.

Tipos de mapping:

* @GetMapping("/ruta") — para mostrar datos (HTTP GET)
* @PostMapping("/ruta") — para enviar datos (HTTP POST)
* @GetMapping("/ruta/{id}") — para mostrar datos de un elemento específico (HTTP GET con path variable)
* @PutMapping("/ruta/{id}") — para actualizar datos de un elemento específico (HTTP PUT con path variable)
* @DeleteMapping("/ruta/{id}") — para eliminar un elemento específico (HTTP DELETE con path variable)

### Paso 1: Crear clase Controller

Ejemplo conceptual primero:

[ok] Crear paquete controller en com.restaurantes


* [ok] Crear clase HelloController
* [ok] Anotarla con @Controller
* [ok] Crear método hello() con @GetMapping("/hello")
* [ok] En el método hello(), devolver el nombre del template HTML (hello)

* Si queremos ver los cambios hay que reiniciar la aplicación spring boot.
* Si la carpeta src/main/resources/templates no existe, hay que crearla.
* IMPORTANTE: tenemos que tener en el pom.xml
  * spring-boot-starter-thymeleaf
    * spring-boot-starter-webmvc

2. [ok] Crear clase RestaurantController
   * [ok] Anotarla con @Controller
   * [ok] Inyectar el RestaurantRepository con constructor
   * [ok] Crear método findAll() con @GetMapping("/restaurants")
   * [ok] En el método findAll(), usar el Model para guardar la lista de restaurantes y
   * [ok] Devolver el nombre del template HTML (restaurant-list)


3. Insertar datos demo:
* Vía SQL
* Vía CommandLineRunner
* Vía Main

Documentación de Thymeleaf

https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html#standard-expression-syntax