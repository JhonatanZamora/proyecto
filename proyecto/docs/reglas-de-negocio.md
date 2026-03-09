# Documento de Reglas de Negocio
## Sistema de Triage y Gestión de Solicitudes Académicas
**Universidad del Quindío — Programación Avanzada**

---

## RN-01 — Solo estudiantes pueden registrar solicitudes

| Campo | Detalle |
|---|---|
| **Acción que regula** | Registro de una nueva solicitud académica |
| **Condición** | El solicitante debe tener rol `ESTUDIANTE` |
| **RF asociado** | RF-01, RF-13 |
| **Vive en** | Entidad `Solicitud` — constructor |
| **Excepción** | `ReglaNegocioException` |

---

## RN-02 — La descripción es obligatoria al registrar

| Campo | Detalle |
|---|---|
| **Acción que regula** | Registro de una nueva solicitud académica |
| **Condición** | La descripción no puede ser nula ni vacía |
| **RF asociado** | RF-01 |
| **Vive en** | Entidad `Solicitud` — constructor |
| **Excepción** | `IllegalArgumentException` |

---

## RN-03 — El canal de origen es obligatorio al registrar

| Campo | Detalle |
|---|---|
| **Acción que regula** | Registro de una nueva solicitud académica |
| **Condición** | El canal de origen no puede ser nulo |
| **RF asociado** | RF-01 |
| **Vive en** | Entidad `Solicitud` — constructor |
| **Excepción** | `IllegalArgumentException` |

---

## RN-04 — La prioridad requiere justificación

| Campo | Detalle |
|---|---|
| **Acción que regula** | Clasificación de una solicitud |
| **Condición** | La justificación de la prioridad no puede ser nula ni vacía |
| **RF asociado** | RF-03 |
| **Vive en** | Entidad `Solicitud` — método `asignarPrioridad()` |
| **Excepción** | `IllegalArgumentException` |

---

## RN-05 — Solo se puede asignar responsable a una solicitud CLASIFICADA

| Campo | Detalle |
|---|---|
| **Acción que regula** | Asignación de responsable |
| **Condición** | El estado de la solicitud debe ser `CLASIFICADA` |
| **RF asociado** | RF-05 |
| **Vive en** | Entidad `Solicitud` — método `asignarResponsable()` |
| **Excepción** | `ReglaNegocioException` |

---

## RN-06 — Solo DOCENTE o COORDINADOR pueden ser responsables

| Campo | Detalle |
|---|---|
| **Acción que regula** | Asignación de responsable |
| **Condición** | El usuario asignado debe tener rol `DOCENTE` o `COORDINADOR` |
| **RF asociado** | RF-05, RF-13 |
| **Vive en** | Entidad `Solicitud` — método `asignarResponsable()` |
| **Excepción** | `ReglaNegocioException` |

---

## RN-07 — Solo se puede atender una solicitud EN_ATENCION

| Campo | Detalle |
|---|---|
| **Acción que regula** | Marcado de solicitud como atendida |
| **Condición** | El estado debe ser `EN_ATENCION` |
| **RF asociado** | RF-04 |
| **Vive en** | Entidad `Solicitud` — método `marcarComoAtendida()` |
| **Excepción** | `ReglaNegocioException` |

---

## RN-08 — El cierre solo es posible desde ATENDIDA

| Campo | Detalle |
|---|---|
| **Acción que regula** | Cierre formal de una solicitud |
| **Condición** | El estado debe ser `ATENDIDA` |
| **RF asociado** | RF-08 |
| **Vive en** | Entidad `Solicitud` — método `cerrar()` |
| **Excepción** | `ReglaNegocioException` |

---

## RN-09 — El cierre requiere observación obligatoria

| Campo | Detalle |
|---|---|
| **Acción que regula** | Cierre formal de una solicitud |
| **Condición** | La observación de cierre no puede ser nula ni vacía |
| **RF asociado** | RF-08 |
| **Vive en** | Entidad `Solicitud` — método `cerrar()` |
| **Excepción** | `ReglaNegocioException` |

---

## RN-10 — Una solicitud cerrada no puede modificarse

| Campo | Detalle |
|---|---|
| **Acción que regula** | Cualquier acción sobre una solicitud |
| **Condición** | Si el estado es terminal (`CERRADA`), ninguna acción es permitida |
| **RF asociado** | RF-08 |
| **Vive en** | Entidad `Solicitud` — método privado `validarQueNoEsTerminal()` |
| **Excepción** | `ReglaNegocioException` |

---

## RN-11 — Toda acción queda registrada en el historial

| Campo | Detalle |
|---|---|
| **Acción que regula** | Cualquier transición de estado |
| **Condición** | Cada cambio de estado genera automáticamente un `EventoHistorial` |
| **RF asociado** | RF-06 |
| **Vive en** | Entidad `Solicitud` — método privado `registrarEvento()` |
| **Excepción** | No aplica — es una garantía estructural |

---

## RN-12 — El historial solo puede ser modificado por la raíz del agregado

| Campo | Detalle |
|---|---|
| **Acción que regula** | Escritura en el historial |
| **Condición** | El constructor de `EventoHistorial` tiene visibilidad de paquete; `getHistorial()` devuelve lista inmutable |
| **RF asociado** | RF-06 |
| **Vive en** | Entidad `EventoHistorial` — constructor package-private; Entidad `Solicitud` — método `getHistorial()` |
| **Excepción** | `UnsupportedOperationException` si se intenta modificar desde fuera |

---

## Resumen de trazabilidad

| RF | Reglas asociadas |
|---|---|
| RF-01 | RN-01, RN-02, RN-03 |
| RF-03 | RN-04 |
| RF-04 | RN-05, RN-07 |
| RF-05 | RN-05, RN-06 |
| RF-06 | RN-11, RN-12 |
| RF-08 | RN-08, RN-09, RN-10 |
| RF-13 | RN-01, RN-06 |
