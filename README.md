# Librería API

Es una API basada en una arquitectura orientada a servicios para administrar una librería: registro de **libros**, **usuarios** y **préstamos**. Incluye además una interfaz gráfica que usa los mismos servicios que la API.

## Indice

1. [Descripción de la API](#1-descripción-de-la-api)
2. [Tecnologías utilizadas](#2-tecnologías-utilizadas)
3. [Instalación y ejecución](#3-instalación-y-ejecución)
4. [Variables de entorno](#4-variables-de-entorno)
5. [Listado de endpoints](#5-listado-de-endpoints)
6. [Ejemplos de peticiones y respuestas](#6-ejemplos-de-peticiones-y-respuestas)

---

## 1. Descripción de la API

La API permite:

- **Libros:** registrar, consultar, actualizar y eliminar libros, con control de ejemplares disponibles.
- **Usuarios:** administrar los socios de la librería (rol `ADMIN` o `USUARIO`, estado `ACTIVO` o `INACTIVO`).
- **Préstamos:** registrar el préstamo de un libro a un socio, registrar su devolución y calcular automáticamente el retraso y la multa.

La aplicación sigue una separación por capas, donde la interfaz web y la API comparten la misma capa de servicios:

```
Cliente REST / Navegador  →  Controller  →  Service  →  Repository  →  MySQL
```
---

## 2. Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| **Java 21** | Lenguaje |
| **Spring Boot 4.1.1** | Framework principal |
| **Spring Web MVC** | API REST y controladores de las vistas |
| **Spring Data JPA / Hibernate** | Persistencia y acceso a datos |
| **Bean Validation (Jakarta)** | Validación de datos de entrada |
| **MySQL 8** + Connector/J | Base de datos relacional |
| **Thymeleaf** | Plantillas HTML de la interfaz |
| **Bootstrap 5.3.8** (CDN) | Estilos de la interfaz |
| **Maven** | Gestión de dependencias y construcción |

---

## 3. Instalación y ejecución

### Requisitos previos

- JDK 21
- Maven 3.9 o superior
- MySQL 8 en ejecución (puerto 3306 por defecto)

### Pasos

**1. Obtener el proyecto**

```bash
git clone https://github.com/Emma67g/LibreriaAPI.git
cd libreria
```

**2. Crear la base de datos** (las tablas las crea la aplicación automáticamente al arrancar):

```sql
CREATE DATABASE libreria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**3. Configurar las credenciales de MySQL.** Por defecto la aplicación usa el usuario `root` con la contraseña definida. Si tu MySQL tiene otros datos, defínelos en application.properties.

**4. Ejecutar la aplicación**

En LibreriaApplication.java, oprimir el botón de correr (símbolo de tríangulo caído hacia la derecha, dice "Run Java").

**5. Abrir la aplicación**

- Interfaz web: <http://localhost:1500>
- API REST: <http://localhost:1500/api/libros>

---

## 4. Variables de entorno

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL JDBC de la base de datos | `jdbc:mysql://localhost:3306/libreria?useSSL=false&serverTimezone=UTC` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de MySQL | definir segun el usuario |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de MySQL | definir segun el usuario |
| `SERVER_PORT` | Puerto en el que corre la API | `1500` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Estrategia de creación de tablas | `update` |
| `SPRING_JPA_SHOW_SQL` | Mostrar las consultas SQL en consola | `true` |

**Linux / macOS (bash):**

```bash
export SPRING_DATASOURCE_USERNAME=mi_usuario
export SPRING_DATASOURCE_PASSWORD=mi_contraseña
export SERVER_PORT=8080
mvn spring-boot:run
```

**Windows (PowerShell):**

```powershell
$env:SPRING_DATASOURCE_USERNAME = "mi_usuario"
$env:SPRING_DATASOURCE_PASSWORD = "mi_contraseña"
$env:SERVER_PORT = "8080"
mvn spring-boot:run
```

**Windows (CMD):**

```cmd
set SPRING_DATASOURCE_USERNAME=mi_usuario
set SPRING_DATASOURCE_PASSWORD=mi_contraseña
set SERVER_PORT=8080
mvn spring-boot:run
```
---

## 5. Listado de endpoints

URL base: `http://localhost:1500`

> **Nota sobre los identificadores:** en `/api/libros/{isbn}` y en el campo `isbn` de los préstamos se usa el **identificador interno** del libro (autoincremental). El ISBN real del libro se guarda en el campo `codigoIsbn`.

### Libros — `/api/libros`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| `GET` | `/api/libros` | Lista todos los libros | `200` |
| `GET` | `/api/libros/{isbn}` | Obtiene un libro | `200`, `404` |
| `POST` | `/api/libros` | Registra un libro | `201`, `400`, `409` |
| `PUT` | `/api/libros/{isbn}` | Actualiza un libro | `200`, `400`, `404`, `409` |
| `DELETE` | `/api/libros/{isbn}` | Elimina un libro (si no tiene préstamos) | `204`, `404`, `409` |

### Usuarios — `/api/usuarios`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| `GET` | `/api/usuarios` | Lista todos los usuarios | `200` |
| `GET` | `/api/usuarios/{id}` | Obtiene un usuario | `200`, `404` |
| `POST` | `/api/usuarios` | Registra un usuario | `201`, `400`, `409` |
| `PUT` | `/api/usuarios/{id}` | Actualiza un usuario | `200`, `400`, `404`, `409` |
| `DELETE` | `/api/usuarios/{id}` | Elimina un usuario (si no tiene préstamos) | `204`, `404`, `409` |

La contraseña se acepta al crear o editar, pero **nunca se devuelve** en las respuestas.

### Préstamos — `/api/prestamos`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| `GET` | `/api/prestamos` | Lista todos los préstamos | `200` |
| `GET` | `/api/prestamos/{id}` | Obtiene un préstamo | `200`, `404` |
| `POST` | `/api/prestamos` | Registra un préstamo | `201`, `400`, `409` |
| `POST` | `/api/prestamos/{id}/devolver` | Registra la devolución | `200`, `404` |
| `DELETE` | `/api/prestamos/{id}` | Elimina un préstamo | `204`, `404` |

---

## 6. Ejemplos de peticiones y respuestas

En Windows CMD se recomienda usar Postman o Git Bash para enviar el JSON.

### Libros

**Registrar un libro**

```bash
curl -X POST http://localhost:1500/api/libros \
  -H "Content-Type: application/json" \
  -d '{
    "codigoIsbn": "978-84-204-0014-3",
    "titulo": "Ficciones",
    "autor": "Jorge Luis Borges",
    "editorial": "Emecé",
    "anioPublicacion": "1944",
    "numejemplares": 2
  }'
```

Respuesta `201 Created` (encabezado `Location: /api/libros/13`):

```json
{
  "isbn": 13,
  "codigoIsbn": "9788420400143",
  "titulo": "Ficciones",
  "autor": "Jorge Luis Borges",
  "editorial": "Emecé",
  "anioPublicacion": "1944",
  "numejemplares": 2
}
```

**Consultar un libro**

```bash
curl http://localhost:1500/api/libros/1
```

Respuesta `200 OK`:

```json
{
  "isbn": 1,
  "codigoIsbn": "9788437600017",
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "editorial": "Editorial Sudamericana",
  "anioPublicacion": "1967",
  "numejemplares": 4
}
```

**Libro inexistente** — `GET /api/libros/999` responde `404 Not Found` sin cuerpo.

**Datos inválidos**

```bash
curl -X POST http://localhost:1500/api/libros \
  -H "Content-Type: application/json" \
  -d '{"codigoIsbn": "123", "titulo": "", "autor": "Autor", "anioPublicacion": "99", "numejemplares": -1}'
```

Respuesta `400 Bad Request` (el orden de los campos puede variar):

```json
{
  "error": "Datos inválidos",
  "campos": {
    "codigoIsbn": "El ISBN debe tener 10 o 13 dígitos (se aceptan guiones)",
    "titulo": "El título es obligatorio",
    "anioPublicacion": "El año de publicación debe tener 4 dígitos",
    "numejemplares": "El número de ejemplares no puede ser negativo"
  }
}
```

**Eliminar un libro con préstamos**

```bash
curl -X DELETE http://localhost:1500/api/libros/1
```

Respuesta `409 Conflict`:

```json
{
  "error": "No se puede eliminar el libro porque tiene préstamos asociados. Elimina primero esos préstamos."
}
```

### Usuarios

**Registrar un usuario**

```bash
curl -X POST http://localhost:1500/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Fernanda Isabel",
    "apellidos": "Gómez Navarro",
    "correoelectronico": "fernanda.gomez@correo.com",
    "contrasena": "Clave123",
    "rol": "USUARIO",
    "estado": "ACTIVO",
    "fechanacimiento": "1999-08-14",
    "telefono": "555-010-0109",
    "direccion": "Calle Independencia 150, Col. Centro"
  }'
```

Respuesta `201 Created` (la contraseña no se devuelve y la fecha de creación la asigna el servidor):

```json
{
  "id": 9,
  "nombre": "Fernanda Isabel",
  "apellidos": "Gómez Navarro",
  "correoelectronico": "fernanda.gomez@correo.com",
  "rol": "USUARIO",
  "estado": "ACTIVO",
  "fechacreacion": "2026-10-11",
  "fechanacimiento": "1999-08-14",
  "telefono": "555-010-0109",
  "direccion": "Calle Independencia 150, Col. Centro"
}
```

**Actualizar sin cambiar la contraseña** — si el cuerpo del `PUT` no incluye `contrasena`, se conserva la anterior (y también la fecha de creación).

**Correo duplicado** — registrar un correo ya existente responde `409 Conflict`:

```json
{
  "error": "La operación viola una restricción de datos (por ejemplo, un ISBN o correo duplicado, o datos asociados)"
}
```

### Préstamos

**Registrar un préstamo**

El campo `fechaLimite` es opcional (formato `yyyy-MM-dd`); si se omite, se asignan 7 días.

```bash
curl -X POST http://localhost:1500/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": 8,
    "socioId": 3,
    "fechaLimite": "2026-10-18"
  }'
```

Respuesta `201 Created` (encabezado `Location: /api/prestamos/16`):

```json
{
  "id": 16,
  "libro": {
    "isbn": 8,
    "codigoIsbn": "9788420400082",
    "titulo": "Rayuela",
    "autor": "Julio Cortázar",
    "editorial": "Editorial Sudamericana",
    "anioPublicacion": "1963",
    "numejemplares": 2
  },
  "socio": {
    "id": 3,
    "nombre": "Ana Sofía",
    "apellidos": "Martínez Cruz",
    "correoelectronico": "ana.martinez@correo.com",
    "rol": "USUARIO",
    "estado": "ACTIVO",
    "fechacreacion": "2026-02-18",
    "fechanacimiento": "1998-11-05",
    "telefono": "555-010-0103",
    "direccion": "Calle Morelos 310, Col. Jardines"
  },
  "fechaPrestamo": "2026-10-11T10:15:30.123456",
  "fechaLimite": "2026-10-18T23:59:59",
  "fechaDevolucion": null,
  "activo": true,
  "vencido": false,
  "diasRetraso": 0,
  "multa": 0.0
}
```

**Consultar un préstamo vencido**

```bash
curl http://localhost:1500/api/prestamos/6
```

Respuesta `200 OK` (se omiten `libro` y `socio` por brevedad, los valores de retraso y multa dependen de la fecha actual):

```json
{
  "id": 6,
  "libro": { "...": "..." },
  "socio": { "...": "..." },
  "fechaPrestamo": "2026-09-26T10:15:30.123456",
  "fechaLimite": "2026-10-03T23:59:59",
  "fechaDevolucion": null,
  "activo": true,
  "vencido": true,
  "diasRetraso": 8,
  "multa": 40.0
}
```

**Registrar la devolución**

```bash
curl -X POST http://localhost:1500/api/prestamos/16/devolver
```

Respuesta `200 OK` (se omiten `libro` y `socio` por brevedad):

```json
{
  "id": 16,
  "libro": { "...": "..." },
  "socio": { "...": "..." },
  "fechaPrestamo": "2026-10-11T10:15:30.123456",
  "fechaLimite": "2026-10-18T23:59:59",
  "fechaDevolucion": "2026-10-12T09:00:12.456789",
  "activo": false,
  "vencido": false,
  "diasRetraso": 0,
  "multa": 0.0
}
```

**Socio con 3 préstamos activos**

```bash
curl -X POST http://localhost:1500/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{"isbn": 6, "socioId": 2}'
```

Respuesta `409 Conflict`:

```json
{
  "error": "Este socio ya tiene 3 préstamos activos (máximo 3)"
}
```

**Libro sin ejemplares disponibles**

```bash
curl -X POST http://localhost:1500/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{"isbn": 11, "socioId": 3}'
```

Respuesta `409 Conflict`:

```json
{
  "error": "No hay ejemplares disponibles de \"Los detectives salvajes\""
}
```

**Fecha límite anterior a hoy**

```bash
curl -X POST http://localhost:1500/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{"isbn": 8, "socioId": 3, "fechaLimite": "2026-10-01"}'
```

Respuesta `400 Bad Request`:

```json
{
  "error": "La fecha límite no puede ser anterior a hoy"
}
```

**Campos obligatorios faltantes**

```bash
curl -X POST http://localhost:1500/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{}'
```

Respuesta `400 Bad Request`:

```json
{
  "error": "Datos inválidos",
  "campos": {
    "isbn": "El id del libro es obligatorio",
    "socioId": "El id del socio es obligatorio"
  }
}
```