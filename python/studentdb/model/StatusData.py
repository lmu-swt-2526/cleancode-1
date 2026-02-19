from enum import Enum


class StatusData(Enum):
    """
    Represents the current status of a student's attendance in a course.
    """

    ATTENDING = "ATTENDING"
    """Student is currently attending the course."""

    SCHEDULED = "SCHEDULED"
    """Student is scheduled to attend the course in the future."""

    FINISHED = "FINISHED"
    """Student has finished the course."""
