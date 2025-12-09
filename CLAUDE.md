# CLAUDE.md
**Autor:** Arquitectura Técnica  
**Versión:** 1.1.0  
**Última actualización:** 2025-10-29  
**Propósito:** Documentar las convenciones, decisiones y buenas prácticas que rigen la arquitectura del sistema, bajo el enfoque de **Arquitectura de Puertos y Adaptadores** con **Vertical Slicing** (evolución moderna de la arquitectura hexagonal clásica).

---

## 🧭 Visión General

Este proyecto adopta la **Arquitectura de Puertos y Adaptadores (Ports and Adapters)** combinada con **Vertical Slicing**, reemplazando la estructura rígida de la arquitectura hexagonal tradicional.  

El objetivo es mantener **independencia del dominio**, **alta modularidad** y **claridad en la organización del código**, donde cada "slice" vertical representa un **módulo funcional autónomo** (por ejemplo: `tournaments`, `players`, `teams`), con sus propias capas internas (dominio, aplicación, infraestructura).

Cada slice puede evolucionar de forma independiente y contiene todos los elementos necesarios para implementar un conjunto coherente de funcionalidades del negocio.

---

## 🧩 Principios Arquitectónicos

- **Dominio puro:** el núcleo del negocio no depende de frameworks ni librerías externas.  
- **Puertos y Adaptadores:** las dependencias fluyen hacia el dominio; los detalles técnicos se implementan en adaptadores.  
- **Slicing vertical:** cada feature (módulo) contiene sus capas internas — dominio, aplicación e infraestructura — en lugar de tener una estructura horizontal compartida.  
- **Alta cohesión y bajo acoplamiento:** cada slice agrupa solo lo que pertenece a su caso de uso.  
- **Reemplazabilidad:** cada adaptador puede sustituirse sin alterar la lógica central.  
- **Escalabilidad modular:** nuevos módulos pueden agregarse sin afectar los existentes.  

---

## 🧠 Estructura Arquitectónica

### 🧱 Estructura por "Slice" (feature module)

Ejemplo:
```
src/
 └── main/java/com/personal/tournament_api/
     ├── tournaments/
     │    ├── domain/
     │    │    ├── Tournament.java
     │    │    ├── TournamentDomainException.java
     │    │    ├── TournamentRepositoryPort.java
     │    │    └── events/
     │    ├── application/
     │    │    ├── CreateTournamentService.java
     │    │    ├── GetTournamentByIdService.java
     │    │    └── commands/
     │    ├── infrastructure/
     │    │    ├── persistence/
     │    │    │    ├── JpaTournamentRepository.java
     │    │    │    └── TournamentEntity.java
     │    │    ├── rest/
     │    │    │    └── TournamentController.java
     │    │    └── mappers/
     │    └── TournamentModuleConfiguration.java
     ├── players/
     │    ├── domain/
     │    ├── application/
     │    ├── infrastructure/
     │    └── PlayerModuleConfiguration.java
     └── shared/
          ├── domain/
          ├── infrastructure/
          └── application/
```

Cada módulo (`tournaments`, `players`, etc.) es **autosuficiente**: define su dominio, sus puertos y sus adaptadores sin depender directamente de otros módulos.

---

### 🧩 Dominio (Core) — ❌ SIN FRAMEWORKS

Contiene:
- Entidades, Value Objects, Agregados  
- Reglas de negocio puras  
- Servicios de dominio (si aplica)  
- Excepciones de dominio  
- Puertos de salida (interfaces para repositorios o servicios externos)  

**Reglas:**
- No usar anotaciones de Spring ni JPA.  
- No inyectar dependencias mediante frameworks.  
- Solo lógica pura en Java.  

---

### ⚙️ Aplicación — ✅ CON FRAMEWORKS

Contiene:
- Casos de uso o puertos de entrada  
- Servicios de aplicación  
- Coordinación entre dominio e infraestructura  
- Mapeo de datos (DTOs ↔ entidades)  

**Reglas:**
- Puede usar Spring (`@Service`, `@Transactional`, etc.)  
- No debe tener lógica de negocio, solo orquestación.  

---

### 🌐 Infraestructura (Adapters) — ✅ CON FRAMEWORKS

Contiene:
- Implementaciones concretas de los puertos del dominio  
- Adaptadores de entrada (REST, eventos, CLI, etc.)  
- Persistencia, seguridad, configuración técnica  

**Reglas:**
- Puede usar Spring Data, JPA, Web, etc.  
- Expone endpoints o integra sistemas externos.  

---

## 🧰 Patrones de Diseño Utilizados

- **Ports and Adapters:** separación clara entre lógica y detalles técnicos.  
- **Repository Pattern:** para abstracción de persistencia.  
- **Factory / Builder:** para construcción de objetos complejos del dominio.  
- **Strategy / Policy:** para intercambiar comportamientos dinámicos.  
- **Domain Events:** para comunicación desacoplada.  
- **Specification Pattern:** para reglas de negocio reutilizables.  
- **CQRS (opcional):** separar comandos y consultas.
- **Entre otros según necesidad.**

---

## ⚙️ Convenciones por Capa

### 🧩 Dominio
- ❌ Sin anotaciones de Spring o JPA.  
- ✅ Solo Java puro.  
- ✅ Lógica de negocio pura.  

### ⚙️ Aplicación
- ✅ Usa `@Service`, `@RequiredArgsConstructor`, etc.  
- ⚠️ Sin lógica de negocio.  
- ✅ Orquesta los flujos.  

### 🌐 Infraestructura
- ✅ Usa anotaciones de frameworks.  
- ✅ Expone controladores REST, repositorios, etc.  

---

## 🧩 Buenas Prácticas

- Mantener el dominio **aislado de frameworks**.
- **Un slice, una responsabilidad.** No mezclar módulos.
- Reutilizar lógica común en `shared/`.
- Evitar servicios "gigantes"; preferir clases pequeñas y específicas.
- Respetar **principios SOLID**.
- Aplicar **DDD táctico** donde tenga sentido (Aggregate Roots, Value Objects, etc.).
- Mantener **test unitarios** para dominio y aplicación, y **tests de integración** para adaptadores.
- Mantener **dominio libre de dependencias circulares**.

### 🗣️ Tell Don't Ask (Principio de Diseño)

**En el Dominio (Entidades y Agregados):**
- ✅ Aplicar **"Tell Don't Ask"**: las entidades deben validar y gestionar sus propios estados internos.
- ✅ La entidad es responsable de garantizar sus invariantes y reglas de negocio internas.
- ❌ No exponer getters para luego validar externamente; la entidad debe encapsular su comportamiento.

**Ejemplo correcto:**
```java
// La entidad valida su propio estado
public class Tournament {
    public void start() {
        if (this.status != TournamentStatus.PENDING) {
            throw new TournamentAlreadyStartedException();
        }
        this.status = TournamentStatus.IN_PROGRESS;
    }
}
```

**En la Capa de Aplicación (Servicios de Aplicación):**
- ✅ Es válido aplicar **"Ask"** cuando se requiere consultar el repositorio.
- ✅ Las entidades no tienen conocimiento del contexto global (otros agregados, unicidad, etc.).
- ✅ Los servicios de aplicación pueden preguntar al repositorio para validar reglas que requieren información externa.

**Ejemplo válido:**
```java
@Service
public class CreateTournamentService {
    public void execute(CreateTournamentCommand command) {
        // "Ask" al repositorio - la entidad no sabe si hay nombres duplicados
        if (tournamentRepository.existsByName(command.getName())) {
            throw new TournamentNameAlreadyExistsException(command.getName());
        }
        Tournament tournament = Tournament.create(command.getName(), ...);
        tournamentRepository.save(tournament);
    }
}
```

**Resumen:**
- **Dominio → Tell:** La entidad gestiona su estado interno.
- **Aplicación → Ask permitido:** El servicio consulta repositorios para reglas que requieren contexto global (unicidad, existencia, relaciones entre agregados, etc.).  

---

## 🧩 Códigos de Estado HTTP

- `200`: Operación exitosa  
- `201`: Recurso creado  
- `204`: Eliminación exitosa (sin cuerpo)  
- `400`: Error de validación o regla de negocio  
- `404`: Recurso no encontrado  
- `409`: Conflicto de negocio (duplicados, inconsistencias)  

---

## 🧪 Testing

- **Dominio:** tests unitarios puros (sin Spring).  
- **Aplicación:** tests unitarios o con mocks de puertos.  
- **Infraestructura:** tests de integración (Spring Boot Test).  
- Mantener cobertura mínima del 80% en lógica crítica.  

---

## 🧰 Commits y Versionamiento

**Formato de commits:**
```
<type>: <description>

<optional body>
```

**Tipos:**
- feat — nueva funcionalidad  
- fix — corrección  
- refactor — cambios internos sin alterar funcionalidad  
- docs — documentación  
- test — pruebas  
- chore — mantenimiento  

**Ejemplo:**
```
feat: implement player registration use case

Add Player entity, repository port, and REST adapter for player creation.
```

**Malos commits:**
```
❌ fix: arreglar bug
❌ feat: cambios
❌ update code
```

**IMPORTANTE - Formato de commits:**
- ❌ **NO incluir** mensajes de herramientas de IA (ej: "Generated with Claude Code")
- ❌ **NO incluir** co-autoría de herramientas (ej: "Co-Authored-By: Claude")
- ✅ Los commits deben reflejar **únicamente el trabajo del desarrollador humano**
- ✅ Mantener commits limpios, profesionales y concisos

**Ejemplo de commit limpio:**
```
feat: implement player registration use case

Add Player entity, repository port, and REST adapter for player creation.
```

---

## 🧭 Evolución Arquitectónica

Esta arquitectura **ya no sigue la estructura horizontal clásica de la Arquitectura Hexagonal**, sino una **versión moderna orientada a módulos verticales** con **Puertos y Adaptadores**, lo que mejora:

- Escalabilidad modular  
- Independencia de features  
- Aislamiento de cambios  
- Capacidad de evolución continua  

Cada módulo (slice) puede evolucionar con sus propias dependencias, puertos y adaptadores, sin romper otros módulos.

---

## 📚 Referencias

- *“Ports and Adapters Architecture”* — Alistair Cockburn  
- *“Clean Architecture”* — Robert C. Martin  
- *“Implementing Domain-Driven Design”* — Vaughn Vernon  
- *“Vertical Slice Architecture”* — Jimmy Bogard  
- *“Patterns of Enterprise Application Architecture”* — Martin Fowler  

---

**Fin del documento `CLAUDE.md`**
