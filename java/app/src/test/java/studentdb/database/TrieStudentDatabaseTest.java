package studentdb.database;

/**
 * Concrete test class for TrieStudentDatabase.
 *
 * <p>This class extends the abstract StudentDatabaseTest to verify that TrieStudentDatabase
 * correctly implements the StudentDatabase interface.
 */
class TrieStudentDatabaseTest extends StudDatabaseTest {

  @Override
  protected StudentDatabase createDatabase() {
    return new TrieStudentDatabase();
  }
}
