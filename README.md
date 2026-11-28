# Artesanías Olafo — Backend de inventario

Backend REST para gestionar categorías y productos terminados de Artesanías Olafo. Hace parte de la evidencia **GA7-220501096-AA3-EV01** del programa ADSO del SENA.

**Aprendiz:** Hans Alejandro Calderón Afanador · **Ficha:** 3336223

## Tecnologías

- Java 21
- Spring Boot 4.2.0
- Maven (el proyecto incluye `mvnw.cmd`)
- MySQL
- API REST con Spring MVC, Spring Data JPA y Jakarta Validation
- Empaquetado WAR

## Funcionalidad

| Recurso               | Tabla                | Ruta base         | Funcionalidad                                                           |
| --------------------- | -------------------- | ----------------- | ----------------------------------------------------------------------- |
| Categoría de producto | `categoria_producto` | `/api/categorias` | Crear, consultar, listar, actualizar y eliminar                         |
| Producto terminado    | `producto_terminado` | `/api/productos`  | Crear, consultar, listar con filtros, actualizar, inactivar y reactivar |

Cada producto pertenece a una categoría mediante `id_categoria_producto`. Una categoría que tenga productos asociados no se puede eliminar. Al eliminar un producto, este se conserva en la base de datos y se marca como inactivo.

## Requisitos y configuración

- JDK 21
- MySQL disponible localmente
- Postman para probar los endpoints

La aplicación escucha en el puerto `8080` y usa por defecto la base de datos `artesanias_olafo`. Antes de iniciar:

1. Crea la base de datos en MySQL si aún no existe:

   ```sql
   CREATE DATABASE artesanias_olafo;
   ```

2. Configura usuario y contraseña de MySQL en `src/main/resources/application.properties` (`spring.datasource.username` y `spring.datasource.password`). No publiques credenciales reales en el repositorio.
3. La configuración JPA actual usa `spring.jpa.hibernate.ddl-auto=update`; Hibernate actualiza el esquema al iniciar.

## Ejecutar

Desde PowerShell, en la carpeta del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

Para ejecutar las pruebas:

```powershell
.\mvnw.cmd test
```

La API estará disponible en `http://localhost:8080`.

## Endpoints

### Categorías

| Método | Ruta                   | Resultado esperado                                                                  |
| ------ | ---------------------- | ----------------------------------------------------------------------------------- |
| GET    | `/api/categorias`      | Lista las categorías (200)                                                          |
| GET    | `/api/categorias/{id}` | Consulta una categoría (200 o 404)                                                  |
| POST   | `/api/categorias`      | Crea una categoría (201; 400 si no es válida; 409 si el nombre ya existe)           |
| PUT    | `/api/categorias/{id}` | Actualiza una categoría (200; 400, 404 o 409 según el caso)                         |
| DELETE | `/api/categorias/{id}` | Elimina una categoría sin productos (204; 404 si no existe; 409 si tiene productos) |

Formato de entrada para POST y PUT:

```json
{
  "nombre": "Cerámica",
  "descripcion": "Artesanías elaboradas en cerámica"
}
```

La respuesta incluye `idCategoria`, `nombre` y `descripcion`.

### Productos terminados

| Método | Ruta                          | Resultado esperado                                                                |
| ------ | ----------------------------- | --------------------------------------------------------------------------------- |
| GET    | `/api/productos`              | Lista productos; por defecto incluye activos e inactivos (200)                    |
| GET    | `/api/productos/{id}`         | Consulta un producto (200 o 404)                                                  |
| POST   | `/api/productos`              | Crea un producto activo (201; 400 si no es válido; 404 si la categoría no existe) |
| PUT    | `/api/productos/{id}`         | Actualiza un producto (200; 400 o 404 según el caso)                              |
| DELETE | `/api/productos/{id}`         | Inactiva el producto sin borrarlo (204; 404 si no existe)                         |
| PATCH  | `/api/productos/{id}/activar` | Reactiva el producto (200; 404 si no existe)                                      |

El listado admite los filtros opcionales `idCategoria` y `soloActivos`, por ejemplo: `/api/productos?idCategoria=1&soloActivos=true`.

Formato de entrada para POST y PUT:

```json
{
  "idCategoria": 1,
  "nombre": "Taza esmaltada",
  "descripcion": "Taza artesanal de cerámica",
  "precioVenta": 18000.0,
  "stockActual": 20
}
```

La respuesta incluye `idProducto`, `idCategoria`, `nombreCategoria`, `nombre`, `descripcion`, `precioVenta`, `stockActual`, `activo` y `fechaCreacion`.

## Demostración funcional con Postman

En Postman, crea un entorno con estas variables:

| Variable      | Valor inicial                                  |
| ------------- | ---------------------------------------------- |
| `baseUrl`     | `http://localhost:8080`                        |
| `categoriaId` | Déjala vacía; se obtiene al crear la categoría |
| `productoId`  | Déjala vacía; se obtiene al crear el producto  |

En las solicitudes POST y PUT selecciona **Body → raw → JSON**. Ejecuta las solicitudes en este orden:

1. **Listar categorías:** `GET {{baseUrl}}/api/categorias` (espera 200).
2. **Crear categoría:** `POST {{baseUrl}}/api/categorias` con:

   ```json
   {
     "nombre": "Cerámica",
     "descripcion": "Artesanías elaboradas en cerámica"
   }
   ```

   Espera 201 y guarda el valor `idCategoria` de la respuesta como `categoriaId`.

3. **Actualizar categoría:** `PUT {{baseUrl}}/api/categorias/{{categoriaId}}`, con el mismo formato JSON y un cambio en nombre o descripción (espera 200).
4. **Validar datos:** vuelve a enviar un POST de categoría con `"nombre": ""` (espera 400 y un cuerpo JSON con el error de validación).
5. **Crear producto:** `POST {{baseUrl}}/api/productos`:

   ```json
   {
     "idCategoria": {{categoriaId}},
     "nombre": "Taza esmaltada",
     "descripcion": "Taza artesanal de cerámica",
     "precioVenta": 18000.00,
     "stockActual": 20
   }
   ```

   Espera 201 y guarda `idProducto` como `productoId`.

6. **Listar productos filtrados:** `GET {{baseUrl}}/api/productos?idCategoria={{categoriaId}}&soloActivos=true` (espera 200).
7. **Actualizar producto:** `PUT {{baseUrl}}/api/productos/{{productoId}}` con el formato de producto y algún campo actualizado (espera 200).
8. **Inactivar y reactivar:** `DELETE {{baseUrl}}/api/productos/{{productoId}}` (espera 204); comprueba el estado consultando el producto o filtrando solo activos. Después ejecuta `PATCH {{baseUrl}}/api/productos/{{productoId}}/activar` (espera 200).
9. **Probar regla de categoría:** `DELETE {{baseUrl}}/api/categorias/{{categoriaId}}` (espera 409 porque el producto continúa asociado, aunque esté inactivo).
10. **Eliminar categoría sin productos:** crea otra categoría sin asociarle productos y elimina su ID con `DELETE {{baseUrl}}/api/categorias/{id}` (espera 204).

Para mostrar la persistencia durante la grabación, puedes consultar las tablas `categoria_producto` y `producto_terminado` en MySQL Workbench junto con las respuestas de Postman.

## Estructura del proyecto

```text
com.olafo.artesaniasOlafoBackend
├── controller   Endpoints REST
├── dto          Modelos de entrada y salida
├── exception    Excepciones y respuestas de error
├── model        Entidades JPA
├── repository   Acceso a datos con Spring Data JPA
└── service      Reglas y lógica de negocio
```

Flujo principal: `Controller → Service → Repository → MySQL`. Las validaciones se realizan con Jakarta Validation y los errores HTTP se centralizan en `GlobalExceptionHandler`.
