package studentdb.database;

/** Exception thrown when a database operation fails due to data constraints. */
public class DatabaseEntryException extends Exception {

  public DatabaseEntryException(String message) {
    super(message);
  }

  public DatabaseEntryException(String message, Throwable cause) {
    super(message, cause);
  }
}
