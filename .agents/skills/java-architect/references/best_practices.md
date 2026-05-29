# Software Engineering Best Practices for Enterprise Products

Synthesized from "Patterns of Enterprise Application Architecture" by Martin Fowler and "Get Your Hands Dirty on Clean Architecture" by Tom Hombergs, along with foundational software engineering principles.

## Table of Contents

1. [Architecture Design Principles](#1-architecture-design-principles)
2. [Code Organization Principles](#2-code-organization-principles)
3. [Domain Modeling Best Practices](#3-domain-modeling-best-practices)
4. [Data Persistence Best Practices](#4-data-persistence-best-practices)
5. [API and Interface Design](#5-api-and-interface-design)
6. [Testing Best Practices](#6-testing-best-practices)
7. [Concurrency and Transaction Management](#7-concurrency-and-transaction-management)
8. [Distributed Systems Patterns](#8-distributed-systems-patterns)
9. [Quality and Maintainability](#9-quality-and-maintainability)
10. [Anti-Patterns to Avoid](#10-anti-patterns-to-avoid)

---

## 1. Architecture Design Principles

### Layered Architecture Fundamentals

The most common enterprise application architecture organizes code into layers:

| Layer | Responsibility |
|-------|---------------|
| Presentation | UI, HTTP handling, request/response mapping |
| Application | Use cases, workflow orchestration, transaction boundaries |
| Domain | Business logic, entities, value objects, domain services |
| Data Source | Persistence, external API access, messaging |

**Key rule**: Dependencies should point inward. Higher layers depend on lower layers, not vice versa.

### Dependency Inversion

- Define interfaces (ports) in inner layers
- Implement interfaces in outer layers
- Use dependency injection to wire implementations at runtime
- This decouples domain logic from frameworks, databases, and UI

### SOLID Principles Summary

| Principle | Meaning for Architecture |
|-----------|-------------------------|
| **S**ingle Responsibility | One reason to change per component; narrow services over broad ones |
| **O**pen/Closed | Extend behavior without modifying existing code |
| **L**iskov Substitution | Subtypes must be substitutable for their base types |
| **I**nterface Segregation | Narrow, focused interfaces over broad general ones |
| **D**ependency Inversion | Depend on abstractions, not concrete implementations |

### The Screaming Architecture

An architecture should "scream" its intent. The structure of the code should reflect the business domain, not the technical framework. Organize by feature/use case, not by technical layer.

### Start with the Domain

1. Understand and model the domain first
2. Implement domain logic before persistence or UI
3. Build persistence and web layers around the domain
4. This ensures the domain drives the design, not the database

**Example: Domain-First Package Structure**

```java
// Domain layer - no dependencies on frameworks
package com.example.domain.account;

public class Account {
    private final AccountId id;
    private Money balance;

    public void withdraw(Money amount) {
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException();
        }
        this.balance = balance.minus(amount);
    }

    public void deposit(Money amount) {
        this.balance = balance.plus(amount);
    }
}
```

---

## 2. Code Organization Principles

### Package by Feature, Not by Layer

**Prefer**:
```
account/
  SendMoneyService.java
  AccountRepository.java
  Account.java
```

**Over**:
```
service/
  SendMoneyService.java
repository/
  AccountRepository.java
domain/
  Account.java
```

### Visibility Discipline

- Use package-private for implementation classes
- Public only for: interfaces, entry points, shared domain objects
- This prevents accidental coupling between components

### Immutability

- Make value objects immutable (all fields final)
- Validate in constructor - impossible to create invalid state
- Immutable objects are thread-safe and easier to reason about

### Constructor over Builder

For input models with validation:
- Use constructors directly (not builders) when adding/removing fields
- Compile errors guide updates across the codebase
- Builders hide validation until runtime

---

## 3. Domain Modeling Best Practices

### Entities vs Value Objects

| Entities | Value Objects |
|----------|--------------|
| Have identity (ID) | Defined by attributes |
| Mutable state | Immutable |
| Long-lived | Can be freely created/discarded |
| Examples: User, Order, Account | Examples: Money, Address, Email |

### Aggregate Design

- An aggregate is a cluster of associated objects treated as a unit
- One entity is the aggregate root - external references only to the root
- Transactions should not span multiple aggregates
- Enforce invariants within the aggregate boundary

### Domain Services

- Use when behavior doesn't naturally belong to an entity or value object
- Stateless operations involving multiple aggregates
- Named after domain concepts, not technical operations

### Validation Strategy

**Input Validation** (syntactic):
- Validates format, presence, type constraints
- No access to current model state needed
- Implement in input model constructors
- Examples: "amount must be positive", "email must be valid format"

**Business Rule Validation** (semantic):
- Validates against current model state
- Requires access to domain data
- Implement in domain entities or use case code
- Examples: "account must not be overdrawn", "user must be active"

---

## 4. Data Persistence Best Practices

### Repository Pattern

- Expose collection-like interface for domain objects
- Hide query and persistence details
- One repository per aggregate root
- Support specification-based queries

**Example: Repository Interface and Implementation**

```java
// Domain layer - interface
package com.example.domain.account;

public interface AccountRepository {
    Account findById(AccountId id);
    void save(Account account);
}

// Persistence layer - implementation
package com.example.persistence.account;

public class AccountRepositoryImpl implements AccountRepository {
    private final JpaAccountRepository jpaRepository;

    @Override
    public Account findById(AccountId id) {
        AccountJpaEntity entity = jpaRepository.findById(id.value())
            .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return AccountMapper.toDomain(entity);
    }

    @Override
    public void save(Account account) {
        AccountJpaEntity entity = AccountMapper.toJpa(account);
        jpaRepository.save(entity);
    }
}
```

### ORM Best Practices

- Keep persistence model separate from domain model when domain complexity grows
- Don't let ORM annotations drive domain design
- Be aware of lazy loading N+1 problems
- Use explicit transactions - don't rely on auto-commit

### Transaction Boundaries

- Transactions belong at the use case / application service level
- Should span all write operations within a use case
- Keep transactions as short as possible
- Don't open transactions for read-only operations unless needed

### Data Mapping

**When to map between domain and persistence models**:
- When ORM constraints conflict with domain model design
- When persistence model needs optimization (denormalization, etc.)
- When domain model uses rich types not supported by ORM

**When to use shared model**:
- Simple CRUD applications
- When ORM works well with domain model
- Rapid prototyping

---

## 5. API and Interface Design

### API Design Principles

- Design APIs around use cases, not CRUD operations
- Each endpoint maps to one use case
- Use domain language in endpoint names (e.g., `/transfer-money` not `/accounts/update`)

### Narrow Interfaces

- Split broad interfaces into focused ones
- One method per interface is often appropriate for ports
- Services should only depend on methods they actually use

### DTOs and Data Transfer

- Use DTOs for crossing process boundaries
- Keep DTOs separate from domain entities
- DTOs should be simple data containers (no behavior)
- Consider using record types or data classes

### Versioning

- Plan for API versioning from the start
- Use URL versioning (`/v1/`, `/v2/`) or header-based versioning
- Maintain backward compatibility when possible

---

## 6. Testing Best Practices

### Test Pyramid

| Layer | Ratio | Scope | Speed |
|-------|-------|-------|-------|
| Unit tests | Many (70%+) | Single class | Fast |
| Integration tests | Some (20%) | Multiple classes, real dependencies | Medium |
| System/E2E tests | Few (10%) | Full application | Slow |

### Unit Testing

- Test domain entities in isolation
- Test use cases with mocked ports
- Use given/when/then structure
- Verify outcomes, not all internal interactions
- Each test should verify one behavior

**Example: Domain Entity Unit Test**

```java
@Test
void cannotWithdrawMoreThanBalance() {
    // given
    Account account = new Account(
        new AccountId(1L),
        Money.of(100, Currency.USD)
    );

    // when/then
    assertThrows(InsufficientFundsException.class, () -> {
        account.withdraw(Money.of(150, Currency.USD));
    });
}

@Test
void depositIncreasesBalance() {
    // given
    Account account = new Account(
        new AccountId(1L),
        Money.of(100, Currency.USD)
    );

    // when
    account.deposit(Money.of(50, Currency.USD));

    // then
    assertEquals(Money.of(150, Currency.USD), account.getBalance());
}
```

**Example: Use Case Unit Test**

```java
@Test
void sendMoneySuccessfully() {
    // given
    Account sourceAccount = givenSourceAccountWithBalance(500);
    Account targetAccount = givenTargetAccountWithBalance(300);

    givenLoadAccountPort.loads(accountId(1L), sourceAccount);
    givenLoadAccountPort.loads(accountId(2L), targetAccount);

    // when
    boolean result = sendMoneyUseCase.sendMoney(
        new SendMoneyCommand(1L, 2L, 100)
    );

    // then
    assertTrue(result);
    then(accountLock).should().releaseAccount(any(AccountId.class));
}
```

### Integration Testing

- Test persistence adapters against real databases
- Test web adapters within the framework context
- Use Testcontainers for database dependencies
- Don't mock what you don't own (databases, external services)

### Test Quality Indicators

- Tests should enable refactoring, not prevent it
- If changing implementation breaks many tests, tests are too brittle
- Measure test quality by confidence to ship, not line coverage
- Line coverage goals are misleading - focus on behavior coverage

---

## 7. Concurrency and Transaction Management

### ACID Properties

- **Atomicity**: All operations in a transaction succeed or all fail
- **Consistency**: Database remains in valid state after transaction
- **Isolation**: Concurrent transactions don't interfere with each other
- **Durability**: Committed changes survive system failures

### Optimistic vs Pessimistic Locking

| Optimistic | Pessimistic |
|------------|-------------|
| Detect conflicts at commit | Prevent conflicts by locking |
| Version number/timestamp | Explicit lock acquisition |
| Better for read-heavy workloads | Better for write-heavy, high-conflict |
| No locks during edit | Locks held during edit |

### Offline Concurrency

- For long-running business transactions (user think-time)
- Use Optimistic Offline Lock for low-conflict scenarios
- Use Pessimistic Offline Lock for high-conflict scenarios
- Consider Coarse-Grained Lock for aggregate consistency

### Session State

**Client-side** (JWT, cookies):
- Scales horizontally
- Has size limits
- Security considerations

**Server-side** (Redis, in-memory):
- More secure
- Requires sticky sessions or shared store
- Memory overhead

**Database**:
- Most durable
- Survives restarts
- Adds database load

---

## 8. Distributed Systems Patterns

### Remote Facade

- Provide coarse-grained API over fine-grained domain objects
- Batch multiple operations into single network call
- Reduces chatty communication over the network

### Data Transfer Object (DTO)

- Flatten and package data for network transfer
- Serializable, no behavior
- Often assembled from multiple domain objects

### Session Facade

- Encapsulate complex interactions behind a simple interface
- Manages transaction and security for a use case
- Common in EJB/Spring service layers

### Gateway Pattern

- Encapsulate access to external systems
- Single point of integration
- Enables testing with stubs/mocks
- Isolates application from external API changes

---

## 9. Quality and Maintainability

### Broken Windows Theory in Code

- One shortcut attracts more shortcuts
- Start clean - initial quality sets the tone
- Document conscious shortcuts with Architecture Decision Records (ADRs)
- Regular refactoring prevents gradual degradation

### Code Smells to Watch For

- **Broad services**: Single class handling many use cases
- **Anemic domain model**: Entities with only getters/setters
- **Feature envy**: Method that uses another class's data more than its own
- **Shotgun surgery**: One change requires modifications in many classes
- **Divergent change**: One class changes for many different reasons

### Documentation

- Document architecture decisions (ADRs)
- Keep README up to date with setup/run instructions
- Document non-obvious business rules in code comments
- Maintain API documentation (OpenAPI/Swagger)

### Refactoring Guidelines

- Refactor continuously, not in big batches
- Ensure tests pass before and after refactoring
- One refactoring at a time
- Commit working code after each refactoring step

---

## 10. Anti-Patterns to Avoid

### Database-Driven Design

Don't design the database schema first and then build domain logic on top. The domain should drive the design, not the persistence mechanism.

### God Class / God Service

Avoid classes that know too much or do too much. Split broad services into narrow, use-case-specific services.

### Anemic Domain Model

Don't push all domain logic into services while leaving entities as pure data bags. Rich domain models encapsulate behavior and invariants.

### Transaction Script for Complex Domains

Don't use Transaction Script pattern when domain logic is complex and interrelated. The Domain Model pattern is more maintainable for complex domains.

### Leaky Abstractions

Don't let framework-specific code (annotations, HTTP details, ORM concerns) leak into the domain layer.

### Shared Models Between Use Cases

Each use case should have dedicated input and output models. Sharing models couples use cases together.

### Premature Abstraction

Don't add layers of abstraction before they're needed. Start simple and add architecture when complexity justifies it.

### Circular Dependencies

Never allow circular dependencies between packages, modules, or services. They make the system impossible to understand and test in isolation.

### Magic Numbers and Strings

Replace hardcoded values with named constants or configuration. Business rules should be explicit and configurable.

### Ignoring the Dependency Rule

Don't let domain code depend on frameworks, databases, or UI code. Dependencies must point inward toward the domain.
