package studentdb.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a student.
 */
public class Stud {

  private final String name;
  private final String uniqueId;
  private final AttendanceList attendedCourses = new AttendanceList();

  /**
   * Creates a student with an empty course map.
   *
   * @param name the student's full name
   * @param id the student ID. Must be unique across all students.
   */
  public Stud(String name, String id) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(id);
    if (name.isBlank()) {
      throw new IllegalArgumentException("Student name cannot be blank");
    }
    if (id.isBlank()) {
      throw new IllegalArgumentException("Student ID cannot be blank");
    }

    this.name = name;
    uniqueId = id;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return uniqueId;
  }

  /**
   * Adds or updates a course and its status for this student. If the course already exists, its
   * status is updated.
   *
   * @param course the course to add or update
   * @param courseAttendanceDetails the status of the course
   * @throws NullPointerException if course or courseStatus is null
   */
  public void addition(CreateCourseName course, CourseAttendanceData courseAttendanceDetails) {
    Objects.requireNonNull(course);
    Objects.requireNonNull(courseAttendanceDetails);
    attendedCourses.put(course, courseAttendanceDetails);
  }

  /**
   * Returns whether this student has attended (or is attending/scheduled for) a given course.
   *
   * @param course the course to check
   * @return true if the student has this course in their records, false otherwise
   * @throws NullPointerException if course is null
   */
  public boolean courseAttendance(CreateCourseName course) {
    Objects.requireNonNull(course);
    return attendedCourses.containsKey(course);
  }

  /**
   * Returns the status of a given course for this student.
   *
   * @param course the course whose status to retrieve
   * @return the course status
   * @throws IllegalArgumentException if course is null
   * @throws IllegalStateException if the course does not exist in the student's course map
   */
  public CourseAttendanceData getStatus(CreateCourseName course) {
    Objects.requireNonNull(course);
    if (!attendedCourses.containsKey(course)) {
      throw new IllegalStateException("Course not found in student's records: " + course.name());
    }
    return attendedCourses.get(course);
  }

  /**
   * Returns the map of attended courses.
   *
   * @return the attendance list containing all courses and their status
   */
  public AttendanceList getAttendedCourses() {
    return attendedCourses;
  }
}
