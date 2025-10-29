# CLAUDE.md
**Autor:** Arquitectura Técnica  
**Versión:** 1.0.0  
**Última actualización:** 2025-10-28  
**Propósito:** Documentar las convenciones, decisiones y buenas prácticas que rigen la arquitectura del sistema, bajo el enfoque de Arquitectura Hexagonal (Puertos y Adaptadores).

---

## 🧭 Visión General

Este proyecto sigue los principios de la **Arquitectura Hexagonal** para lograr independencia entre el dominio del negocio y los detalles de infraestructura.  
El objetivo es permitir que la lógica de negocio evolucione sin depender de frameworks, bases de datos ni mecanismos externos.

El código debe organizarse de modo que las dependencias fluyan **desde el exterior hacia el dominio**, y nunca al revés.

---

## 🧩 Principios de Arquitectura

- **Dominio independiente:** el núcleo del dominio no debe depender de frameworks ni de librerías externas.
- **Separación de responsabilidades:** cada capa tiene un propósito claro y limitado.
- **Inversión de dependencias:** las abstracciones residen en el dominio, las implementaciones en la infraestructura.
- **Alta cohesión y bajo acoplamiento.**
- **Infraestructura reemplazable:** todo componente externo debe poder sustituirse sin modificar la lógica central.
- **Diseño orientado a puertos:** los casos de uso se exponen mediante interfaces (puertos) implementadas por adaptadores.

---

## 🧠 Estructura Arquitectónica

### Dominio (Core) - ❌ SIN FRAMEWORKS
Contiene:
- Entidades, Value Objects, Agregados
- Reglas de negocio puras
- Servicios de dominio (si aplica)
- Eventos del dominio (si aplica)
- Puertos de salida (interfaces para repositorios)

**IMPORTANTE:** El dominio NO debe tener dependencias de frameworks (Spring, JPA, etc.). Sin anotaciones como @Service, @Entity, @Component.

### Aplicación - ✅ PUEDE USAR FRAMEWORKS
Contiene:
- Casos de uso o puertos de entrada (interfaces)
- Servicios de aplicación (implementaciones)
- Orquestación del flujo de negocio
- Coordinación con puertos de salida
- Mapeo de datos entre dominio e infraestructura

**IMPORTANTE:** Los servicios de aplicación SÍ pueden usar anotaciones de Spring (@Service, @RequiredArgsConstructor, etc.). Su propósito es orquestar, no contener lógica de negocio.

### Infraestructura (Adapters) - ✅ PUEDE USAR FRAMEWORKS
Contiene:
- Implementaciones concretas de los puertos de salida (bases de datos, APIs, colas, archivos, etc.)
- Adaptadores de entrada (REST, mensajería, CLI, batch, etc.)
- Configuración técnica (frameworks, seguridad, persistencia)

**IMPORTANTE:** Esta capa usa completamente frameworks (JPA @Entity, Spring @Repository, @RestController, etc.).

---

## 🧰 Patrones de Diseño Utilizados

- **Repository Pattern:** abstrae el acceso a datos.
- **Factory / Builder:** para la creación de objetos complejos del dominio.
- **Strategy:** permite intercambiar comportamientos en tiempo de ejecución.
- **Template Method:** define algoritmos con pasos personalizables.
- **Domain Events:** comunicación interna sin acoplamiento entre entidades o agregados.
- **CQRS (opcional):** separación de comandos y consultas cuando la complejidad lo requiere.
- **Specification Pattern:** encapsula reglas de negocio reutilizables.

---

## ⚙️ Convenciones de Implementación

### Uso de Frameworks por Capa

**Dominio (domain/):**
- ❌ NO usar anotaciones de Spring (@Service, @Component, @Autowired)
- ❌ NO usar anotaciones de JPA (@Entity, @Table, @Column)
- ❌ NO inyectar dependencias mediante frameworks
- ✅ Solo Java puro con lógica de negocio
- ✅ Constructores simples sin frameworks

**Aplicación (application/):**
- ✅ Usar @Service en servicios de aplicación
- ✅ Usar @RequiredArgsConstructor de Lombok para inyección
- ✅ Inyectar dependencias mediante constructor
- ⚠️  NO contener lógica de negocio, solo orquestación

**Infraestructura (infrastructure/):**
- ✅ Usar todas las anotaciones necesarias (@Entity, @Repository, @RestController, @RequestMapping, etc.)
- ✅ Frameworks completos (Spring Data JPA, Spring Web, etc.)

### Otras Convenciones

- Los puertos (interfaces) definen los puntos de interacción entre capas.
- Las implementaciones concretas viven fuera del dominio.
- Evitar clases o métodos estáticos para la lógica de negocio.
- Los servicios de aplicación deben ser delgados: su propósito es orquestar, no contener lógica.
- Las pruebas unitarias deben cubrir la lógica de dominio de forma aislada.

---

## 🧩 Guía para Nuevos Casos de Uso

1. Crear una interfaz que represente el **puerto de entrada** del caso de uso.
2. Implementar el caso de uso en un **servicio de aplicación**.
3. Definir los **puertos de salida** necesarios (interfaces para persistencia o servicios externos).
4. Implementar dichos puertos como **adaptadores de infraestructura**.
5. Asegurar que la lógica principal se mantenga en el dominio.
6. Exponer el caso de uso mediante un adaptador de entrada (ej. API REST, evento, comando).

---

## ✅ Buenas Prácticas

- Mantener el **dominio libre de anotaciones** o dependencias de frameworks.
- Evitar colocar lógica de negocio en controladores, repositorios o adaptadores.
- Usar DTOs o mapeadores para aislar los modelos del dominio.
- Respetar el **Principio de Deméter** (“tell, don’t ask”).
- Aplicar **principios SOLID** consistentemente.
- Definir interfaces claras para los contratos de comunicación entre capas.
- Tests:
    - **Unitarios:** dominio y servicios de aplicación.
    - **Integración:** adaptadores e infraestructura.
- Mantener una **cobertura mínima del 80%** en componentes críticos.

---

## 📝 Convenciones de Git y Commits

### Formato de Commits

Todos los commits deben estar en **inglés** y seguir el formato:

```
<type>: <description>

<optional body>
```

### Tipos de Commit

- **feat**: Nueva funcionalidad
- **fix**: Corrección de bugs
- **refactor**: Refactorización de código sin cambiar funcionalidad
- **docs**: Cambios en documentación
- **test**: Agregar o modificar tests
- **chore**: Tareas de mantenimiento (dependencias, configuración)
- **style**: Cambios de formato (sin afectar lógica)

### Reglas de Commits

1. **Descripción clara y descriptiva**: Explica QUÉ y POR QUÉ, no CÓMO
   - ✅ `feat: add unique name validation for tournaments`
   - ❌ `feat: add method`

2. **Idioma**: Siempre en inglés

3. **Atomic commits**: Un commit por concepto lógico
   - Separa diferentes funcionalidades en commits distintos
   - Agrupa cambios relacionados en un solo commit

4. **Mensajes descriptivos**:
   - Primera línea: máximo 72 caracteres
   - Cuerpo opcional: explica contexto si es necesario
   - Usa imperativo: "add" no "added" o "adds"

### Ejemplos

**Buenos commits:**
```
feat: implement tournament domain model with state machine

Add Tournament entity with business rules for state transitions:
- CREATED → IN_PROGRESS → COMPLETED
- Cancellation only allowed for non-completed tournaments
```

```
refactor: extract guard clauses to private methods in Tournament

Improve readability by extracting validation logic into:
- ensureIsCreated()
- ensureIsInProgress()
- ensureIsNotCompleted()
```

**Malos commits:**
```
❌ fix: arreglar bug
❌ feat: cambios
❌ update code
```

---

## 📖 Documentación de API

### OpenAPI / Swagger

El proyecto utiliza **OpenAPI 3.0** para documentar todos los endpoints de la API REST.

**Ubicación:** `src/main/resources/api.yml`

**Principios de Documentación:**

1. **Completitud**: Documentar todos los endpoints con:
   - Descripción clara del propósito
   - Parámetros de entrada con validaciones
   - Posibles respuestas (éxito y errores)
   - Ejemplos de uso

2. **Estructura OpenAPI**:
   - `paths`: Definición de endpoints por ruta
   - `components/schemas`: Modelos de datos reutilizables
   - `components/parameters`: Parámetros compartidos
   - Tags para agrupar endpoints relacionados

3. **Schemas Documentados**:
   - **Request DTOs**: Validaciones, longitudes, patrones
   - **Response DTOs**: Estructura de respuesta exitosa
   - **ErrorResponse**: Formato estándar de errores
   - **Enums**: Valores permitidos con descripciones

4. **Códigos de Estado HTTP**:
   - `200`: Operación exitosa (GET, PUT, PATCH)
   - `201`: Recurso creado (POST)
   - `204`: Sin contenido (DELETE exitoso)
   - `400`: Error de validación o regla de negocio
   - `404`: Recurso no encontrado

5. **Mantenimiento**:
   - Actualizar `api.yml` al agregar/modificar endpoints
   - Sincronizar con cambios en DTOs y validaciones
   - Incluir ejemplos representativos

**Visualización:**
- Importar `api.yml` en Swagger Editor: https://editor.swagger.io/
- Usar herramientas como Postman para importar la colección

---

## 🔄 Versionamiento y Evolución

- Toda modificación arquitectónica significativa debe documentarse en este archivo.
- Las **decisiones de arquitectura (ADR)** deben almacenarse en el directorio `/docs/adr/`.
- Las revisiones técnicas deben realizarse periódicamente para asegurar la adherencia a los principios.
- Los componentes de infraestructura deben poder reemplazarse sin impacto en la capa de dominio.

---

## 🧩 Evaluación de Calidad Arquitectónica

- **Mantenibilidad:** facilidad para agregar nuevos casos de uso sin modificar el núcleo.
- **Escalabilidad:** facilidad para agregar nuevos adaptadores o tecnologías.
- **Aislamiento:** capacidad para probar el dominio sin infraestructura.
- **Simplicidad:** preferir claridad sobre complejidad innecesaria.
- **Trazabilidad:** cada decisión debe poder justificarse mediante principios y patrones definidos.

---

## 📚 Referencias

- *“Hexagonal Architecture”* — Alistair Cockburn
- *“Clean Architecture”* — Robert C. Martin
- *“Implementing Domain-Driven Design”* — Vaughn Vernon
- *“Patterns of Enterprise Application Architecture”* — Martin Fowler

---

**Fin del documento `CLAUDE.md`**
