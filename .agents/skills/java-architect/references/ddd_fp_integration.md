# Domain-Driven Design (DDD) & Functional Programming (FP) Integration

## Table of Contents

1. [DDD Strategic Patterns](#ddd-strategic-patterns)
2. [DDD Tactical Patterns](#ddd-tactical-patterns)
3. [Functional Programming Patterns for Architecture](#functional-programming-patterns-for-architecture)
4. [Integrating FP with Clean Architecture](#integrating-fp-with-clean-architecture)
5. [Anti-Patterns and Warnings](#anti-patterns-and-warnings)

---

## DDD Strategic Patterns

### Bounded Context
- **Definition**: A defined boundary within which a domain model exists and is consistent. Outside the boundary, different models may exist for the same concepts.
- **Responsibility**: Defines where a particular domain model applies; each context has its own ubiquitous language.
- **Identification contexts**: Organizational boundaries; different domain expertise; varying change frequency; different data ownership.

### Context Mapping

**Patterns for relationships between bounded contexts**:

| Pattern | Description | When to Use |
|---------|------------|-------------|
| **Partnership** | Two contexts mutually depend on each other | Teams are co-located; high collaboration |
| **Shared Kernel** | Overlapping model shared between contexts | Highly coupled subdomains; can't separate yet |
| **Customer-Supplier** | Downstream context influences upstream | Upstream is responsive to downstream needs |
| **Conformist** | Downstream conforms to upstream model | Upstream is external; no influence possible |
| **Anti-Corruption Layer (ACL)** | Translation layer protects downstream from upstream model | Upstream model is messy or incompatible |
| **Open Host Service** | Published language for multiple consumers | Multiple downstream contexts |
| **Published Language** | Well-documented shared interchange model | Inter-context communication |
| **Separate Ways** | No connection between contexts | No shared business value |
| **Big Ball of Mud** | Legacy system with unclear boundaries | Acknowledged legacy; plan extraction |

### Subdomain Classification

| Type | Characteristics | Strategic Priority |
|------|---------------|-------------------|
| **Core Domain** | Differentiating business logic; competitive advantage | Highest investment; best team |
| **Supporting Subdomain** | Required but not differentiating; custom-built | Moderate investment; consider buying |
| **Generic Subdomain** | Common capabilities available off-the-shelf | Lowest investment; buy or outsource |

---

## DDD Tactical Patterns

### Entities
- **Definition**: Objects with a distinct identity that persists across state changes.
- **Rules**: Identity is defined at creation and never changes; equality based on identity; encapsulate behavior and invariants.
- **Implementation**: Constructor validation; factory methods for complex creation; methods that express business operations.

### Value Objects
- **Definition**: Immutable objects distinguished only by their attribute values, not by identity.
- **Rules**: Immutable after creation; equality based on all attributes; can be freely shared and copied; validate in constructor.
- **Examples**: Money, Address, DateRange, Email, Quantity.
- **Benefits**: Composability; implicit thread-safety; fail-fast validation; rich behavior without identity overhead.

### Aggregates
- **Definition**: A cluster of associated objects treated as a single unit for data changes. Each aggregate has a root entity and a boundary.
- **Rules**:
  - Root Entity has global identity; boundary members have local identity
  - External references can only point to aggregate root
  - Transactions should not cross aggregate boundaries
  - Delete removes everything within the boundary
  - Invariants must be consistent within the boundary at all times
- **Design guidance**: Keep aggregates small; large aggregates cause concurrency issues; one transaction per aggregate.

### Domain Services
- **Definition**: Stateless operations that don't belong to any Entity or Value Object.
- **When to use**: Business logic involves multiple aggregates; operation is a significant domain concept; logic doesn't fit naturally in an entity.
- **Rules**: Stateless; coordinate between aggregates; express domain concepts in method names.

### Application Services
- **Definition**: Orchestrates use cases by coordinating domain objects and infrastructure.
- **Rules**: Thin; no business logic; transaction boundaries; load aggregates, execute operations, save changes.

### Repositories
- **Definition**: Collection-like interface for accessing aggregates, mediating between domain and data mapping.
- **Rules**: One repository per aggregate root; interface in domain, implementation in infrastructure; encapsulate query logic.

### Domain Events
- **Definition**: Events that represent something that happened in the domain, of interest to other parts of the system.
- **Rules**: Past tense naming (OrderPlaced, PaymentReceived); immutable; contain event data; enable loose coupling between aggregates.
- **Usage**: Cross-aggregate communication; eventual consistency; event sourcing; integration between bounded contexts.

### Factories
- **Definition**: Encapsulate complex object creation logic.
- **When to use**: Complex aggregate construction; multiple creation variants; invariant enforcement at creation.

### Modules (Packages)
- **Definition**: Organize domain objects into cohesive units.
- **Rules**: Named by ubiquitous language; low coupling between modules; high cohesion within; reflect business concepts not technical layers.

---

## Functional Programming Patterns for Architecture

### Immutability as Default
- All data structures are immutable unless there's a compelling reason
- Changes produce new values rather than modifying existing ones
- **Benefits**: Thread safety; predictable behavior; easy to reason about; natural fit for Value Objects

### Pure Functions for Domain Logic
- **Definition**: Functions where output depends only on input; no side effects.
- **Application**: Business rules, calculations, validations, transformations
- **Benefits**: Testable without mocks; composable; cacheable; parallelizable; referentially transparent

### Railway-Oriented Programming
- **Pattern**: Chain operations where each can succeed or fail; failures short-circuit the chain.
- **Implementation**: Result/Either type with map, bind, and mapError operations.
- **Application**: Validation pipelines; use case execution chains; error handling in domain logic.

```
Input -> Validate -> Transform -> Save -> Notify
           |           |          |        |
           v           v          v        v
         Result     Result     Result   Result
           |           |          |        |
           +-----------+----------+--------+
                       |
                  Final Result
```

### Effect System for Side Effects
- **Pattern**: Separate pure computation from effectful operations using types.
- **Application**: Domain logic returns descriptions of effects; infrastructure interprets and executes them.
- **Benefits**: Testability; composability; clear separation of concerns.

### Monoid and Semigroup Patterns
- **Definition**: Types that can be combined associatively with an identity element.
- **Application**: Aggregating values (Money, Quantity); building configurations; composing validators.

### Lens and Optics
- **Pattern**: Composable getters and setters for nested immutable data.
- **Application**: Updating deeply nested domain objects; state transformations.

### Pattern Matching for Domain Logic
- **Pattern**: Express business rules as exhaustive pattern matches on domain types.
- **Benefits**: Compiler ensures all cases handled; expressive; self-documenting.

### Functional Error Handling
- **Pattern**: Use Result/Either types instead of exceptions for expected failures.
- **Rules**: Exceptions for unexpected/exceptional cases only; domain errors as values; explicit error types.

```
Result<T, E> = Success(T) | Failure(E)

Use case returns Result<Output, DomainError>
DomainError = ValidationError | BusinessRuleViolation | NotFoundError
```

---

## Integrating FP with Clean Architecture

### Functional Core, Imperative Shell

**Pattern**:
```
+---------------------+     +----------------------+
|   Imperative Shell   |     |    Functional Core   |
|  (Infrastructure)    | --> |    (Domain)          |
|  - Load data         |     |  - Pure business     |
|  - Call domain       |     |    logic             |
|  - Save results      | <-- |  - Immutable data    |
|  - Handle effects    |     |  - No side effects   |
+---------------------+     +----------------------+
```

**Rules**:
- Domain is entirely pure and functional
- Infrastructure handles all side effects (I/O, state, external calls)
- Data flows into the core, is transformed, flows out
- The "shell" interprets functional results into effects

### Domain Model as Algebraic Data Types

```haskell
-- Example: Banking domain

-- Value Objects (product types + validation)
data Money = Money { amount :: BigDecimal, currency :: Currency }
  deriving (Eq, Show)

data AccountId = AccountId UUID
  deriving (Eq, Show)

-- Entity (identity + state)
data Account = Account
  { accountId :: AccountId
  , balance :: Money
  , activities :: [Activity]
  , withdrawalLimit :: Money
  } deriving (Show)

-- Aggregate Root with behavior
withdraw :: Money -> Account -> Result WithdrawalError (Account, Activity)
withdraw amount account
  | amount <= Money 0 = Failure NegativeAmount
  | amount > withdrawalLimit account = Failure LimitExceeded
  | amount > balance account = Failure InsufficientFunds
  | otherwise = Success (updatedAccount, activity)
  where
    updatedAccount = account { balance = balance account - amount
                             , activities = activity : activities account }
    activity = Activity (accountId account) Withdrawal amount timestamp

-- Domain Events
data DomainEvent = MoneySent SendMoneyEvent
                 | MoneyReceived ReceiveMoneyEvent
                 | LimitReached AccountId
                 deriving (Show)
```

### FP in Java/C# (Object-Functional Style)

**Patterns**:
1. **Immutable classes**: `final` fields; no setters; builder pattern or constructor
2. **Value-based `equals`/`hashCode`**: For Value Objects; identity-based for Entities
3. **Result types**: Custom `Result<T, E>` instead of exceptions
4. **Optional/Maybe**: Explicit null handling
5. **Stream/Sequence processing**: Composable data transformations
6. **Function composition**: Small functions composed into pipelines

**Java Example**:
```java
// Value Object
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    private Money(BigDecimal amount, Currency currency) {
        if (amount.signum() < 0) throw new IllegalArgumentException("Negative amount");
        this.amount = amount;
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public Money add(Money other) {
        assertSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    // No setters; all operations return new instances
}

// Railway-oriented validation
public Result<SendMoneyCommand, ValidationError> validate(SendMoneyRequest request) {
    return validateSourceAccountId(request.getSourceAccountId())
        .flatMap(sourceId -> validateTargetAccountId(request.getTargetAccountId())
            .flatMap(targetId -> validateAmount(request.getAmount())
                .map(amount -> new SendMoneyCommand(sourceId, targetId, amount))));
}
```

### Combining DDD Aggregates with FP

**Pattern**:
- Aggregate root as a function: `State -> Command -> Event[] * State`
- State transitions are pure functions
- Events are the only output; persistence is a side effect handled externally
- Event sourcing naturally fits this model

```
Command + Current State -> Validation -> Business Logic -> (New State, Events)
```

### Functional Repositories

**Pattern**:
- Repository interface returns `Option<T>` or `Result<T, NotFound>`
- Queries are pure descriptions; execution is deferred
- Composition of query operations before execution

```java
// Functional repository interface
public interface AccountRepository {
    Result<Account, NotFoundError> findById(AccountId id);
    List<Account> findByOwner(CustomerId owner);
    Account save(Account account);
}
```

---

## Anti-Patterns and Warnings

### The Anemic Domain Model
- **Symptom**: Entities have only getters/setters; all logic in service classes
- **Root cause**: ORM-driven design; misunderstanding of DDD; CRUD mindset
- **Fix**: Push behavior into entities; use constructors for validation; apply Tell, Don't Ask

### Golden Hammer
- **Symptom**: Every problem solved with the same pattern (e.g., everything is a microservice; every app needs event sourcing)
- **Fix**: Evaluate each context independently; use the simplest pattern that works; refer to decision frameworks

### Over-Engineering
- **Symptom**: Complex event sourcing for simple CRUD; microservices for small team; DDD everywhere
- **Fix**: Start simple; evolve based on actual needs; YAGNI principle

### Leaky Abstractions
- **Symptom**: Domain model references ORM annotations; HTTP status codes in domain; database IDs exposed as domain identity
- **Fix**: Separate persistence model from domain model; use anti-corruption layers; keep infrastructure concerns at the boundary

### Big Ball of Mud in Microservices
- **Symptom**: Distributed monolith with synchronous chains; shared database; tight coupling
- **Fix**: Define clear bounded contexts; async communication where possible; database per service; eventual consistency

### Premature Optimization
- **Symptom**: Complex caching before measuring; denormalized schemas before need; CQRS for simple reads
- **Fix**: Measure first; optimize bottlenecks; keep it simple until data proves otherwise

### Transaction Script Creep
- **Symptom**: Transaction Script used for complex domains; duplicated business logic across procedures
- **Fix**: Graduate to Domain Model when complexity exceeds threshold; refactor incrementally

### Repository Leakage
- **Symptom**: Repositories expose query methods for every UI screen; business logic in queries
- **Fix**: Repositories for aggregate persistence only; use CQRS for complex reads; separate query model

### Ubiquitous Language Violation
- **Symptom**: Technical terms in domain code (`HibernateAccountDAO`, `UserDTO`); business terms in infrastructure
- **Fix**: Model code speaks business language; infrastructure speaks technical language; ACL translates
