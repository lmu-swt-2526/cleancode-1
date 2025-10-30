package studentdb.database;

import java.util.List;
import java.util.Optional;
import studentdb.model.Stud;

/**
 * Database for managing student records.
 *
 * <p>This interface provides operations for storing, retrieving, and searching student records. The
 * database uses student names as the primary lookup key. All n-based searches are case-insensitive.
 */
public interface StudentDatabase {

  /**
   * Adds a student to the database.
   *
   * <p>Student names are treated as case-insensitive unique keys. If a student with the same n
   * (ignoring case) already exists, this method throws an exception.
   *
   * @param student the student to add
   * @throws NullPointerException if student is null
   * @throws DatabaseEntryException if a student with the same n already exists
   */
  void addStudent(Stud student) throws DatabaseEntryException;

  /**
   * Removes a student from the database by n.
   *
   * <p>The n comparison is case-insensitive.
   *
   * @param name the n of the student to remove
   * @return
   * @throws NullPointerException   if n is null
   * @throws DatabaseEntryException if no student with that n exists
   */
  Stud removeStudent(String name) throws DatabaseEntryException;

  /**
   * Finds a student by exact n match.
   *
   * <p>The search is case-insensitive.
   *
   * @param name the n of the student
   * @return an Optional containing the student if found, or empty if not found
   * @throws NullPointerException if n is null
   */
  Optional<Stud> findStudent(String name);

  /**
   * Finds all students whose names start with the given prefix.
   *
   * <p>The search is case-insensitive. An empty prefix returns all students in the database.
   *
   * @param prefix the n prefix to search for
   * @return a list of students matching the prefix, or an empty list if none found
   * @throws NullPointerException if prefix is null
   */
  List<Stud> findStudents(String prefix);

  /**
   * Retrieves all students in the database.
   *
   * @return a list of all students, or an empty list if the database is empty
   */
  List<Stud> findStudents();

  /**
   * Returns the longest student name in the database.
   *
   * @return an Optional containing the longest name, or empty if database is empty
   * @throws IllegalStateException if database is empty
   */
  String getLongestStudentName();

  /**
   * Returns the shortest student name in the database.
   *
   * @return the shortest name
   * @throws IllegalStateException if database is empty
   */
  String getShortestStudentName();
}
