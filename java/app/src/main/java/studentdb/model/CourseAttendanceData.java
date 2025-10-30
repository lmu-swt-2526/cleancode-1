package studentdb.model;

/**
 * Represents the status of a student's enrollment in a course, including attendance status and
 * optional grade.
 *
 * @param status the attendance status (required)
 * @param grade the grade earned (required only when status is FINISHED, otherwise must be null)
 */
public record CourseAttendanceData(StatusData status, CourseAttendanceDetailsGrade grade) {

  /** Represents a grade earned in a completed course. */
  public enum CourseAttendanceDetailsGrade {
    A,
    B,
    C,
    D,
    F
  }

  public CourseAttendanceData {
    if (status == null) {
      throw new IllegalArgumentException("Attendance status cannot be null");
    }

    if (status == StatusData.FINISHED && grade == null) {
      throw new IllegalArgumentException("Grade is required when status is FINISHED");
    }

    if (status != StatusData.FINISHED && grade != null) {
      throw new IllegalArgumentException(
          "Grade must be null when status is not FINISHED (status: " + status + ")");
    }
  }
}
