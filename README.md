# Olafo Backend — Sistema de Gestión de Inventario para Artesanías Olafo

Evidencia **GA7-220501096-AA3-EV01** · Backend con Spring Boot 4, Java 21, Maven y MySQL.

**Aprendiz:** Hans Alejandro Calderón Afanador · SENA ADSO, ficha 3336223

## Módulos desarrollados

| Módulo                | Tabla                | Endpoint base     |
| --------------------- | -------------------- | ----------------- |
| Categoría de producto | `categoria_producto` | `/api/categorias` |
| Producto terminado    | `producto_terminado` | `/api/productos`  |

Relación: una categoría tiene muchos productos (`producto_terminado.id_categoria_producto` → `categoria_producto.id_categoria_producto`).

## Requisitos

- JDK 21 o superior
- Maven 3.9+
- MySQL 8
- Visual Studio Code (opcional, con "Extension Pack for Java" y "REST Client")

## Endpoints

### Categorías — `/api/categorias`

| Método | URL                    | Descripción | Respuesta                         |
| ------ | ---------------------- | ----------- | --------------------------------- |
| GET    | `/api/categorias`      | Listar      | 200                               |
| GET    | `/api/categorias/{id}` | Consultar   | 200 / 404                         |
| POST   | `/api/categorias`      | Crear       | 201 / 400 / 409                   |
| PUT    | `/api/categorias/{id}` | Actualizar  | 200 / 400 / 404 / 409             |
| DELETE | `/api/categorias/{id}` | Eliminar    | 204 / 404 / 409 (tiene productos) |

### Productos — `/api/productos`

| Método | URL                                        | Descripción                       | Respuesta                   |
| ------ | ------------------------------------------ | --------------------------------- | --------------------------- |
| GET    | `/api/productos?idCategoria=&soloActivos=` | Listar con filtros                | 200                         |
| GET    | `/api/productos/{id}`                      | Consultar                         | 200 / 404                   |
| POST   | `/api/productos`                           | Crear                             | 201 / 400 / 404 (categoría) |
| PUT    | `/api/productos/{id}`                      | Actualizar                        | 200 / 400 / 404             |
| DELETE | `/api/productos/{id}`                      | Eliminación lógica (`activo = 0`) | 204 / 404                   |
| PATCH  | `/api/productos/{id}/activar`              | Reactivar                         | 200 / 404                   |

Ejemplo de cuerpo para crear un producto:

```json
{
  "idCategoria": 1,
  "nombre": "Taza esmaltada",
  "descripcion": "Taza de arcilla",
  "precioVenta": 18000.0,
  "stockActual": 20
}
```

## Arquitectura y paquetes

```
com.olafo
├── OlafoApplication        punto de entrada
├── controller              endpoints REST (reciben y responden HTTP)
├── service                 lógica de negocio y reglas
├── repository              acceso a datos (Spring Data JPA)
├── entity                  entidades JPA mapeadas al modelo relacional
├── dto                     objetos de entrada/salida con validaciones
└── exception               excepciones y manejador global de errores
```

Flujo: `Controller → Service → Repository → MySQL`. Cada capa solo conoce a la siguiente.

## Validaciones y reglas

- Nombre obligatorio y con longitud máxima (`@NotBlank`, `@Size`).
- Precio ≥ 0 con máximo 2 decimales; stock ≥ 0 (`@DecimalMin`, `@Digits`, `@PositiveOrZero`).
- El nombre de la categoría es único; no se elimina una categoría con productos.
- Los errores se devuelven en JSON (`ProblemDetail`) con el código HTTP correspondiente.

## Correspondencia con los artefactos aprobados

- Entidades `CategoriaProducto` y `ProductoTerminado`: mismas columnas, tipos y llave foránea del modelo físico; atributos y nombres del diagrama de clases.
- `precioVenta` usa `BigDecimal` (en el diagrama figura `double`) porque la columna es `DECIMAL(10,2)` y se trabaja con dinero.
- Métodos del diagrama cubiertos: `crear`, `actualizar`, `consultar`, `eliminar`, `listarProductos` (categoría); `registrar`, `editar`, `inactivar`, `consultarDisponibilidad` (producto).
