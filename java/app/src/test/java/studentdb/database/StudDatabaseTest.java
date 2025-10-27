package studentdb.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import studentdb.model.Stud;

/**
 * Test suite for StudentDatabase interface.
 *
 * <p>This abstract test class can be extended by concrete implementations to ensure they correctly
 * implement the StudentDatabase interface contract.
 */
abstract class StudDatabaseTest {

  protected StudentDatabase database;

  /**
   * Factory method to create a fresh database instance for each test.
   *
   * @return a new StudentDatabase instance
   */
  protected abstract StudentDatabase createDatabase();

  @BeforeEach
  void setUp() {
    database = createDatabase();
  }

  // Tests for addStudent

  @Test
  void addValidStudent() throws DatabaseEntryException {
    Stud student = new Stud("Alice Anderson", "S001");
    database.addStudent(student);

    Optional<Stud> found = database.findStudent("Alice Anderson");
    assertTrue(found.isPresent());
    assertEquals(student, found.get());
  }

  @Test
  void addDuplicateStudentThrows() throws DatabaseEntryException {
    Stud student1 = new Stud("Bob Brown", "S001");
    Stud student2 = new Stud("Bob Brown", "S002");

    database.addStudent(student1);
    assertThrows(DatabaseEntryException.class, () -> database.addStudent(student2));
  }

  @Test
  void addNullStudentThrows() {
    assertThrows(NullPointerException.class, () -> database.addStudent(null));
  }

  // Tests for removeStudent

  @Test
  void removeExistingStudent() throws DatabaseEntryException {
    Stud student = new Stud("Charlie Chen", "S003");
    database.addStudent(student);

    database.removeStudent("Charlie Chen");

    Optional<Stud> found = database.findStudent("Charlie Chen");
    assertFalse(found.isPresent());
  }

  @Test
  void removeNonExistentStudentThrows() {
    assertThrows(DatabaseEntryException.class, () -> database.removeStudent("Nonexistent Person"));
  }

  @Test
  void removeNullNameThrows() {
    assertThrows(NullPointerException.class, () -> database.removeStudent(null));
  }

  // Tests for findByName

  @Test
  void findExistingStudent() throws DatabaseEntryException {
    Stud student = new Stud("Diana Davis", "S004");
    database.addStudent(student);

    Optional<Stud> found = database.findStudent("Diana Davis");
    assertTrue(found.isPresent());
    assertEquals(student, found.get());
  }

  @Test
  void findExistingStudentCaseInsensitive() throws DatabaseEntryException {
    Stud student = new Stud("Eve Evans", "S005");
    database.addStudent(student);

    Optional<Stud> found = database.findStudent("eve evans");
    assertTrue(found.isPresent());
    assertEquals(student, found.get());
  }

  @Test
  void findNonExistentStudent() {
    Optional<Stud> found = database.findStudent("Nonexistent Person");
    assertFalse(found.isPresent());
  }

  @Test
  void findStudentWithNullThrows() {
    assertThrows(NullPointerException.class, () -> database.findStudent(null));
  }

  // Tests for findByPrefix

  @Test
  void findStudentsPrefixMultipleMatches() throws DatabaseEntryException {
    database.addStudent(new Stud("Alice Anderson", "S001"));
    database.addStudent(new Stud("Albert Brown", "S002"));
    database.addStudent(new Stud("Bob Smith", "S003"));

    List<Stud> found = database.findStudents("al");
    assertEquals(2, found.size());
    assertTrue(found.stream().anyMatch(s -> s.getName().equals("Alice Anderson")));
    assertTrue(found.stream().anyMatch(s -> s.getName().equals("Albert Brown")));
  }

  @Test
  void findStudentsPrefixNoMatches() throws DatabaseEntryException {
    database.addStudent(new Stud("Alice Anderson", "S001"));
    List<Stud> found = database.findStudents("Z");
    assertTrue(found.isEmpty());
  }

  @Test
  void findStudentsPrefixSingleCharacter() throws DatabaseEntryException {
    database.addStudent(new Stud("Alice Anderson", "S001"));
    database.addStudent(new Stud("Albert Brown", "S002"));
    database.addStudent(new Stud("Bob Smith", "S003"));

    List<Stud> found = database.findStudents("a");
    assertEquals(2, found.size());
  }

  @Test
  void findStudentsPrefixEmpty() throws DatabaseEntryException {
    database.addStudent(new Stud("Alice Anderson", "S001"));
    database.addStudent(new Stud("Bob Smith", "S002"));
    database.addStudent(new Stud("Charlie Chen", "S003"));

    List<Stud> found = database.findStudents("");
    assertEquals(3, found.size());
  }

  @Test
  void findStudentsPrefixWithNullThrows() {
    assertThrows(NullPointerException.class, () -> database.findStudents(null));
  }

  // Tests for getAllStudents

  @Test
  void findStudentsEmpty() {
    List<Stud> all = database.findStudents();
    assertTrue(all.isEmpty());
  }

  @Test
  void findStudents() throws DatabaseEntryException {
    database.addStudent(new Stud("Alice Anderson", "S001"));
    database.addStudent(new Stud("Bob Brown", "S002"));
    database.addStudent(new Stud("Charlie Chen", "S003"));

    List<Stud> all = database.findStudents();
    assertEquals(3, all.size());
  }

  @Test
  void findStudentsAfterRemoval() throws DatabaseEntryException {
    database.addStudent(new Stud("Alice Anderson", "S001"));
    database.addStudent(new Stud("Bob Brown", "S002"));
    database.removeStudent("Alice Anderson");

    List<Stud> all = database.findStudents();
    assertEquals(1, all.size());
    assertEquals("Bob Brown", all.get(0).getName());
  }
}
