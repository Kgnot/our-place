# Convenciones para crear un módulo nuevo

Guía de referencia para levantar cualquier módulo de dominio (`identity`, `affection`, `pet`, etc.)
de forma consistente. Si estás copiando un módulo existente como plantilla, revisa esta guía primero —
hay decisiones que **no** deben copiarse sin pensar (ver checklist al final).

> Esta guía asume módulos **sin mucha lógica de dominio compleja** (CRUD con algunas invariantes simples,
> tipo `affection` o `pet`). Para módulos con agregados grandes, múltiples invariantes cruzadas o máquinas
> de estado (tipo `identity`), el patrón base sigue aplicando pero seguramente necesites más comportamiento
> dentro de la entidad, no solo en el caso de uso.

---

## 1. Estructura de carpetas de un módulo

```
org.our_place.<modulo>
├── api/
│   ├── <Modulo>Api.java              # puerto para consumo IN-PROCESS de otros módulos
│   ├── <Modulo>ApiImpl.java          # implementación del puerto — NO es el controller
│   └── events/
│       └── <Evento>.java             # domain events (implements DomainEvent)
├── controller/
│   ├── <Entidad>Controller.java      # SOLO capa HTTP
│   ├── request/
│   │   └── <Accion>Request.java      # forma del JSON de entrada, con @Valid
│   └── response/
│       └── <Entidad>Response.java    # forma del JSON de salida
├── usecase/
│   ├── <Accion>UseCase.java          # escritura, implementa UseCase<C,R>
│   ├── command/
│   │   └── <Accion>Command.java
│   └── output/
│       └── <Accion>Output.java
├── service/
│   ├── <Entidad>QueryService.java    # lectura, @Transactional(readOnly = true)
│   └── dto/
│       └── <Entidad>Dto.java
├── domain/
│   ├── vo/                           # value objects (Email, Role, UserStatus...)
│   └── exception/                    # extends ResultException
└── persistence/
    ├── entity/
    └── repository/
```

---

## 2. CQRS ligero: servicios vs casos de uso

Sin bus de comandos, sin proyecciones separadas — solo separación de intención:

- **Casos de uso (`usecase/`)** → **escritura**. Protegen invariantes de negocio, mutan agregados, publican eventos.
- **Servicios (`service/`)** → **lectura**. Proyectan a DTO, sin lógica de negocio, `@Transactional(readOnly = true)` a nivel de clase.

Un caso de uso nunca debería usarse para leer y mostrar datos; un servicio nunca debería mutar estado.

---

## 3. Contrato de caso de uso

```java
public interface UseCase<C, R> {
    R execute(C command);
}
```

Cada caso de uso:
- Vive en el paquete `usecase` (por convención de nombre, para que el AOP de logging lo enganche sin anotarlo).
- Recibe un `Command` (record, en `usecase/command`).
- Devuelve un `Output` (record, en `usecase/output`).
- Usa `@Component` + `@RequiredArgsConstructor`.

### Regla de `@Transactional`

- **Toca más de un repositorio, o muta una entidad confiando en dirty checking** → `@Transactional` explícito, siempre.
- Un solo `.save()` → técnicamente no hace falta, pero se pone igual: el día que el caso de uso crezca a una
  segunda operación, ya está lista.
- Si el flujo debe persistir un efecto secundario **aunque lance una excepción de negocio** (ej. contar intentos
  fallidos de login), usar `noRollbackFor`:
  ```java
  @Transactional(noRollbackFor = InvalidCredentialsException.class)
  ```
  Sin esto, Spring hace rollback de toda la transacción al lanzar cualquier `RuntimeException`, y la mutación
  se pierde silenciosamente — sin error, sin aviso.

### Publicación de eventos

Publicar el evento **dentro** de la transacción del caso de uso. Quien escuche debe usar
`@TransactionalEventListener(phase = AFTER_COMMIT)` si su efecto es irreversible (enviar un correo, por
ejemplo) — así no se dispara si la transacción termina en rollback.

---

## 4. Value Objects vs entidades JPA de catálogo

Un catálogo (`Lkp*`) es la entidad JPA que persiste el FK. El VO (`domain/vo/Role`, `UserStatus`, etc.) es
la forma de trabajar ese mismo concepto **en lógica de negocio**, con validación en construcción:

```java
public record UserStatus(String code) {
    public static final UserStatus ACTIVE = new UserStatus("active");
    // ...
    public boolean allowsLogin() { return ACTIVE.equals(this); }
}
```

⚠️ **Riesgo real, ya nos pasó**: si defines el VO pero la lógica de negocio sigue comparando el `.code()` de
la entidad JPA directamente, el VO queda "bonito y sin usar" y las invariantes que debería proteger no se
aplican en el camino real de ejecución. Al introducir un VO nuevo, buscar y actualizar **todos** los puntos
donde hoy se compara el string/entidad a mano.

---

## 5. Excepciones de dominio

Todas extienden `ResultException` (no `RuntimeException` a secas), para que `GlobalExceptionHandler` las
mapee automáticamente a la respuesta HTTP correcta vía `ResultIssue`:

```java
public class MiExcepcion extends ResultException {
    public MiExcepcion(...) {
        super(
            "mensaje técnico para logs",
            new ResultIssue("CODIGO_ESTABLE", "mensaje legible para el cliente", ResultIssue.Severity.WARNING)
        );
    }
}
```

`WARNING` → 400, `CRITICAL` → 500 (según el `GlobalExceptionHandler` actual). Si un caso no encaja bien en
esas dos (ej. "no encontrado" debería ser 404), es una discusión pendiente sobre extender `ResultIssue`, no
algo que se decida módulo por módulo.

---

## 6. Repositorios + factory `create()` para entidades con UUID

Si la entidad tiene PK `UUID` generada en la aplicación, **nunca construirla con `new Entidad()` +
setters sueltos desde el caso de uso**. Debe existir un factory estático `create(...)` en la propia
entidad, que sea el único punto donde se arma una instancia nueva válida:

```java
@Entity
public class Pet implements Persistable<UUID> {

    @Transient
    private boolean isNew = false;

    public static Pet create(UUID roomId, LkpSpecies species, String name, UUID createdByUserId) {
        Pet pet = new Pet();
        pet.id = UUID.randomUUID();
        pet.isNew = true;
        pet.roomId = roomId;
        pet.species = species;
        pet.name = name;
        pet.createdByUserId = createdByUserId;
        pet.createdAt = OffsetDateTime.now();
        return pet;
    }

    @Override
    public boolean isNew() { return isNew; }

    @PostPersist
    @PostLoad
    void markNotNew() { this.isNew = false; }
}
```

Por qué importa, no es solo estilo:

- **Evita instancias a medio construir.** Sin factory, es fácil olvidar un campo obligatorio (`createdAt`,
  el `id`) porque no hay un único lugar que obligue a pasar todo lo necesario.
- **Resuelve el bug de `Persistable` de raíz.** El `id = UUID.randomUUID()` y el `isNew = true` quedan
  atados en el mismo sitio — no hay forma de generar el UUID sin marcar `isNew`, que es justo lo que
  causa el `TransientPropertyValueException` cuando se hace a mano y se olvida.
- El repositorio (`interface XRepository extends JpaRepository<X, UUID>`) se mantiene sin lógica — solo
  queries. Toda construcción de entidad vive en el `create()` de la entidad, nunca en el repositorio ni
  esparcida en el caso de uso.

Si la PK es `bigserial`/`IDENTITY` (la genera la base), no aplica `Persistable`, pero igual conviene un
`create(...)` estático por consistencia y para no dejar setters públicos sueltos en la entidad.

---

## 7. API interna (puerto para otros módulos)

**El controller NO implementa la interfaz de API interna.** Son dos contratos que evolucionan por separado:
el controller es capa HTTP (status codes, `@RequestBody`, paginación futura); la API interna es para que
otro módulo la inyecte directo, in-process, sin HTTP de por medio.

Toda API interna se anota con `@SharedApi`, con una descripción breve de qué expone. Es lo que marca esa
interfaz como puerto consumible por otros módulos (a diferencia de una interfaz interna cualquiera):

```java
// api/MiModuloApi.java — el puerto
@SharedApi(description = "API interna del módulo <modulo>, consumible in-process por otros módulos")
public interface MiModuloApi {
    MiOutput accion(MiCommand command);
}

// api/MiModuloApiImpl.java — el adapter, separado del controller
@Component
@RequiredArgsConstructor
public class MiModuloApiImpl implements MiModuloApi {
    private final MiUseCase miUseCase;
    @Override
    public MiOutput accion(MiCommand command) { return miUseCase.execute(command); }
}

// controller/MiController.java — SOLO HTTP, no implementa nada del dominio
@RestController
@RequiredArgsConstructor
public class MiController {
    private final MiUseCase miUseCase; // inyecta el caso de uso directo, no pasa por el Api
    ...
}
```

---

## 8. Controladores

- `Request` (en `controller/request`) ≠ `Command` (en `usecase/command`). El Request es la forma del JSON
  con validaciones `@NotBlank`/`@Email`; el Command es lo que consume el dominio, ya limpio. No los fusiones
  aunque hoy se vean idénticos.
- Datos que identifican **quién hace la acción** (ej. `authorUserId`) salen de `SecurityContextApi.getCurrentUserId()`,
  **nunca del body ni del path** — si no, cualquiera puede suplantar a otro usuario en el request.
- El recurso sobre el que se actúa (`roomId`, `userLoginId`) va en la **ruta**, no en el body.
- `POST` que crea recurso → `201 Created`. El resto → `200 OK`.

---

## 9. Eventos de dominio

Igual que la API interna, todo evento se anota con `@SharedDomain` describiendo cuándo se dispara:

```java
@SharedDomain(description = "Event triggered when <qué pasó en el dominio>")
public record MiEvento(
        UUID entidadId,
        // ... campos relevantes
) implements DomainEvent {
    @Override public UUID eventId() { return UUID.randomUUID(); }
    @Override public Instant occurredAt() { return Instant.now(); }
    @Override public String key() { return "mi_entidad.creada"; }
    @Override public EventScope scope() { return EventScope.INTERNAL; }
}
```

Viven en `api/events/` porque son parte del contrato público del módulo hacia otros módulos, igual que la API interna.
`@SharedApi` y `@SharedDomain` son la señal visual/documental de "esto cruza la frontera del módulo" — cualquier
interfaz o evento sin esa anotación se asume de uso interno exclusivo.

---

## 10. Logging por AOP — no lo dupliques

El aspecto de logging de casos de uso apunta con un pointcut **global**:

```java
@Around("execution(* org.our_place..usecase..*UseCase.execute(..)) && args(command)")
```

Los `..` intermedios significan "cualquier paquete debajo de `org.our_place`" — esto ya cubre **todos los
módulos**, no solo el que lo definió originalmente. **No crear un aspecto nuevo por módulo** — solo hace que
el mismo caso de uso quede logueado dos veces. Si se necesita mover el aspecto a un paquete verdaderamente
compartido (`shared.config.aop`) para que quede explícito que es cross-cutting, es una refactorización
consciente, no una copia accidental.

⚠️ Nunca loguear el `Command` completo (`.toString()`) — solo su nombre de clase. Un command con contraseña,
token, etc. se filtraría a los logs en texto plano.

---

## Checklist rápido al crear un módulo nuevo

- [ ] ¿El caso de uso necesita `@Transactional`? ¿Necesita `noRollbackFor`?
- [ ] ¿Las excepciones de dominio extienden `ResultException`?
- [ ] ¿El controller implementa la API interna por error? → no debería
- [ ] ¿Quién identifica al actor de la acción? → `SecurityContextApi`, nunca el body
- [ ] ¿Ya existe un aspecto de logging global? → no dupliques
- [ ] ¿Hay VOs de dominio? → verificar que la lógica real los use, no solo la entidad JPA