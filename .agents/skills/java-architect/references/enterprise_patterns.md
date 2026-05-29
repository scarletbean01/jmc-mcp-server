# Patterns of Enterprise Application Architecture (PEAA) Catalog

Reference source: Fowler, Martin. *Patterns of Enterprise Application Architecture*. Addison-Wesley, 2003.

## Table of Contents

1. [Domain Logic Patterns](#domain-logic-patterns)
2. [Data Source Architectural Patterns](#data-source-architectural-patterns)
3. [Object-Relational Behavioral Patterns](#object-relational-behavioral-patterns)
4. [Object-Relational Structural Patterns](#object-relational-structural-patterns)
5. [Object-Relational Metadata Mapping Patterns](#object-relational-metadata-mapping-patterns)
6. [Web Presentation Patterns](#web-presentation-patterns)
7. [Distribution Patterns](#distribution-patterns)
8. [Offline Concurrency Patterns](#offline-concurrency-patterns)
9. [Session State Patterns](#session-state-patterns)
10. [Base Patterns](#base-patterns)

---

## Domain Logic Patterns

These patterns organize the business logic of an enterprise application.

### Transaction Script
- **Intent**: Organize business logic by procedures where each procedure handles a single request from the presentation.
- **How It Works**: Each business transaction is implemented as a procedure that contains all business rules and directly accesses the database (typically via SQL or a thin data access layer).
- **When to Use**: Simple domain logic; small applications; rapid prototyping; teams with limited OO experience; when business rules have few interactions.
- **Trade-offs**: (+) Simple to understand, natural procedural approach, easy to transaction-boundary. (-) Duplication as complexity grows, poor factoring of business rules, does not scale with complexity.
- **Related Patterns**: Table Data Gateway, Row Data Gateway

### Domain Model
- **Intent**: An object model of the domain that incorporates both behavior and data.
- **How It Works**: Business logic is organized as a network of interconnected objects (Entities, Value Objects, Aggregates) where each object encapsulates both state and behavior. Rich domain model contains complex business rules, validations, and computations within the objects.
- **When to Use**: Complex and evolving business logic; business rules involve interactions between multiple domain concepts; need for maintainable long-term architecture; teams with strong OO/DDD skills.
- **Trade-offs**: (+) Highly maintainable for complex logic, natural language mapping to business, testable in isolation. (-) Steep learning curve, requires experienced team, needs infrastructure for persistence (Data Mapper).
- **Related Patterns**: Data Mapper, Unit of Work, Repository, Aggregate, Lazy Load

### Table Module
- **Intent**: A single instance that handles the business logic for all rows in a database table or view.
- **How It Works**: One class per table, with methods for table-oriented operations. Uses Record Set for data access. Sits between Transaction Script and Domain Model in complexity.
- **When to Use**: When a strong Record Set framework exists (e.g., .NET DataSets); moderately complex logic; mostly table-oriented operations.
- **Trade-offs**: (+) Good for table-centric views, works well with existing UI tools. (-) Cannot easily organize logic around objects with complex relationships.
- **Related Patterns**: Table Data Gateway, Record Set

### Service Layer
- **Intent**: Defines the application's boundary and its set of available operations from the perspective of the interfacing client layers.
- **How It Works**: A layer of services that coordinates between the domain layer and external layers. Implements use cases by orchestrating domain objects and infrastructure concerns. Can be implemented as facade or operation script pattern.
- **When to Use**: Almost always recommended when using Domain Model; need clear API boundary for the domain; multiple clients (web, API, batch) access the same domain.
- **Trade-offs**: (+) Clean API, encapsulates domain access, enables multiple client types, transaction coordination. (-) Additional layer of abstraction, can become "anemic" if not careful.
- **Related Patterns**: Domain Model, Unit of Work, Repository, Transaction Script

---

## Data Source Architectural Patterns

These patterns determine how the application accesses and manipulates data.

### Table Data Gateway
- **Intent**: An object that acts as a Gateway to a database table. One instance handles all the rows in the table.
- **When to Use**: Transaction Script; Table Module; simple CRUD applications; when you need record set-based access.

### Row Data Gateway
- **Intent**: An object that acts as a Gateway to a single record in a data source. There is one instance per row.
- **When to Use**: Transaction Script; when domain objects map closely to table rows; simpler than Active Record when you want to separate data access from domain logic.

### Active Record
- **Intent**: An object that wraps a row in a database table, encapsulates the database access, and adds domain logic on that data.
- **When to Use**: Simple domain logic; when domain class structure closely matches database schema; CRUD-heavy applications; rapid development scenarios.
- **Trade-offs**: (+) Simple, natural for CRUD, minimal infrastructure. (-) Tight coupling to DB schema, doesn't scale with complex domain logic, violates SRP.

### Data Mapper
- **Intent**: A layer of Mappers that moves data between objects and a database while keeping them independent of each other and the mapper itself.
- **When to Use**: Domain Model with complex mapping to database; need to change domain model and database independently; sophisticated ORM scenarios.
- **Trade-offs**: (+) Full decoupling, supports complex mappings, enables rich domain model. (-) Complex infrastructure, steep learning curve, potential performance overhead.

---

## Object-Relational Behavioral Patterns

These patterns handle the behavior of object persistence and database interaction.

### Unit of Work
- **Intent**: Maintains a list of objects affected by a business transaction and coordinates the writing out of changes and the resolution of concurrency problems.
- **When to Use**: Almost always with Domain Model; any scenario where multiple objects change in a single transaction; need for optimistic locking.
- **Patterns**: Caller Registration, Object Registration, Unit of Work Controller

### Identity Map
- **Intent**: Ensures that each object gets loaded only once by keeping every loaded object in a map. Looks up objects using the map when referring to them.
- **When to Use**: Any scenario with object loading; prevents inconsistent object states; essential for Unit of Work.

### Lazy Load
- **Intent**: An object that does not contain all of the data you need but knows how to get it.
- **Variants**: Lazy Initialization, Virtual Proxy, Value Holder, Ghost
- **When to Use**: Performance optimization; large object graphs; partial object access patterns.

---

## Object-Relational Structural Patterns

These patterns handle the structural mapping between objects and relational databases.

### Identity Field
- **Intent**: Saves a database ID field in an object to maintain identity between an in-memory object and a database row.
- **Patterns**: Integral Key, Key Table, Compound Key, GUID

### Foreign Key Mapping
- **Intent**: Maps an association between objects to a foreign key reference between tables.

### Association Table Mapping
- **Intent**: Saves an association as a table with foreign keys to the tables that are linked by the association.

### Dependent Mapping
- **Intent**: Has one class perform the database mapping for a child class.

### Embedded Value
- **Intent**: Maps an object into several fields of another object's table.

### Serialized LOB
- **Intent**: Saves a graph of objects by serializing them into a single large object (LOB), which it stores in a database field.

### Single Table Inheritance
- **Intent**: Represents an inheritance hierarchy of classes as a single table that has columns for all the fields of the various classes.

### Class Table Inheritance
- **Intent**: Represents an inheritance hierarchy with each class in a separate table.

### Concrete Table Inheritance
- **Intent**: Represents an inheritance hierarchy with one table per concrete class in the hierarchy.

### Inheritance Mappers
- **Intent**: A structure to organize database mappers that handle inheritance hierarchies.

---

## Object-Relational Metadata Mapping Patterns

These patterns use metadata to drive the object-relational mapping.

### Metadata Mapping
- **Intent**: Holds details of object-relational mapping in metadata.

### Query Object
- **Intent**: An object that represents a database query.

### Repository
- **Intent**: Mediates between the domain and data mapping layers using a collection-like interface for accessing domain objects.
- **When to Use**: With Domain Model; provides clean API for data access; enables swapping persistence strategies; supports testability.

---

## Web Presentation Patterns

These patterns handle the presentation layer in web applications.

### Model View Controller (MVC)
- **Intent**: Splits user interface interaction into three distinct roles.

### Page Controller
- **Intent**: An object that handles a request for a specific page or action on a Web site.

### Front Controller
- **Intent**: A controller that handles all requests for a Web site.

### Template View
- **Intent**: Renders information into HTML by embedding markers in an HTML page.

### Transform View
- **Intent**: A view that processes domain data element by element and transforms it into HTML.

### Two Step View
- **Intent**: Turns domain data into HTML in two steps: first by forming some kind of logical page, then rendering the logical page into HTML.

### Application Controller
- **Intent**: A centralized point for handling screen navigation and the flow of an application.

---

## Distribution Patterns

These patterns address distributing an application across multiple processes.

### Remote Facade
- **Intent**: Provides a coarse-grained facade on fine-grained objects to improve efficiency over a network.

### Data Transfer Object (DTO)
- **Intent**: An object that carries data between processes in order to reduce the number of method calls.

---

## Offline Concurrency Patterns

These patterns manage concurrency in business transactions that span multiple system transactions.

### Optimistic Offline Lock
- **Intent**: Prevents conflicts between concurrent business transactions by detecting a conflict and rolling back the transaction.

### Pessimistic Offline Lock
- **Intent**: Prevents conflicts between concurrent business transactions by allowing only one business transaction at a time to access data.

### Coarse-Grained Lock
- **Intent**: Locks a set of related objects with a single lock.

### Implicit Lock
- **Intent**: Allows framework code to acquire offline locks based on the executing business transaction context.

---

## Session State Patterns

These patterns manage session state in stateless environments.

### Client Session State
- **Intent**: Stores session state on the client.

### Server Session State
- **Intent**: Keeps the session state on a server system in a serialized form.

### Database Session State
- **Intent**: Stores session data as committed data in the database.

---

## Base Patterns

These are lower-level patterns used by other patterns in the catalog.

### Gateway
- **Intent**: An object that encapsulates access to an external system or resource.

### Mapper
- **Intent**: An object that sets up a communication between two independent objects.

### Layer Supertype
- **Intent**: A type that acts as the supertype for all types in its layer.

### Separated Interface
- **Intent**: Defines an interface in a separate package from its implementation.

### Registry
- **Intent**: A well-known object that other objects can use to find common objects and services.

### Value Object
- **Intent**: A small simple object, like money or a date range, whose equality isn't based on identity.

### Money
- **Intent**: Represents a monetary value.

### Plugin
- **Intent**: Links classes during configuration rather than compilation.

### Service Stub
- **Intent**: Removes dependence upon problematic services during testing.

### Record Set
- **Intent**: An in-memory representation of tabular data.
