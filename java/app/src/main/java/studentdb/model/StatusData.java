package studentdb.model;

/**
 * Represents the current status of a student's attendance in a course.
 */
public enum StatusData {

    /**
     * Student is currently attending the course.
     */
    ATTENDING,

    /**
     * Student is scheduled to attend the course in the future.
     */
    SCHEDULED,

    /**
     * Student has finished the course.
     */
    FINISHED
}
