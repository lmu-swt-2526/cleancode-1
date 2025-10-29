# Student Database - Python Translation

This is a 1:1 translation of the Java student database project into Python.

## Translation Approach

This translation maintains the exact structure and style of the Java implementation:
- One class per file (matching Java's convention)
- Java-style naming (camelCase methods like `getName()`, `addStudent()`)
- Same file/package structure
- Explicit null checks and type checking
- Wrapper classes that mimic Java collections

**Note:** This is intentionally unpythonic! See `UNPYTHONIC_PATTERNS.md` for a detailed list of what could be improved to make this more Pythonic.

## Project Structure

```
python/
├── studentdb/
│   ├── __init__.py
│   ├── model/
│   │   ├── __init__.py
│   │   ├── StatusData.py              # Enum for course status
│   │   ├── CreateCourseName.py        # Course name record
│   │   ├── CourseAttendanceData.py    # Course attendance with grade
│   │   ├── AttendanceList.py          # HashMap wrapper for courses
│   │   └── Stud.py                    # Student class
│   ├── database/
│   │   ├── __init__.py
│   │   ├── DatabaseEntryException.py  # Custom exception
│   │   ├── StudentDatabase.py         # Database interface (duck typing)
│   │   ├── TrieNode.py                # Trie node structure
│   │   └── TrieStudentDatabase.py     # Trie-based implementation
│   ├── credits/
│   │   ├── __init__.py
│   │   └── CreditManager.py           # Course credit management
│   └── shell/
│       ├── __init__.py
│       └── Shell.py                   # Command-line interface
├── README.md
└── UNPYTHONIC_PATTERNS.md             # List of Java-isms and suggestions
```

## Running the Application

```bash
cd python
python -m studentdb.shell.Shell
```

Or:

```bash
cd python
python studentdb/shell/Shell.py
```

## Features

The application provides a command-line interface for:
1. Adding students (name + ID)
2. Removing students by name
3. Searching for a student by exact name
4. Searching for students by name prefix
5. Listing all students

The database uses a Trie (prefix tree) data structure for efficient prefix-based searches.

## Key Differences from Java

While the translation is 1:1, Python requires some adaptations:

1. **No interfaces**: Python uses duck typing, so `StudentDatabase` is a regular class with `NotImplementedError` methods
2. **Records → Dataclasses**: Java records become `@dataclass(frozen=True)`
3. **Enums**: Python's `Enum` class is used
4. **Optional**: Python's `Optional` type hint replaces Java's `Optional<T>`
5. **Scanner → input()**: Java's `Scanner` becomes Python's `input()`
6. **Checked exceptions**: Python doesn't have checked exceptions, but we still raise/catch the same exceptions

## Translation Details

### Model Package
- **StatusData**: Enum with three states (ATTENDING, SCHEDULED, FINISHED)
- **CreateCourseName**: Simple dataclass holding a course name
- **CourseAttendanceData**: Dataclass with status and optional grade, includes validation
- **AttendanceList**: Wrapper around dict mimicking Java's Map interface
- **Stud**: Student class with name, ID, and attendance list

### Database Package
- **DatabaseEntryException**: Custom exception for database errors
- **StudentDatabase**: Base class defining the interface (uses duck typing)
- **TrieNode**: Node in the Trie structure with character-based children
- **TrieStudentDatabase**: Trie-based implementation with O(m) prefix search where m = prefix length

### Credits Package
- **CreditManager**: Manages course catalog and calculates weighted average grades

### Shell Package
- **Shell**: Interactive command-line interface for the database

## Known Issues (Inherited from Java)

1. **Bug in getLongestStudentName()**: Returns `element.getId()` instead of `element.getName()` on line that checks length (TrieStudentDatabase.py:95)
2. **Commented code**: CreditManager integration is commented out in Shell
3. **Verbose naming**: Many abbreviated variable names (prfx, crnt, l_results, p_node, etc.)

## Next Steps

To make this codebase Pythonic, refer to `UNPYTHONIC_PATTERNS.md` which contains:
- 20+ specific patterns that could be improved
- Code examples showing Pythonic alternatives
- Recommendations for refactoring

Major improvements would include:
- Consolidating files (one module per package instead of one file per class)
- Converting Java camelCase to Python snake_case
- Using `@property` instead of getter/setter methods
- Removing the `AttendanceList` wrapper and using dict directly
- Simplifying exception handling
- Using Python idioms (comprehensions, context managers, etc.)
