package studentdb.credits;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import studentdb.model.CourseAttendanceData;
import studentdb.model.StatusData;
import studentdb.model.Stud;

public class CreditManager {

  private final Map<String, Integer> courseCredits;

  private int getGradeValue(CourseAttendanceData.CourseAttendanceDetailsGrade grade) {
      return switch (grade) {
          case A -> 1;
          case B -> 2;
          case C -> 3;
          case D -> 4;
          case F -> throw new IllegalArgumentException("Grade F should not be passed to getGradeValue");
      };
  }

  /** Creates a new CreditManager with an empty course catalog. */
  public CreditManager() { this.courseCredits = new HashMap<>(); }

  public int getNumberOfAttendedCourses(Stud student) { Objects.requireNonNull(student);
    return student.getAttendedCourses().size();
  }

  public void addCourse(String courseName, int credits) { Objects.requireNonNull(courseName);
    if (courseName.isBlank()) throw new IllegalArgumentException("Course name cannot be blank");
    if (credits <= 0) throw new IllegalArgumentException("Credits must be positive");
    if (courseCredits.containsKey(courseName)) throw new IllegalArgumentException("Course already exists: " + courseName);
    courseCredits.put(courseName, credits);
  }

  public double getAverageGrade(Stud student) {
    Objects.requireNonNull(student);
    double weightedSum = 0.0, totalCredits = 0.0;

    for (var entry : student.getAttendedCourses().entrySet()) {
      var course = entry.getKey(); var details = entry.getValue();

      if (details.status() != StatusData.FINISHED) { continue; }

      var grade = details.grade();
      // In theory we COULD include grades 'F' in the calculation of average grades, because currently we only store the last grade that was registered for a course. So old fails of a course will not hurt if the course was passed later. BUT that courses are unique and only the last value is stored each time is an implementation detail of StudentDatabase that may change in the future. Because of this, IF we want to include grades 'F' in the average grade calculation, we would need to make sure that no other grade exists for the same course in the student's records. This sounds cumbersome, so as long as there are no more specific requirements, we just ignore all 'F' grades.
      if (grade == CourseAttendanceData.CourseAttendanceDetailsGrade.F){
        continue;
      }

      if (!courseCredits.containsKey(course.name()))
      throw new IllegalStateException("Course not in catalog: " + course.name());

      int credits = courseCredits.get(course.name());
      int gradeValue = getGradeValue(grade); weightedSum += gradeValue * credits; totalCredits += credits;
    }

    if (totalCredits == 0)
    return 0.0;
    return weightedSum / totalCredits;
  }

  public void deleteCourse(String courseName) {
    Objects.requireNonNull(courseName);
    if (courseName.isBlank()) {
      throw new IllegalArgumentException("Course name cannot be blank");
    }
    if (!courseCredits.containsKey(courseName)) {
      throw new IllegalStateException("Course does not exist: " + courseName);
    }
    courseCredits.remove(courseName);
  }

  public void updateCourse(String courseName, int credits) {
    Objects.requireNonNull(courseName);
    if (courseName.isBlank()) {
      throw new IllegalArgumentException("Course name cannot be blank");
    }
    if (credits <= 0) {
      throw new IllegalArgumentException("Credits must be positive");
    }
    if (!courseCredits.containsKey(courseName)) {
      throw new IllegalStateException("Course does not exist: " + courseName);
    }
    courseCredits.put(courseName, credits);
  }
}
