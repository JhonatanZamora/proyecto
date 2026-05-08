# Sistema de Triage y Gestión de Solicitudes Académicas

**Curso:** Programación Avanzada  
**Universidad:** Universidad del Quindío — Programa de Ingeniería de Sistemas y Computación  
**Entrega:** Hito 2 — API REST, Persistencia JPA y Seguridad JWT

---

## Integrantes

| Nombre                           | Documento  |
|----------------------------------|------------|
| Juan Camilo Valbuena Benavides   | 1006490498 |
| Jhonatan Alberto Zamora Castillo | 1089487563 |

---

## Descripción del Proyecto

El Programa de Ingeniería de Sistemas y Computación de la Universidad del Quindío cuenta con más de 1.400 estudiantes, docentes y administrativos que realizan de manera permanente solicitudes académicas a través de múltiples canales (presencial, correo, SAC, telefónico).

Este sistema resuelve la gestión ineficiente de dichas solicitudes mediante una plataforma unificada que permite:

- **Registrar** solicitudes de manera estructurada con trazabilidad completa.
- **Clasificar y priorizar** solicitudes según reglas de negocio definidas.
- **Asignar responsables** de forma controlada y auditable.
- **Gestionar el ciclo de vida** completo: desde el registro hasta el cierre formal.
- **Mantener un historial** auditable de cada acción realizada.

---

## Arquitectura

El proyecto sigue **Domain-Driven Design (DDD)** con arquitectura hexagonal:

```
co.edu.uniquindio.proyecto
├── domain/               → Entidades, Value Objects, Servicios, Excepciones
├── application/          → Casos de uso (Use Cases), puertos de salida
└── infrastructure/
    ├── persistence/jpa/  → Adaptadores JPA (H2)
    ├── rest/             → Controladores REST, DTOs, Mappers
    ├── security/         → JWT (HS256), filtro de autenticación
    └── config/           → DataSeeder, configuración de propiedades
```

**Ciclo de vida de una solicitud:**

```
REGISTRADA → CLASIFICADA → EN_ATENCION → ATENDIDA → CERRADA
```

---

## Stack Tecnológico

| Tecnología            | Versión  |
|-----------------------|----------|
| Java                  | 25       |
| Spring Boot           | 4.x      |
| Spring Security       | 6.x      |
| Spring Data JPA       | 3.x      |
| H2 Database           | en memoria |
| Nimbus JOSE + JWT     | incluido en oauth2-resource-server |
| Gradle                | 9.x      |
| Lombok                | latest   |
| springdoc-openapi     | 2.x      |

---

## Instrucciones para ejecutar

### Requisitos previos

- Java 25 instalado
- IntelliJ IDEA (recomendado) o cualquier IDE con soporte Gradle

### Clonar y ejecutar

```bash
git clone https://github.com/JhonatanZamora/proyecto.git
cd proyecto/proyecto
./gradlew bootRun
```

La aplicación inicia en `http://localhost:8080`.

### Compilar sin ejecutar

```bash
./gradlew build
```

### Ejecutar pruebas

```bash
./gradlew test
```

Reporte HTML: `build/reports/tests/test/index.html`

---

## Credenciales seed

Al iniciar la aplicación el `DataSeeder` inserta automáticamente estos usuarios en la base de datos en memoria:

| Email                     | Contraseña | Rol         | Descripción                        |
|---------------------------|------------|-------------|------------------------------------|
| admin@solicitudes.com     | admin123   | COORDINADOR | Puede clasificar y asignar         |
| agente@solicitudes.com    | agente123  | DOCENTE     | Puede ser asignado como responsable |

> Los usuarios se recrean en cada inicio porque la base de datos H2 es `create-drop`.

---

## Endpoints de la API

### Autenticación

| Método | Ruta          | Descripción                              | Auth requerida |
|--------|---------------|------------------------------------------|----------------|
| POST   | /auth/login   | Obtener token JWT con email y contraseña | No             |

### Solicitudes

| Método | Ruta                                  | Descripción                                                    | Auth requerida |
|--------|---------------------------------------|----------------------------------------------------------------|----------------|
| POST   | /api/solicitudes                      | Registrar nueva solicitud (crea en estado REGISTRADA)          | Sí             |
| GET    | /api/solicitudes                      | Listar solicitudes paginadas (filtros: estado, prioridad)      | Sí             |
| PATCH  | /api/solicitudes/{codigo}/clasificar  | Clasificar: asignar prioridad (REGISTRADA → CLASIFICADA)       | Sí             |
| PATCH  | /api/solicitudes/{codigo}/asignar     | Asignar responsable (CLASIFICADA → EN_ATENCION)                | Sí             |
| PATCH  | /api/solicitudes/{codigo}/atender     | Marcar como atendida (EN_ATENCION → ATENDIDA)                  | Sí             |
| PATCH  | /api/solicitudes/{codigo}/cerrar      | Cerrar definitivamente (ATENDIDA → CERRADA, estado terminal)   | Sí             |
| GET    | /api/solicitudes/{codigo}/historial   | Consultar historial de auditoría de una solicitud              | Sí             |

---

## Flujo de uso típico

```bash
# 1. Obtener token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@solicitudes.com","password":"admin123"}'

# 2. Usar el token en las siguientes peticiones
TOKEN="<token_del_paso_anterior>"

# 3. Registrar una solicitud
curl -X POST http://localhost:8080/api/solicitudes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "tipoDocumento": "CC",
    "numeroDocumento": "1089000001",
    "tipoSolicitud": "HOMOLOGACION",
    "descripcion": "Solicitud de homologación de Cálculo I cursada en otra institución",
    "canalOrigen": "CSU"
  }'

# 4. Clasificar la solicitud (reemplaza <codigo> con el UUID devuelto)
curl -X PATCH http://localhost:8080/api/solicitudes/<codigo>/clasificar \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"prioridad":"ALTA","justificacion":"Riesgo de pérdida de semestre"}'
```

---

## Herramientas de desarrollo

### Swagger UI

Interfaz interactiva para explorar y probar todos los endpoints:

```
http://localhost:8080/swagger-ui.html
```

Para autenticarse en Swagger:
1. Ejecutar `POST /auth/login` con las credenciales seed.
2. Copiar el `token` de la respuesta.
3. Clic en **Authorize** (candado) → pegar el token → **Authorize**.

### H2 Console

Consola web para inspeccionar la base de datos en memoria:

```
http://localhost:8080/h2-console
```

| Campo    | Valor                      |
|----------|----------------------------|
| JDBC URL | `jdbc:h2:mem:solicitudesdb` |
| Usuario  | `sa`                       |
| Password | *(vacío)*                  |

---

## Documentación adicional

La carpeta `/docs` contiene los artefactos de diseño:

| Documento              | Descripción                                      |
|------------------------|--------------------------------------------------|
| `openapi.yaml`         | Especificación OpenAPI 3.0 completa de la API    |
| `DiagramaUML.png`      | Modelo de dominio UML                            |
| `DiagramaEstado.png`   | Ciclo de vida de la Solicitud                    |
| `glosario.md`          | Lenguaje ubicuo del dominio                      |
| `reglas-de-negocio.md` | Reglas de negocio trazadas a RFs                 |

---

## Requisitos Funcionales Cubiertos

| RF    | Descripción                          | Hito 1 | Hito 2 |
|-------|--------------------------------------|--------|--------|
| RF-01 | Registro de solicitudes              | ✅     | ✅ API  |
| RF-02 | Clasificación de solicitudes         | ✅     | ✅ API  |
| RF-03 | Priorización con justificación       | ✅     | ✅ API  |
| RF-04 | Ciclo de vida (estados)              | ✅     | ✅ API  |
| RF-05 | Solo DOCENTE/COORDINADOR responsable | ✅     | ✅ API  |
| RF-06 | Historial auditable                  | ✅     | ✅ API  |
| RF-08 | Cierre con observación obligatoria   | ✅     | ✅ API  |
| RF-13 | Roles y autorización                 | ✅     | ✅ JWT  |
