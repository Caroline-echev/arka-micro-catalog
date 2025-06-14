# Microservicio de Catálogo de Productos (`micro_catalog`)

Este microservicio proporciona funcionalidades para la **gestión de productos**, **categorías** y **marcas**, implementado con arquitectura reactiva basada en Spring WebFlux.

---

##  Características Principales

- **Gestión de productos**: CRUD completo con asociación a marcas y categorías
- **Gestión de categorías**: Creación y administración de categorías de productos
- **Gestión de marcas**: Administración de marcas comerciales
- **Búsqueda paginada**: Filtrado y ordenación de resultados
- **Documentación**: API documentada con Swagger/OpenAPI

---

##  Tecnologías Utilizadas

- **Spring WebFlux** – Manejo reactivo de peticiones
- **R2DBC** – Acceso reactivo a base de datos
- **PostgreSQL** – Base de datos relacional
- **Flyway** – Migraciones de base de datos
- **Swagger/OpenAPI** – Documentación automática
- **Lombok** – Reducción de código boilerplate
- **MapStruct** – Mapeo entre modelos y DTOs

---

## Estructura del Proyecto

micro_catalog/
├── adapters/

│ ├── driving/ # Entrada: Interfaces externas (HTTP)

│ │ └── reactive/

│ │ ├── controller/ # Controladores REST

│ │ ├── dto/ # Objetos de transferencia de datos

│ │ └── mapper/ # Mapeo entre DTOs y modelos de dominio

│ ├── driven/ # Salida: Infraestructura externa

│ │ ├── r2dbc/ # Implementación de persistencia reactiva

│ │ │ ├── adapter/ # Implementación de los puertos SPI

│ │ │ ├── config/ # Configuraciones específicas de R2DBC

│ │ │ ├── entity/ # Entidades JPA para persistencia

│ │ │ ├── mapper/ # Mapeo entre entidades y modelos de dominio

│ │ │ ├── repository/ # Repositorios R2DBC

│ │ │ └── util/ # Utilidades específicas de persistencia

│ │ └── security/ # Implementación de seguridad

│ │ ├── adapter/ # Servicios relacionados a JWT y autenticación

│ │ └── util/ # Funciones utilitarias de seguridad

│

├── configuration/ # Configuración de la aplicación Spring Boot

│ ├── bean/ # Beans personalizados

│ ├── exception/ # Manejadores globales de errores

│ ├── flyway/ # Configuración de migraciones con Flyway

│ ├── security/ # Configuración de seguridad (JWT, filtros)

│ ├── swagger/ # Configuración de documentación Swagger

│ └── util/ # Utilidades generales de configuración

│
├── domain/ # Núcleo del dominio (independiente de frameworks)

│ ├── api/ # Interfaces de servicios (puertos de entrada)

│ ├── exception/ # Excepciones personalizadas del dominio

│ │ └── enums/ # Enumeraciones para tipos de error

│ ├── model/ # Modelos de dominio (entidades, value objects)

│ ├── spi/ # Interfaces de persistencia (puertos de salida)

│ └── usecase/ # Casos de uso (lógica de negocio)

│ └── util/ # Utilidades reutilizables en los casos de uso

│

└── application.properties # Archivo de configuración principal


## Endpoints Principales
Productos (/api/products)
POST / - Crear nuevo producto

GET / - Listar productos paginados

GET /{id} - Obtener producto por ID

PUT /{id} - Actualizar producto

Categorías (/api/categories)
POST / - Crear nueva categoría

GET / - Listar categorías paginadas

GET /{id} - Obtener categoría por ID

PUT /{id} - Actualizar categoría

Marcas (/api/brands)
POST / - Crear nueva marca

GET / - Listar marcas paginadas

GET /{id} - Obtener marca por ID

PUT /{id} - Actualizar marca

