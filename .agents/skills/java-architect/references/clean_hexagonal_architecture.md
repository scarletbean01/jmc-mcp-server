# Clean Architecture and Hexagonal Architecture Patterns

Reference source: Extracted from "Get Your Hands Dirty on Clean Architecture" by Tom Hombergs.

## Table of Contents

1. [Core Principles](#1-core-principles)
2. [Hexagonal Architecture Structure](#2-hexagonal-architecture-structure)
3. [Organizing Code](#3-organizing-code)
4. [Implementing Use Cases](#4-implementing-use-cases)
5. [Implementing Adapters](#5-implementing-adapters)
6. [Mapping Strategies](#6-mapping-strategies)
7. [Testing Strategy](#7-testing-strategy)
8. [Assembling the Application](#8-assembling-the-application)
9. [Enforcing Boundaries](#9-enforcing-architecture-boundaries)
10. [Shortcuts and Trade-offs](#10-taking-shortcuts-consciously)
11. [Architecture Style Decision Framework](#11-architecture-style-decision-framework)

---

## 1. Core Principles

### Single Responsibility Principle (SRP)

A component should have only one reason to change. "Reason to change" means a source of change that can propagate through dependencies. Each dependency of a component is a potential reason to change that component.

- **Implication for architecture**: Minimize dependencies pointing toward important code (domain code)
- **Practice**: Create narrow, focused components rather than broad general-purpose ones

### Dependency Inversion Principle (DIP)

High-level modules should not depend on low-level modules. Both should depend on abstractions. Abstractions should not depend on details. Details should depend on abstractions.

- **Applied to architecture**: Domain code (high-level) should not depend on persistence or UI code (low-level)
- **Mechanism**: Interfaces/ports defined in the domain layer, implemented by outer layers

### Interface Segregation Principle (ISP)

Broad interfaces should be split into specific ones so that clients only depend on methods they need.

- **Applied to ports**: Create narrow port interfaces (often one method per port) rather than broad repository interfaces
- **Benefit**: Services only depend on exactly the methods they need

### Dependency Rule (Clean Architecture)

All dependencies between layers must point inward, toward the domain logic. The domain code must not have any outward-facing dependencies.

- **Domain entities** are at the core
- **Use cases** surround the entities
- **Interface adapters** surround use cases
- **Frameworks and drivers** are outermost

---

## 2. Hexagonal Architecture Structure

### Core Concepts

**Hexagonal Architecture** (also called Ports and Adapters) applies Clean Architecture principles:

- The **application core** (hexagon) contains domain entities and use cases
- The hexagon has **no outgoing dependencies** - all dependencies point inward
- **Adapters** outside the hexagon translate between the application and external systems
- **Ports** define the interfaces through which adapters communicate with the core

### Adapter Types

**Driving (Incoming) Adapters**: Call the application core
- Web adapters (REST controllers, web UIs)
- CLI adapters
- Message queue consumers
- Test adapters

**Driven (Outgoing) Adapters**: Called by the application core
- Persistence adapters (database access)
- External API clients
- Email/SMS senders
- File system adapters
- Message queue publishers

### Port Types

**Incoming Ports**: Interfaces implemented by use cases, called by driving adapters
- Define what the application can do
- Each use case has a dedicated incoming port (e.g., `SendMoneyUseCase`)

**Outgoing Ports**: Interfaces implemented by outgoing adapters, called by use cases
- Define what the application needs from the outside
- Narrow, focused interfaces (e.g., `LoadAccountPort`, `UpdateAccountStatePort`)

---

## 3. Organizing Code

### Package Structure (Architecturally Expressive)

Organize code so the architecture is visible at a glance:

```
com.example.application/
  account/
    adapter/
      in/
        web/
          SendMoneyController.java
      out/
        persistence/
          AccountPersistenceAdapter.java
    application/
      port/
        in/
          SendMoneyUseCase.java
          SendMoneyCommand.java
        out/
          LoadAccountPort.java
          UpdateAccountStatePort.java
      service/
        SendMoneyService.java
    domain/
      Account.java
      Activity.java
      Money.java
```

### Alternative: Organize by Feature

```
com.example.application/
  account/
    SendMoneyController.java
    SendMoneyService.java
    AccountRepository.java
    Account.java
```

### Key Rules

- High-level packages represent **bounded contexts** (DDD)
- Each architecture element maps to a package
- Use package-private visibility to enforce boundaries within modules
- The package structure should reduce the "architecture-code gap"

---

## 4. Implementing Use Cases

### Use Case Steps

Every use case follows this pattern:
1. **Take input** - Receive input model (command/query)
2. **Validate business rules** - Check rules requiring model state access
3. **Manipulate model state** - Execute domain logic
4. **Return output** - Return use-case-specific output

**Example: Send Money Use Case**

```java
@RequiredArgsConstructor
public class SendMoneyService implements SendMoneyUseCase {
    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {
        // Validate business rules
        AccountId sourceAccountId = command.getSourceAccountId();
        AccountId targetAccountId = command.getTargetAccountId();

        accountLock.lockAccount(sourceAccountId);
        accountLock.lockAccount(targetAccountId);

        try {
            // Load domain objects
            Account sourceAccount = loadAccountPort.loadAccount(
                sourceAccountId);
            Account targetAccount = loadAccountPort.loadAccount(
                targetAccountId);

            // Execute domain logic
            sourceAccount.withdraw(command.getMoney(), targetAccountId);
            targetAccount.deposit(command.getMoney(), sourceAccountId);

            // Persist changes
            updateAccountStatePort.updateActivities(sourceAccount);
            updateAccountStatePort.updateActivities(targetAccount);

            return true;
        } finally {
            accountLock.releaseAccount(sourceAccountId);
            accountLock.releaseAccount(targetAccountId);
        }
    }
}
```

### Input Validation vs Business Rule Validation

| Aspect | Input Validation | Business Rule Validation |
|--------|-----------------|-------------------------|
| Needs model state | No | Yes |
| Type | Syntactical | Semantical |
| Location | Input model constructor | Domain entity or use case code |
| Implementation | Declarative (@NotNull, etc.) | Imperative code |

### Input Model Best Practices

- **Dedicated input model per use case** - never share between use cases
- **Immutable** - all fields final, validated in constructor
- **Self-validating** - constructor throws on invalid input
- **Prefer constructors over builders** - compile-time safety for required fields

**Example: Send Money Command (Input Model)**

```java
@Getter
public class SendMoneyCommand {
    private final AccountId sourceAccountId;
    private final AccountId targetAccountId;
    private final Money money;

    public SendMoneyCommand(
            AccountId sourceAccountId,
            AccountId targetAccountId,
            Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;
        requireNonNull(sourceAccountId);
        requireNonNull(targetAccountId);
        requireNonNull(money);
        requireGreaterThan(money, 0);
    }
}
```

### Output Model Best Practices

- Return as little as possible (boolean for simple operations)
- Dedicated output model per use case
- Avoid using domain entities as output models (couples entity to use case)

### Rich vs Anemic Domain Model

**Rich Domain Model**: Domain entities contain most logic
- Use case acts as entry point, orchestrates entity method calls
- Business rules live in entities
- Best for complex domains

**Anemic Domain Model**: Entities are thin data holders
- Domain logic lives in use case classes
- Entities have getters/setters only
- Best for simple CRUD operations

### Read-Only Operations (Queries)

- Can be implemented as query services with dedicated ports
- Distinguish from modifying use cases (commands)
- Aligns with CQRS (Command Query Responsibility Segregation)
- Query services may be thin pass-through to persistence

---

## 5. Implementing Adapters

### Web Adapter Responsibilities

1. Map HTTP requests to objects
2. Perform authentication/authorization checks
3. Validate input (web-level validation, different from use case validation)
4. Map input to use case input model
5. Call the use case through incoming port
6. Map output to HTTP response
7. Handle exceptions and return appropriate HTTP status codes

**Example: Send Money Web Adapter**

```java
@RestController
@RequiredArgsConstructor
public class SendMoneyController {
    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}")
    ResponseEntity sendMoney(
            @PathVariable("sourceAccountId") Long sourceAccountId,
            @PathVariable("targetAccountId") Long targetAccountId,
            @RequestBody SendMoneyBody body) {

        // Map HTTP input to use case input model
        SendMoneyCommand command = new SendMoneyCommand(
            new AccountId(sourceAccountId),
            new AccountId(targetAccountId),
            Money.of(body.getAmount())
        );

        // Call use case
        boolean success = sendMoneyUseCase.sendMoney(command);

        // Map output to HTTP response
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

@Data
static class SendMoneyBody {
    private BigDecimal amount;
}
```

### Web Adapter Best Practices

- **Slice controllers narrowly** - one controller per operation, not per resource
- **Avoid shared resource models** - each controller uses its own input/output models
- **Name controllers after use cases** (e.g., `SendMoneyController`, not `AccountController`)
- **Never leak HTTP into application layer**

### Persistence Adapter Responsibilities

1. Take input through outgoing port interface
2. Map input to database format (e.g., JPA entities)
3. Send to database (query or update)
4. Map database output to domain/application format
5. Return output

**Example: Account Persistence Adapter**

```java
@RequiredArgsConstructor
@Component
public class AccountPersistenceAdapter implements
        LoadAccountPort, UpdateAccountStatePort {

    private final SpringDataAccountRepository accountRepository;
    private final ActivityRepository activityRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account loadAccount(AccountId accountId) {
        AccountJpaEntity account = accountRepository
            .findById(accountId.getValue())
            .orElseThrow(() -> new EntityNotFoundException());

        List<ActivityJpaEntity> activities = activityRepository
            .findByOwnerSince(accountId.getValue(),
                LocalDateTime.now().minusDays(10));

        Long withdrawalBalance = orZero(activityRepository
            .getWithdrawalBalanceUntil(accountId.getValue()));
        Long depositBalance = orZero(activityRepository
            .getDepositBalanceUntil(accountId.getValue()));

        return accountMapper.mapToDomainEntity(
            account, activities, withdrawalBalance, depositBalance);
    }

    @Override
    public void updateActivities(Account account) {
        for (Activity activity : account.getActivityWindow().getActivities()) {
            if (activity.getId() == null) {
                activityRepository.save(accountMapper.mapToJpaEntity(activity));
            }
        }
    }

    private Long orZero(Long value) {
        return value == null ? 0L : value;
    }
}
```

### Persistence Adapter Best Practices

- **Narrow port interfaces** - apply ISP, one method per port when possible
- **One adapter per aggregate** - aligns with DDD boundaries
- **Separate domain model from persistence model** when needed
- **Transaction boundaries belong in use cases** (application layer), not persistence adapters

---

## 6. Mapping Strategies

### No Mapping Strategy

All layers share the same model. Domain entities used across all layers.

- **When to use**: Simple CRUD use cases where all layers need the same data
- **Pros**: No mapping overhead, rapid development
- **Cons**: Domain model polluted with framework annotations (JSON, ORM), SRP violation

### Two-Way Mapping Strategy

Each layer has its own model. Adapters map between their model and the domain model.

- **When to use**: When layers have different model requirements
- **Pros**: Clean separation, each layer optimizes its own model
- **Cons**: Boilerplate mapping code, debugging complexity

### Full Mapping Strategy

Each use case has its own dedicated input and output models.

- **When to use**: Between web and application layers for state-modifying use cases
- **Pros**: Maximum decoupling between use cases, crisp validation per use case
- **Cons**: Most mapping code, but mapping is simpler (one-to-one)

### One-Way Mapping Strategy

Models implement a shared "state" interface. Each layer only maps what it receives.

- **When to use**: When models across layers are similar, read-heavy operations
- **Pros**: Distributed mapping responsibility, works with DDD factories
- **Cons**: Conceptually more complex

### Choosing a Strategy

- **Decide per use case**, not globally
- **Modifying use cases**: Full mapping (web<->app), No mapping (app<->persistence)
- **Queries**: No mapping everywhere, move to Two-way when needed
- **Evolve over time** - start simple, add mapping when pain emerges

---

## 7. Testing Strategy

### Test Pyramid

```
    /\
   /  \  System Tests (few)
  /____\
 /      \  Integration Tests (some)
/________\
          \  Unit Tests (many)
```

### Unit Tests

**Domain Entities**:
- Instantiate single class, test through public interface
- Mock external dependencies
- Best for verifying business rules

**Use Cases**:
- Mock all port interfaces
- Verify interactions with mocked dependencies
- Structure in given/when/then sections
- Don't verify all interactions - focus on most important ones

### Integration Tests

**Web Adapters**:
- Test within the web framework (e.g., @WebMvcTest in Spring)
- Mock use case ports
- Verify HTTP mapping, validation, routing
- Trust the framework - don't test HTTP protocol itself

**Persistence Adapters**:
- Test against real database (use Testcontainers)
- Verify mapping to/from database
- Use SQL scripts to set up database state
- Don't mock the database

### System Tests

- Start complete application
- Send real HTTP requests
- Verify end-to-end behavior
- Cover important user scenarios/paths
- Build a domain-specific testing vocabulary

### Testing Guidelines

- Cover domain entities with unit tests as you implement them
- Cover use cases with unit tests as you implement them
- Cover adapters with integration tests as you implement them
- Cover main user paths with system tests
- Test **while implementing**, not after
- If tests require constant updating during refactoring, improve test robustness

---

## 8. Assembling the Application

### Dependency Injection

A neutral configuration component instantiates all classes and wires dependencies:

- Must have access to all layers (lives in outermost layer)
- Creates adapter instances, use case instances, wires them together
- Responsible for routing (HTTP to web adapters), database connections

### Assembly Approaches

**Plain Code Assembly**:
- Manual instantiation in main/bootstrap method
- Full control, no framework dependency
- Verbose for large applications

**Classpath Scanning (e.g., Spring)**:
- Framework finds @Component annotated classes
- Automatic wiring via constructor injection
- Convenient but "magical" - less transparency

**Java Config (e.g., Spring @Configuration)**:
- Explicit @Bean factory methods
- Scoped configuration classes per module
- Balance of control and convenience
- Enables partial context startup for tests

### Best Practices

- Keep application code framework-agnostic when possible
- Create stereotype annotations for architecture elements (@PersistenceAdapter, @WebAdapter)
- Group configuration by module/layer
- Use package-private visibility for classes that don't need to be public

---

## 9. Enforcing Architecture Boundaries

### Three Levels of Enforcement

**1. Visibility Modifiers (Compiler)**:
- Use package-private to hide implementation classes
- Public only for: port interfaces, domain entities, entry points
- Limitation: breaks down with sub-packages (Java limitation)

**2. Post-Compile Checks (Build)**:
- Tools like ArchUnit verify dependency directions
- Automated tests that fail on architecture violations
- Run in CI pipeline
- Limitation: must be maintained alongside code

**3. Build Artifacts (Strongest)**:
- Separate JAR/modules per architecture layer
- Build tool enforces declared dependencies
- Prevents circular dependencies by design
- Allows isolated work on modules
- Cost: must maintain build scripts

### Recommended Approach

Combine all three: package-private where possible + ArchUnit tests + modular builds when architecture stabilizes.

---

## 10. Taking Shortcuts Consciously

### Broken Windows Theory

Code quality is contagious. When one shortcut exists, the threshold for adding more drops. Start clean to maintain quality over time.

### Documented Shortcuts

When taking a shortcut, document it with an Architecture Decision Record (ADR) explaining why.

### Common Shortcuts and Their Effects

**Sharing Models Between Use Cases**:
- Effect: Couples use cases - changes to shared model affect multiple use cases
- Acceptable when: Use cases are functionally bound and should evolve together

**Using Domain Entities as Input/Output Models**:
- Effect: Domain entity gains reasons to change from outer layers
- Acceptable when: Simple CRUD operations that won't grow complex

**Skipping Incoming Ports**:
- Effect: Loses clearly marked entry points to the application
- Acceptable when: Application is small with single incoming adapter

**Skipping Application Services (CRUD through persistence adapter)**:
- Effect: Domain logic may leak into persistence layer
- Acceptable when: True CRUD with zero domain logic
- Must have: Clear guideline for when to introduce application services

---

## 11. Architecture Style Decision Framework

### When to Use Hexagonal/Clean Architecture

**The domain is king**: Use when domain code is the most important part of the application and should drive development.

**Best fits**:
- Complex business logic
- Domain-Driven Design practices
- Long-lived applications that will evolve
- Multiple UI channels or external integrations
- Applications where testing is critical

### When Layered Architecture May Suffice

**Consider traditional layered when**:
- Simple CRUD application with minimal business logic
- Short-lived prototype or throwaway code
- Team has deep layered architecture experience
- Database-driven design is acceptable

### Decision Factors

| Factor | Hexagonal | Layered |
|--------|-----------|---------|
| Domain complexity | High | Low |
| Expected lifetime | Long | Short |
| Team experience | Has time to learn | Needs immediate productivity |
| Number of UIs | Multiple | Single |
| Test criticality | High | Low |
| DDD adoption | Yes | No |

### Key Insight

All architectures can work if implemented with discipline. The question is which architecture makes it easiest to maintain discipline and quality over time for your specific context.
