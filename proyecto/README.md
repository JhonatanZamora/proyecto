# Sistema de Triage y Gestión de Solicitudes Académicas

**Curso:** Programación Avanzada  
**Universidad:** Universidad del Quindío — Programa de Ingeniería de Sistemas y Computación  
**Entrega:** Hito 1 — Modelado del Dominio y Materialización en Código

---

##  Integrantes

| Nombre                           | Documento  |
|----------------------------------|------------|
| Juan Camilo Valbuena Benavides   | 1006490498 |
| Jhonatan Alberto Zamora Castillo | 1089487563 |

---

## Descripción del Proyecto

El Programa de Ingeniería de Sistemas y Computación de la Universidad del Quindío cuenta con más de 1.400 estudiantes, docentes y administrativos que realizan de manera permanente solicitudes académicas y administrativas a través de múltiples canales (presencial, correo, SAC, telefónico).

Este sistema resuelve la gestión ineficiente de dichas solicitudes mediante una plataforma unificada que permite:

- **Registrar** solicitudes de manera estructurada con trazabilidad completa.
- **Clasificar y priorizar** solicitudes según reglas de negocio definidas.
- **Asignar responsables** de forma controlada y auditable.
- **Gestionar el ciclo de vida** completo: desde el registro hasta el cierre formal.
- **Mantener un historial** auditable de cada acción realizada.

La arquitectura sigue los principios de **Domain-Driven Design (DDD)**, garantizando que la lógica de negocio esté encapsulada en el dominio, independiente de frameworks e infraestructura.

---

## Arquitectura

El proyecto está organizado por **capas de dominio**, no por capas técnicas:

```
co.edu.uniquindio.proyecto
├── domain/
│   ├── entity/          → Solicitud, Usuario, EventoHistorial
│   ├── valueobject/     → Email, CodigoSolicitud, DocumentoIdentidad,
│   │                       EstadoSolicitud, Prioridad, TipoSolicitud,
│   │                       CanalOrigen, Rol, TipoDocumento
│   ├── service/         → AsignacionSolicitudService, NotificadorSolicitudes
│   └── exception/       → ReglaNegocioException
├── application/         → (Hito 2)
└── infrastructure/      → (Hito 2)
```

---

## Stack Tecnológico

| Tecnología | Versión |
|---|---|
| Java | 25 |
| Spring Boot | 4.x |
| Gradle | 8.x |
| JUnit | 5.x |
| Lombok | latest |

---

## Instrucciones para compilar y ejecutar

### Requisitos previos

- Java 25 instalado
- Gradle 8.x (o usar el wrapper incluido)
- IntelliJ IDEA (recomendado)

### Clonar el repositorio

```bash
git clone https://github.com/JhonatanZamora/proyecto.git
cd proyecto
```

### Compilar el proyecto

```bash
./gradlew build
```

### Ejecutar las pruebas unitarias

```bash
./gradlew test
```

### Ver reporte de pruebas

Después de ejecutar los tests, el reporte HTML se genera en:

```
build/reports/tests/test/index.html
```

---

##  Cobertura de Pruebas

| Clase de prueba | Tipo | Tests  |
|---|---|--------|
| `EmailTest` | Value Object | 3      |
| `CodigoSolicitudTest` | Value Object | 8      |
| `DocumentoIdentidadTest` | Value Object | 10     |
| `UsuarioTest` | Entidad | 10     |
| `SolicitudTest` | Agregado Raíz | 16     |
| `AsignacionSolicitudServiceTest` | Servicio de Dominio | 4      |
| `NotificadorSolicitudesTest` | Servicio de Dominio | 4      |
| **Total** | | **63** |

---

## Documentación

La carpeta `/docs` contiene los artefactos de análisis y diseño:

| Documento | Descripción |
|---|---|
| `diagrama-clases.png` | Modelo de dominio UML |
| `diagrama-estados.png` | Ciclo de vida de la Solicitud |
| `glosario.md` | Lenguaje ubicuo del dominio |
| `reglas-de-negocio.md` | Reglas de negocio trazadas a RFs |

---

##  Requisitos Funcionales Cubiertos

| RF | Descripción | Estado |
|---|---|---|
| RF-01 | Registro de solicitudes | ✅ |
| RF-02 | Clasificación de solicitudes | ✅ |
| RF-03 | Priorización con justificación | ✅ |
| RF-04 | Ciclo de vida (estados) | ✅ |
| RF-06 | Historial auditable | ✅ |
| RF-08 | Cierre con observación obligatoria | ✅ |
| RF-11 | Independencia de IA | ✅ |
| RF-13 | Roles y autorización | ✅ |
