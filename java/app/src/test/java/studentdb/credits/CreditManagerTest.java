package studentdb.credits;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import studentdb.model.CourseAttendanceData;
import studentdb.model.CourseAttendanceData.CourseAttendanceDetailsGrade;
import studentdb.model.CreateCourseName;
import studentdb.model.StatusData;
import studentdb.model.Stud;

class CreditManagerTest {

  private CreditManager manager;

  @BeforeEach
  void setUp() {
    manager = new CreditManager();
  }

  // Tests for addCourse()

  @Test
  void testAddCourseSuccessfully() {
    manager.addCourse("Mathematics", 6);
    // No exception means success
  }

  @Test
  void testAddCourseWithNullName() {
    assertThrows(IllegalArgumentException.class, () -> manager.addCourse(null, 6));
  }

  @Test
  void testAddCourseWithBlankName() {
    assertThrows(IllegalArgumentException.class, () -> manager.addCourse("  ", 6));
  }

  @Test
  void testAddCourseWithZeroCredits() {
    assertThrows(IllegalArgumentException.class, () -> manager.addCourse("Mathematics", 0));
  }

  @Test
  void testAddCourseWithNegativeCredits() {
    assertThrows(IllegalArgumentException.class, () -> manager.addCourse("Mathematics", -5));
  }

  @Test
  void testAddDuplicateCourse() {
    manager.addCourse("Mathematics", 6);
    assertThrows(IllegalArgumentException.class, () -> manager.addCourse("Mathematics", 6));
  }

  // Tests for updateCourse()

  @Test
  void testUpdateCourseSuccessfully() {
    manager.addCourse("Mathematics", 6);
    manager.updateCourse("Mathematics", 8);
    // No exception means success
  }

  @Test
  void testUpdateNonExistentCourse() {
    assertThrows(IllegalStateException.class, () -> manager.updateCourse("Physics", 6));
  }

  @Test
  void testUpdateCourseWithNullName() {
    assertThrows(IllegalArgumentException.class, () -> manager.updateCourse(null, 6));
  }

  @Test
  void testUpdateCourseWithInvalidCredits() {
    manager.addCourse("Mathematics", 6);
    assertThrows(IllegalArgumentException.class, () -> manager.updateCourse("Mathematics", -1));
  }

  // Tests for deleteCourse()

  @Test
  void testDeleteCourseSuccessfully() {
    manager.addCourse("Mathematics", 6);
    manager.deleteCourse("Mathematics");
    // Should be able to add it again after deletion
    manager.addCourse("Mathematics", 8);
  }

  @Test
  void testDeleteNonExistentCourse() {
    assertThrows(IllegalStateException.class, () -> manager.deleteCourse("Physics"));
  }

  @Test
  void testDeleteCourseWithNullName() {
    assertThrows(IllegalArgumentException.class, () -> manager.deleteCourse(null));
  }

  // Tests for getNumberOfAttendedCourses()

  @Test
  void testGetNumberOfCoursesForStudentWithNoCourses() {
    Stud student = new Stud("Alice", "S001");
    assertEquals(0, manager.getNumberOfAttendedCourses(student));
  }

  @Test
  void testGetNumberOfCoursesWithMultipleCourses() {
    Stud student = new Stud("Alice", "S001");
    student.addition(new CreateCourseName("Mathematics"), new CourseAttendanceData(StatusData.ATTENDING, null));
    student.addition(new CreateCourseName("Physics"), new CourseAttendanceData(StatusData.SCHEDULED, null));
    student.addition(new CreateCourseName("Chemistry"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.A));

    assertEquals(3, manager.getNumberOfAttendedCourses(student));
  }

  @Test
  void testGetNumberOfCoursesWithNullStudent() {
    assertThrows(IllegalArgumentException.class, () -> manager.getNumberOfAttendedCourses(null));
  }

  // Tests for getAverageGrade()

  @Test
  void testAverageGradeWithNoFinishedCourses() {
    manager.addCourse("Mathematics", 6);
    Stud student = new Stud("Alice", "S001");
    student.addition(new CreateCourseName("Mathematics"), new CourseAttendanceData(StatusData.ATTENDING, null));

    assertEquals(0.0, manager.getAverageGrade(student), 0.001);
  }

  @Test
  void testAverageGradeWithSingleCourse() {
    manager.addCourse("Mathematics", 6);
    Stud student = new Stud("Alice", "S001");
    student.addition(new CreateCourseName("Mathematics"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.B));

    // B=2, so average = (2*6)/6 = 2.0
    assertEquals(2.0, manager.getAverageGrade(student), 0.001);
  }

  @Test
  void testAverageGradeWithMultipleCourses() {
    manager.addCourse("Mathematics", 6);
    manager.addCourse("Physics", 4);
    manager.addCourse("Chemistry", 5);

    Stud student = new Stud("Alice", "S001");
    student.addition(new CreateCourseName("Mathematics"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.A));
    student.addition(new CreateCourseName("Physics"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.B));
    student.addition(new CreateCourseName("Chemistry"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.C));

    // A=1, B=2, C=3
    // (1*6 + 2*4 + 3*5) / (6 + 4 + 5) = (6 + 8 + 15) / 15 = 29/15 ≈ 1.933
    assertEquals(1.933, manager.getAverageGrade(student), 0.001);
  }

  @Test
  void testAverageGradeExcludesFailedCourses() {
    manager.addCourse("Mathematics", 6);
    manager.addCourse("Physics", 4);

    Stud student = new Stud("Alice", "S001");
    student.addition(new CreateCourseName("Mathematics"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.A));
    student.addition(new CreateCourseName("Physics"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.F));

    // Only Mathematics counts: (1*6)/6 = 1.0
    assertEquals(1.0, manager.getAverageGrade(student), 0.001);
  }

  @Test
  void testAverageGradeExcludesNonFinishedCourses() {
    manager.addCourse("Mathematics", 6);
    manager.addCourse("Physics", 4);

    Stud student = new Stud("Alice", "S001");
    student.addition(new CreateCourseName("Mathematics"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.A));
    student.addition(new CreateCourseName("Physics"), new CourseAttendanceData(StatusData.ATTENDING, null));

    // Only Mathematics counts: (1*6)/6 = 1.0
    assertEquals(1.0, manager.getAverageGrade(student), 0.001);
  }

  @Test
  void testAverageGradeWithAllGrades() {
    manager.addCourse("Math", 2);
    manager.addCourse("Physics", 2);
    manager.addCourse("Chemistry", 2);
    manager.addCourse("Biology", 2);

    Stud student = new Stud("Alice", "S001");
    student.addition(new CreateCourseName("Math"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.A));
    student.addition(new CreateCourseName("Physics"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.B));
    student.addition(new CreateCourseName("Chemistry"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.C));
    student.addition(new CreateCourseName("Biology"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.D));

    // (1*2 + 2*2 + 3*2 + 4*2) / (2+2+2+2) = (2+4+6+8)/8 = 20/8 = 2.5
    assertEquals(2.5, manager.getAverageGrade(student), 0.001);
  }

  @Test
  void testAverageGradeWithNullStudent() {
    assertThrows(IllegalArgumentException.class, () -> manager.getAverageGrade(null));
  }

  @Test
  void testAverageGradeWithCourseNotInCatalog() {
    Stud student = new Stud("Alice", "S001");
    student.addition(new CreateCourseName("UnknownCourse"), new CourseAttendanceData(StatusData.FINISHED, CourseAttendanceDetailsGrade.A));

    assertThrows(IllegalStateException.class, () -> manager.getAverageGrade(student));
  }
}
