


### Opción 1:

requiere crear y guardar user en el setUp():

```java
user = userRepository.save(
                User.builder().username("user").email("user@gmail.com")
                        .password(passwordEncoder.encode("user"))
                        .role(Role.ROLE_USER)
                .build());
```

En el mockMvc añadir al get o post:

```java
.with(user(user))
.with(csrf())
```


## Opción 2:

src/test/resources/test-data.sql

```sql
INSERT INTO `users` (`username`, `email`, `role`, `password`)
VALUES ( 'user0', 'user0@gmail.com', 'ROLE_USER', '$2a$10$MWDku4iQNldnuK.ilK6vNuBXGs3/VijTd5GX3OA/pxatR/TAmhSPW');
```

En el method de test:

```java
@WithUserDetails("user0")
@Sql("/test-data.sql") 
```

y en el mockMvc:

```java
 .with(csrf())
```