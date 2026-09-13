# Cost Center Management

A Java software engineering project for managing faculty cost centers, purchase requests, approval workflows, user roles, persistence, statistics, and automated unit tests.

The project models the **cost center management of a Faculty of Information Technology** and was developed as a university software engineering project. It follows a layered structure with separate packages for domain logic, terminal-based user interaction, and persistence.

## Features

- Manage institutes and professors
- Assign professors as institute heads or dean
- Role-based login and menus
- Create purchase requests
- Approve or reject purchase requests according to role responsibilities
- Automatically approve faculty purchases created by the dean when the request can be executed
- Deposit money into institute cost centers
- Deposit money at faculty level and distribute it between faculty and institutes
- Enforce institute overdraft and faculty-wide debt rules
- Track transactions and expenses
- Show statistics for institute balances, institute expenses, and professor expenses
- Persist the faculty state using Java object serialization
- Automatically save changes made through the terminal UI
- Load the previously saved state when the application starts
- Validate core business logic with JUnit tests

## User Roles

### Administrator

The administrator manages the organizational structure and user roles. The available functions include:

- Create institutes
- Create professors
- Assign a professor as dean
- Assign a professor as institute head
- Display an overview of the faculty structure

### Professor

A professor can:

- Create purchase requests
- Deposit money into the assigned institute cost center
- View personal purchase requests and their status

### Institute Head

An institute head has the professor functions and can additionally:

- View open purchase requests for the institute
- Approve or reject purchase requests from professors
- Authenticate approvals with a password

### Dean

The dean can:

- View personal purchase requests
- Review open purchase requests from institute heads
- Approve or reject institute-head requests
- Create faculty purchase requests
- Deposit money at faculty level
- View financial statistics

## Business Rules

The domain layer contains the main business rules of the application.

### Institute cost centers

Each institute owns its own cost center. An institute cost center may be overdrawn down to **-3,000 EUR**.

### Faculty-wide debt limit

The sum of all negative institute cost-center balances is monitored by the faculty. The total debt of all institutes may not exceed **10,000 EUR**.

### Faculty deposits

When the dean performs a faculty deposit:

- 50% is deposited into the faculty cost center
- 50% is distributed equally among all institutes

A faculty deposit requires at least one institute.

### Purchase approval workflow

The project implements a role-based approval workflow:

```text
Professor
    -> Purchase Request
    -> Institute Head
    -> Approve / Reject

Institute Head
    -> Purchase Request
    -> Dean
    -> Approve / Reject

Dean
    -> Faculty Purchase Request
    -> Automatically approved when the purchase can be executed
```

Approved institute purchases are debited from the corresponding institute cost center. Faculty purchases created by the dean are debited from the faculty cost center.

## Architecture

The source code is separated into three main layers.

```text
src/
├── costcentermanagement/
│   └── CostCenterManagement.java
├── domain/
│   ├── Admin.java
│   ├── Approver.java
│   ├── Authenticatable.java
│   ├── CostCenter.java
│   ├── CostCenterBalanceComparator.java
│   ├── Dean.java
│   ├── Faculty.java
│   ├── FacultyDAO.java
│   ├── Institute.java
│   ├── InstituteExpensesComparator.java
│   ├── InstituteHead.java
│   ├── Professor.java
│   ├── ProfessorExpensesComparator.java
│   ├── PurchaseRequest.java
│   ├── RequestStatus.java
│   ├── StatisticsCapable.java
│   ├── StatisticsService.java
│   ├── Transaction.java
│   ├── TransactionType.java
│   └── User.java
├── persistence/
│   └── SerializedFacultyDAO.java
└── ui/
    ├── AdminUI.java
    ├── DeanUI.java
    ├── InstituteHeadUI.java
    ├── LoginUI.java
    ├── ProfessorUI.java
    └── TerminalUI.java
```

### Domain Layer

The `domain` package contains the business model and business rules. It includes the user hierarchy, faculty and institute structure, cost centers, purchase requests, approval logic, transactions, statistics, interfaces, enums, and DAO abstraction.

### UI Layer

The `ui` package implements a terminal-based user interface. Each role has its own menu and available actions. `TerminalUI` provides shared functionality such as input handling, validation, display handling, and automatic persistence after successful changes.

### Persistence Layer

The `persistence` package contains `SerializedFacultyDAO`, which implements the `FacultyDAO` interface. The complete faculty state is stored and loaded using Java object serialization.

## Application Flow

At startup, the application tries to load the saved faculty state from `fakultaet.ser`.

If no saved state exists, a new faculty named `Informationstechnik` is created. The application then rebuilds the user list from the loaded domain objects and starts the login process.

After a successful login, the application opens the menu corresponding to the user's role:

```text
Admin           -> AdminUI
Dean            -> DeanUI
InstituteHead   -> InstituteHeadUI
Professor       -> ProfessorUI
```

## Statistics

The dean can display three statistics:

- Institute cost-center balances in descending order
- Institute expenses in descending order
- Professor expenses in descending order

The sorting is implemented using dedicated comparator classes and `StatisticsService`.

## Persistence

Persistence is implemented with Java serialization.

```text
FacultyDAO
    ↑
SerializedFacultyDAO
```

`SerializedFacultyDAO` provides:

- `load(String filePath)`
- `save(Faculty faculty, String filePath)`

The terminal UI calls the persistence layer automatically after successful state-changing operations.

## Testing

The project contains **10 JUnit test classes with 48 individual test methods**.

```text
test/domain/
├── AdminTest.java
├── ApprovalTest.java
├── CostCenterTest.java
├── DeanTest.java
├── FacultyTest.java
├── InstituteTest.java
├── ProfessorTest.java
├── StatisticsServiceTest.java
├── TransactionTest.java
└── UserTest.java
```

The tests cover areas such as:

- Institute creation
- Duplicate institute prevention
- Professor creation and institute assignment
- Dean and institute-head creation
- Personnel-number handling
- Login and password changes
- Purchase-request creation
- Approval and rejection workflows
- Password validation during approval
- Prevention of self-approval
- Automatic approval of dean faculty purchases
- Cost-center deposits and debits
- Institute overdraft limits
- Faculty-wide debt calculations
- Faculty deposit distribution
- Expense calculations
- Statistics sorting
- Transaction data

## Technologies and Concepts

- Java
- Object-Oriented Programming
- Object-Oriented Analysis and Design
- Inheritance and polymorphism
- Interfaces and enums
- Layered architecture
- DAO pattern
- Java serialization
- Collections
- Comparators and sorting
- Exception handling
- Terminal-based user interface
- JUnit 5
- Unit testing
- Git and version control

## Project Documentation

The repository also contains project documentation in the `docu` directory. The project was developed from requirements and object-oriented analysis/design before implementation.

The documentation includes the software-engineering artifacts created during the project, including requirements and design documentation.

## Purpose of the Project

The goal of this project was not only to implement a working Java application, but to apply a complete software engineering workflow:

```text
Stakeholder Requirements
        ↓
Software Requirements
        ↓
Object-Oriented Analysis
        ↓
Object-Oriented Design
        ↓
Implementation
        ↓
Unit Testing
        ↓
Version Control
```

This repository therefore demonstrates both **Java development skills** and the application of **software engineering methods** to a complete project.
