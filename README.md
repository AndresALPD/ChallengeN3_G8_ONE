# 📚 Challenge #3 - Literalura

Este es un proyecto desarrollado como parte del **Programa Oracle Next Education (ONE) - Alura Latam**, correspondiente al **Challenge #3** de la especialización en Java. El objetivo principal fue consumir la API pública de Gutendex para buscar y registrar libros en una base de datos, aplicando buenas prácticas de arquitectura y desarrollo en Java.

---

## 🚀 Objetivos del proyecto

- Buscar libros utilizando la API [Gutendex](https://gutendex.com/)
- Registrar libros seleccionados en una base de datos PostgreSQL
- Implementar una aplicación de consola interactiva en Java
- Aplicar principios de POO, DTOs, persistencia con JPA/Hibernate, e inyección de dependencias con Spring
- Filtrar y listar libros por idioma, autor, y año de publicación

---

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Web (RestClient)**
- **PostgreSQL**
- **Lombok**
- **Maven**
- **IntelliJ IDEA**

---

## ⚙️ Estructura del proyecto

literalura/
├── src/
│ └── main/
│ ├── java/com/andres/literalura/
│ │ ├── api/ → Cliente RestClient para Gutendex
│ │ ├── config/ → Configuración de RestClient
│ │ ├── dto/ → Clases DTO (BookDTO, AuthorDTO, ResponseDTO)
│ │ ├── menu/ → Menú de consola (interfaz de usuario)
│ │ ├── model/ → Entidades JPA (Book, Author)
│ │ ├── repository/ → Repositorios Spring Data JPA
│ │ ├── service/ → Lógica de negocio (BookService, AuthorService)
│ │ └── LiteraluraApplication.java
│
└── resources/
├── application.properties → Configuración de base de datos PostgreSQL

---

## 🧠 Funcionalidades implementadas

✔️ Buscar libros por título desde la API Gutendex  
✔️ Mostrar resultados con título, autor, idioma y cantidad de descargas  
✔️ Guardar libro y autor en base de datos (relación muchos-a-muchos)  
✔️ Listar libros registrados en la base de datos  
✔️ Filtrar libros por idioma, año o autor  
✔️ Evitar duplicados al guardar  
✔️ Persistencia en PostgreSQL

---

## 🖥️ Ejecución del proyecto

1. Clona el repositorio o descarga el código fuente.
2. Asegúrate de tener **PostgreSQL** instalado y en ejecución.
3. Crea una base de datos llamada `literalura`.
4. Configura tu `application.properties` con las credenciales correctas:
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
5. Abre el proyecto con **IntelliJ IDEA**.
6. Ejecuta la clase `LiteraluraApplication.java`.
7. Interactúa con el menú de consola para buscar y registrar libros.

---

## 📄 Ejemplo de uso

==== LITERALURA ====

Buscar libro por título

Listar libros guardados

Listar autores por idioma

Listar libros por año de publicación

Salir

Seleccione una opción:


---

## 🗃️ Base de datos

Se utiliza **PostgreSQL** como base de datos relacional para almacenar libros y autores. El acceso se configura en `application.properties`.

Parámetros sugeridos:
Base de datos: literalura
Usuario: postgres
Contraseña: tu_contraseña
Puerto: 5432

---
