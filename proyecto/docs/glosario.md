# Glosario de Lenguaje Ubicuo
## Sistema de Triage y Gestión de Solicitudes Académicas
**Universidad del Quindío — Programación Avanzada**

---

## ¿Qué es el Lenguaje Ubicuo?

Es el vocabulario compartido entre el equipo de desarrollo y los expertos del dominio.
Todos los términos aquí definidos se usan de forma idéntica en las conversaciones,
los documentos y el código fuente.

---

## Términos del Dominio

| Nombre | Definición en el sistema | Clasificación |
|---|---|---|
| **Solicitud** | Trámite académico formal registrado por un estudiante a través de cualquier canal. Es la unidad central del sistema y posee un ciclo de vida propio. | Entidad / Agregado Raíz |
| **Usuario** | Persona que interactúa con el sistema. Puede ser Estudiante, Docente o Coordinador. Cada uno tiene permisos diferenciados según su rol. | Entidad |
| **EventoHistorial** | Registro inmutable de una acción ocurrida sobre una solicitud. Garantiza la trazabilidad completa del ciclo de vida. | Entidad (interna al agregado) |
| **CodigoSolicitud** | Identificador único e irrepetible de una solicitud académica. Generado automáticamente al momento del registro. | Value Object |
| **Email** | Dirección de correo electrónico válida y normalizada de un usuario. Debe cumplir formato estándar. | Value Object |
| **DocumentoIdentidad** | Combinación de tipo de documento y número que identifica a una persona de forma única. | Value Object |
| **TipoDocumento** | Categoría del documento de identidad: CC, TI, CE o PA. | Value Object (enum) |
| **EstadoSolicitud** | Posición actual de la solicitud dentro de su ciclo de vida: REGISTRADA, CLASIFICADA, EN_ATENCION, ATENDIDA o CERRADA. | Value Object (enum) |
| **Prioridad** | Nivel de urgencia asignado a una solicitud: ALTA, MEDIA o BAJA. Determina el orden de atención. | Value Object (enum) |
| **TipoSolicitud** | Categoría del trámite académico: REGISTRO_ASIGNATURA, HOMOLOGACION, CANCELACION_ASIGNATURA, SOLICITUD_CUPO o CONSULTA_ACADEMICA. | Value Object (enum) |
| **CanalOrigen** | Medio por el cual llegó la solicitud al sistema: CSU, CORREO, SAC o TELEFONICO. | Value Object (enum) |
| **Rol** | Función del usuario dentro del sistema: ESTUDIANTE, DOCENTE o COORDINADOR. Define qué operaciones puede realizar. | Value Object (enum) |
| **ReglaNegocioException** | Excepción que se lanza cuando una regla de negocio es violada. Distingue errores del dominio de errores técnicos. | Excepción del Dominio |
| **AsignacionSolicitudService** | Servicio de dominio que coordina la clasificación y asignación de responsable en una operación atómica. | Servicio de Dominio |
| **NotificadorSolicitudes** | Servicio de dominio que determina quién debe ser notificado ante cada tipo de evento sobre una solicitud. | Servicio de Dominio |
| **Ciclo de vida** | Secuencia de estados por los que pasa una solicitud desde su registro hasta su cierre: REGISTRADA → CLASIFICADA → EN_ATENCION → ATENDIDA → CERRADA. | Concepto del Dominio |
| **Invariante** | Regla de negocio que debe ser siempre verdadera. El agregado garantiza que sus invariantes nunca se violen. | Concepto del Dominio |
| **Responsable** | Usuario con rol DOCENTE o COORDINADOR asignado para atender una solicitud específica. | Concepto del Dominio |
| **Clasificación** | Acción de asignar prioridad y justificación a una solicitud REGISTRADA. La realiza el Coordinador. | Concepto del Dominio |
| **Cierre** | Acción formal que marca el fin del proceso de una solicitud. Solo es posible desde el estado ATENDIDA y requiere una observación obligatoria. | Concepto del Dominio |
