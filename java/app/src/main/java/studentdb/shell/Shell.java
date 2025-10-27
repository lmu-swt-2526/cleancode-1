package studentdb.shell;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
//import studentdb.credits.CreditManager;
import studentdb.database.DatabaseEntryException;
import studentdb.database.StudentDatabase;
import studentdb.database.TrieStudentDatabase;
import studentdb.model.Stud;

// Initial version:
// Author: Thomas Lemberger
// Date: 2025-10-27
// Description: A simple command-line shell for interacting with the student database.
//
// Change 1:
// Author: Thomas Lemberger
// Date: 2025-10-27
// Description: Added menu options for adding the ECTS credits a course provides
//
// Change 2:
// Author: Thomas Lemberger
// Date: 2025-10-27
// Description: CreditManager didn't work, commented for now until we have time to continue on this (probably January)
//
// Chagne 3:
// Author: Thomas Lemberger
// Date: 2025-10-27
// Description: Fixed indices in menu (7: Exit -> 6: Exit)

/** The shell. */
public class Shell {

  // Student database
  private final StudentDatabase database;
  // Credit manager
  // private final CreditManager creditManager = new CreditManager();
  // Scanner
  private final Scanner scanner;

  private int currentMainMenuActionChoice = -1;
  private boolean isShellRunning = true;

  ////// Constructors of Shell ////////

  public Shell() {
    this.database = new TrieStudentDatabase();
    // We assume UTF-8 encoding by default.
    // The shell will not work if the console this application is run through
    // uses a different encoding.
    this.scanner = new Scanner(System.in);
  }

  ////// End Constructors of Shell ////////

  public static void main(String[] args) {
    // It may look a bit over-the-top to create a Shell instance just to call run(),
    // but we keep Shell dynamic on purpose to be able to extend it more easily later on.
    Shell shell = new Shell();
    shell.run();
  }

  //////// Public Methods of Shell Class //////////

  public void run() {
    while (isShellRunning) {
      printMenu();
      setMainMenuInput();
      switch (currentMainMenuActionChoice) {
        // 1 = Add student
        case 1:
          addStudent();
          break;
        // 2 = Remove student
        case 2:
          removeStudent();
          break;
        // 3 = Search by name
        case 3:
          searchByName();
          break;
        // 4 = Search by prefix
        case 4:
          searchByPrefix();
          break;
        // 5 = List all students
        case 5:
          listAllStudents();
          break;
//        case "6":
//          addCourseToCatalog();
//          break;
        // 6 = Exit
        case 6:
          isShellRunning = false;
          System.out.println("Goodbye!");
          break;
        default:
          System.out.println("Invalid choice. Please try again.");
      } // endSwitch line 65
      System.out.println();
    } // endWhile line 62
  } // endMethod run line 60

  //////////////////////////////////////////////////
  //////// Private Methods of Shell Class //////////
  //////////////////////////////////////////////////

  private void printMenu() {
    System.out.println("Menu:");
    System.out.println("  1. Add student");
    System.out.println("  2. Remove student");
    System.out.println("  3. Search by name");
    System.out.println("  4. Search by prefix");
    System.out.println("  5. List all students");
    //System.out.println("  6. Add course to catalog");
    System.out.println("  6. Exit");
    System.out.print("Enter choice: ");
  }

  private void setMainMenuInput() {
    String choice = scanner.nextLine().trim();
    try {
      // Decides on the concrete action
      currentMainMenuActionChoice = Integer.parseInt(choice);
    } catch (NumberFormatException e) {
      // Invalid input means we just loop again
    }
  }

  private void addStudent() {
    System.out.print("Enter student name: ");
    // The scanner takes user input from System.in until the user presses Enter.
    // It then returns the full line including leading/trailing spaces to us.
    String name = scanner.nextLine().trim();
    if (name.isEmpty()) {
      System.out.println("Error: Name cannot be empty");
      return;
    }

    // System.out.print prints the student ID to the console that is visible to the user.
    System.out.print("Enter student ID: ");
    String id = scanner.nextLine().trim();
    if (id.isEmpty()) {
      System.out.println("Error: ID cannot be empty");
      return;
    }

    try {
      Stud student = new Stud(name, id);
      // The TrieStudentDatabase will do some checks on the student first and then store it in a format
      // that allows us to do fast prefix searches.
      database.addStudent(student);
      System.out.println("Student added successfully");
    } catch (DatabaseEntryException e) {
      // This exception can only happen if a student with the same name already exists,
      // so provide specific error message.
      System.out.println("Error: Student " + name + " already exists");
    }
  }

  private void removeStudent() {
    System.out.print("Enter student name: ");
    String name = scanner.nextLine().trim();
    if (name.isEmpty()) {
      System.out.println("Error: Name cannot be empty");
      return;
    }
    try {
      database.removeStudent(name);
      System.out.println("Student removed successfully");
    } catch (DatabaseEntryException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private void searchByName() {
    System.out.print("Enter student name: ");
    String name = scanner.nextLine().trim();
    if (name.isEmpty()) {
      System.out.println("Error: Name cannot be empty");
      return;
    }

    Optional<Stud> student = database.findStudent(name);
    if (student.isPresent()) {
      printStudent(student.get());
    } else {
      System.out.println("No student found with name: " + name);
    }
  }

  private void searchByPrefix() {
    System.out.print("Enter name prefix (or press Enter for all): ");
    String prefix = scanner.nextLine().trim();

    List<Stud> students = database.findStudents(prefix);
    if (students.isEmpty()) {
      System.out.println("No students found with prefix: " + prefix);
    } else {
      System.out.println("Found " + students.size() + " student(s):");
      for (Stud student : students) {
        printStudent(student);
      }
    }
  }

  private void listAllStudents() {
    List<Stud> students = database.findStudents();
    if (students.isEmpty()) {
      System.out.println("No students in database");
    } else {
      System.out.println("Total students: " + students.size());
      for (Stud student : students) {
        printStudent(student);
      }
    }
  }

  private void printStudent(Stud student) {
    System.out.println("  - " + student.getName() + " (ID: " + student.getId() + ")");
  }
//
//  private void addCourseToCatalog() {
//    System.out.print("Enter course name: ");
//    String courseName = scanner.nextLine().trim();
//    if (courseName.isEmpty()) {
//      System.out.println("Error: Course name cannot be empty");
//      return;
//    }
//
//    System.out.print("Enter ECTS credits: ");
//    String creditsInput = scanner.nextLine().trim();
//    if (creditsInput.isEmpty()) {
//      System.out.println("Error: Credits cannot be empty");
//      return;
//    }
//
//    try {
//      int credits = Integer.parseInt(creditsInput);
//      creditManager.addCourse(courseName, credits);
//      System.out.println("Course added successfully");
//    } catch (NumberFormatException e) {
//      System.out.println("Error: Credits must be a valid integer");
//    } catch (IllegalArgumentException e) {
//      System.out.println("Error: " + e.getMessage());
//    } catch (IllegalStateException e) {
//      System.out.println("Error: " + e.getMessage());
//    }
//  }
}
