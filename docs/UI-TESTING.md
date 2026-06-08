

 ## Paso 1: Agregar dependencia Selenium al pom.xml (OK)

https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java

pom.xml:

```xml
<properties>
    <java.version>25</java.version>
    <selenium.version>4.44.0</selenium.version> 
</properties>
```

```xml
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <scope>test</scope>
</dependency>
```

## Paso 2: crear un test sencillo para verificar funcionamiento (OK)

src/test/java/com/restaurantes/ui/HelloSeleniumTest.java

## Paso 3: Clase Base

BaseSeleniumTest

## Paso 4: Primer test real RestaurantSeleniumTest.java