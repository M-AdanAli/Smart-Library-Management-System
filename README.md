# 📚 Smart Library Management System

> A **console-based Smart Library Management System** built in **pure Java**, demonstrating clean architecture, OOP principles, and production-ready design patterns.

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![Jackson](https://img.shields.io/badge/Jackson-2.17.0-blue.svg)](https://github.com/FasterXML/jackson)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 🎯 Overview

<iframe src="https://www.linkedin.com/embed/feed/update/urn:li:ugcPost:7424722977025585153?compact=1" height="399" width="504" frameborder="0" allowfullscreen="" title="Embedded post"></iframe>

This project showcases a **production-like architectural exercise** that evolved from a typical "Core Java LMS" into a sophisticated system featuring:

- **Layered architecture** with clear separation of concerns
- **JSON-based persistence** using Jackson (no database required)
- **Polymorphic domain model** with proper OOP and custom exceptions
- **Realistic borrowing lifecycle** with overdue detection and automated fine calculation

---

## ✨ Key Features

### 🏗️ Layered Architecture
```
Console UI (CUI)
    ↓
LibraryService (Facade)
    ↓
Services Layer (UserService, BookService, BorrowingService)
    ↓
Repository Layer (JSON-backed persistence)
    ↓
Domain Model (Entities & DTOs)
```

- **Domain Layer**: `User`, `Student`, `Librarian`, `Book`, `BorrowingRecord`
- **Repository Layer**: JSON file-based storage with full CRUD operations
- **Service Layer**: Business logic encapsulation
- **Facade Layer**: `LibraryService` orchestrates all operations
- **UI Layer**: Console-based interface with robust input handling

### 💾 Persistent Storage

- **Jackson ObjectMapper** with `JavaTimeModule` for `LocalDate` serialization
- **Field-level visibility** configuration (no public getters/setters needed)
- **Automatic object graph reconstruction** on startup
- Custom `JsonStorageUtil` for reliable data persistence

### 👥 Polymorphic User System

- Abstract `User` base class with concrete implementations:
    - **Student/Borrower**: Borrow privileges, fine tracking
    - **Librarian**: Administrative operations
- Jackson `@JsonTypeInfo` and `@JsonSubTypes` for type preservation
- Seamless deserialization maintaining concrete types

### 📖 Complete Borrowing Lifecycle

**BorrowingRecord Status Management:**
- `ACTIVE` - Currently borrowed
- `RETURNED` - Returned on time or late
- `OVERDUE` - Past due date, not yet returned

**Automated Fine Calculation:**
- Triggers only when status is `OVERDUE`
- Calculates based on days between `dueDate` and `returnDate` (or current date)
- Fine rate: **50 units per day**

### 🔍 Advanced Book Management

**Search Capabilities:**
- By title, author, genre, or ALL attributes
- Uses `SearchAttribute` enum for type-safe queries

**Inventory Management:**
- Add/update books with validation
- Quantity tracking and availability checks
- ISBN uniqueness enforcement

---

## ⚙️ Technology Stack

| Component | Technology |
|-----------|------------|
| Language | Java 8+ |
| Persistence | Jackson (JSON) |
| Serialization | jackson-databind 2.17.0 |
| Date/Time | jackson-datatype-jsr310 2.17.0 |
| UI | Console/CLI |
| Build | Maven/Gradle compatible |

### Maven Dependencies
```xml
<dependencies>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.17.0</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
        <version>2.17.0</version>
    </dependency>
</dependencies>
```

---

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven or Gradle (optional)
- IDE (IntelliJ IDEA, Eclipse, VS Code) or text editor

### Installation

1. **Clone the repository**
```bash
   git clone https://github.com/M-AdanAli/Smart-Library-Management-System.git
   cd Smart-Library-Management-System
```

2. **Configure your IDE**
    - Import as Java project
    - Ensure JDK 8+ is configured
    - Add Jackson libraries to classpath (via Maven/Gradle or manual JAR inclusion)

3. **Run the application**

   **Via IDE:**
    - Locate the `Main` class
    - Run as Java Application

   **Via Command Line:**
```bash
   javac -cp libs/* src/.../Main.java
   java -cp libs/*:src ...Main
```

---

## 🔐 Default Credentials

> ⚠️ **For demo/testing purposes only** - Change in production!

### Default Librarian
```
Email:    librarian@library.com
Password: Password@123
Role:     LIBRARIAN
```

### System Defaults

| Setting | Value             |
|---------|-------------------|
| Borrowing Duration | 3 days (Students) |
| Fine per Overdue Day | 50 units          |
| Date Format | yyyy-MM-dd        |

---

## 📋 Business Rules

### Borrowing Eligibility

A borrower can borrow a book **only if:**
- ✅ No pending fines (or within allowed limits)
- ✅ Book has available quantity > 0

### Borrowing Process

1. Generate unique record ID (timestamp + UUID)
2. Validate borrower eligibility
3. Check book availability
4. Decrease book quantity by 1
5. Create `BorrowingRecord` with status `ACTIVE`
6. Persist all changes to JSON

### Return Process

1. Locate active record for (borrower, book)
2. Set `returnDate` to current date
3. Update status (`RETURNED` or `OVERDUE`)
4. Calculate fine if overdue
5. Increase book quantity by 1
6. Persist updates

### Fine Calculation
```java
if (status == OVERDUE) {
    long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
    fine = overdueDays * 50;
}
```

---

## 💻 Console UI Guide

### ConsoleUtil Helper Methods

| Method | Purpose |
|--------|---------|
| `printLibraryHeader()` | Display app header |
| `printWelcomeUser(String name)` | Personalized greeting |
| `inputString(String prompt)` | Get string input |
| `inputDate(String prompt)` | Get date (yyyy-MM-dd format) |
| `inputInteger(String prompt)` | Get integer with validation |
| `clearConsole()` | Simulate screen clear |
| `delay(long duration)` | Add pause for UX |

### UI Best Practices

> ⚠️ **Console Zoom Warning**: Tables and formatted outputs are optimized for specific console dimensions. If layout appears misaligned:
> - Zoom out your console window
> - Increase terminal width
> - Reduce font size

---

## ⚠️ Important Limitations

### Security
- ❌ Passwords stored in **plain text** in JSON
- ❌ No encryption or hashing
- ❌ Not suitable for production without security enhancements

### Concurrency
- ❌ **Single-user** design only
- ❌ No file locking or concurrent access handling
- ❌ No transaction support

---

## 🎯 Project Goals

This project serves as an **educational and portfolio piece** demonstrating:

✅ Clean separation of concerns in Java  
✅ JSON persistence with Jackson and DTOs  
✅ Polymorphism and custom exception handling  
✅ Realistic domain modeling  
✅ Console UI with robust input validation

---

## 🔮 Future Enhancements

### Planned Improvements

- [ ] **Database Integration**: PostgreSQL or MySQL
- [ ] **JavaFX Desktop App**: Native GUI
- [ ] **Password Hashing**: BCrypt or similar
- [ ] **Unit Tests**: JUnit 5 coverage
- [ ] **Logging**: SLF4J + Logback

---

## 🤝 Contributing

Contributions are welcome! This project improves through community feedback.

### How to Contribute

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Feedback Areas

We especially appreciate feedback on:
- Architecture and design patterns
- Code organization and naming conventions
- Exception handling strategies
- Testability improvements
- Performance optimizations

---

## 👤 Author

**M. Adan Ali**

- GitHub: [@M-AdanAli](https://github.com/M-AdanAli)
- Project Link: [Smart Library Management System](https://github.com/M-AdanAli/Smart-Library-Management-System)

---

## ⭐ Show Your Support

If you found this project helpful or interesting:

- ⭐ **Star** this repository
- 🍴 **Fork** it for your own learning
- 📢 **Share** with others who might benefit

Your support helps guide future improvements and encourages continued development!

---

## 📞 Support

For questions or issues:
- Open an [Issue](https://github.com/M-AdanAli/Smart-Library-Management-System/issues)
- Check existing documentation
- Review the code examples in this README

---

**Built with ❤️ using Core Java and Jackson**