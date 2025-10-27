# Student Database System

A Java application for managing student records using a Trie-based data structure.

## Features

- Add and remove students
- Search students by exact name (case-insensitive)
- Search students by name prefix (case-insensitive)
- Interactive command-line interface

## Building

```bash
./gradlew build
```

## Running

```bash
./gradlew run
```

Or use the distribution:

```bash
./app/build/install/app/bin/app
```

## Testing

Run all tests:

```bash
./gradlew test
```

Run specific test class:

```bash
./gradlew test --tests "studentdb.database.*"
```

## Code Quality

The project uses several code quality tools:

### Checkstyle

Enforces Google Java Style coding standards:

```bash
./gradlew checkstyleMain checkstyleTest
```

Configuration: `config/checkstyle/checkstyle.xml`

### SpotBugs

Performs static analysis to find bugs:

```bash
./gradlew spotbugsMain spotbugsTest
```

Reports are generated at `app/build/reports/spotbugs/`

Configuration: `config/spotbugs/excludeFilter.xml`

### Spotless

Automatically formats code according to Google Java Format:

```bash
./gradlew spotlessApply
```

Check formatting without applying:

```bash
./gradlew spotlessCheck
```

### Run All Checks

```bash
./gradlew check
```

## Project Structure

- `app/src/main/java/studentdb/model/` - Domain model classes
- `app/src/main/java/studentdb/database/` - Database interface and implementation
- `app/src/main/java/studentdb/shell/` - Command-line interface
- `doc/reference/` - API and architecture documentation

## Documentation

See [doc/reference/](doc/reference/) for detailed API and architecture documentation.
