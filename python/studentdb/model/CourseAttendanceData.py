from dataclasses import dataclass
from enum import Enum
from typing import Optional

from studentdb.model.StatusData import StatusData


class CourseAttendanceDetailsGrade(Enum):
    """Represents a grade earned in a completed course."""
    A = "A"
    B = "B"
    C = "C"
    D = "D"
    F = "F"


@dataclass(frozen=True)
class CourseAttendanceData:
    """
    Represents the status of a student's enrollment in a course, including attendance status and
    optional grade.

    Args:
        status: the attendance status (required)
        grade: the grade earned (required only when status is FINISHED, otherwise must be None)
    """
    status: StatusData
    grade: Optional[CourseAttendanceDetailsGrade]

    def __post_init__(self):
        if self.status is None:
            raise ValueError("Attendance status cannot be None")

        if self.status == StatusData.FINISHED and self.grade is None:
            raise ValueError("Grade is required when status is FINISHED")

        if self.status != StatusData.FINISHED and self.grade is not None:
            raise ValueError(
                f"Grade must be None when status is not FINISHED (status: {self.status})"
            )
