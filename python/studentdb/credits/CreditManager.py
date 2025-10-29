from typing import Dict

from studentdb.model.CourseAttendanceData import CourseAttendanceData, CourseAttendanceDetailsGrade
from studentdb.model.StatusData import StatusData
from studentdb.model.Stud import Stud


class CreditManager:

    def __init__(self):
        """Creates a new CreditManager with an empty course catalog."""
        self._courseCredits: Dict[str, int] = {}

    def _getGradeValue(self, grade: CourseAttendanceDetailsGrade) -> int:
        if grade == CourseAttendanceDetailsGrade.A:
            return 1
        elif grade == CourseAttendanceDetailsGrade.B:
            return 2
        elif grade == CourseAttendanceDetailsGrade.C:
            return 3
        elif grade == CourseAttendanceDetailsGrade.D:
            return 4
        elif grade == CourseAttendanceDetailsGrade.F:
            raise ValueError("Grade F should not be passed to getGradeValue")
        else:
            raise ValueError(f"Unknown grade: {grade}")

    def getNumberOfAttendedCourses(self, student: Stud) -> int:
        if student is None:
            raise TypeError("student cannot be None")
        return student.getAttendedCourses().size()

    def addCourse(self, courseName: str, credits: int) -> None:
        if courseName is None:
            raise TypeError("courseName cannot be None")
        if not courseName or courseName.isspace():
            raise ValueError("Course name cannot be blank")
        if credits <= 0:
            raise ValueError("Credits must be positive")
        if courseName in self._courseCredits:
            raise ValueError(f"Course already exists: {courseName}")
        self._courseCredits[courseName] = credits

    def getAverageGrade(self, student: Stud) -> float:
        if student is None:
            raise TypeError("student cannot be None")
        weightedSum = 0.0
        totalCredits = 0.0

        for entry in student.getAttendedCourses().entrySet():
            course = entry[0]
            details = entry[1]

            if details.status != StatusData.FINISHED:
                continue

            grade = details.grade
            # In theory we COULD include grades 'F' in the calculation of average grades, because currently we only store the last grade that was registered for a course. So old fails of a course will not hurt if the course was passed later. BUT that courses are unique and only the last value is stored each time is an implementation detail of StudentDatabase that may change in the future. Because of this, IF we want to include grades 'F' in the average grade calculation, we would need to make sure that no other grade exists for the same course in the student's records. This sounds cumbersome, so as long as there are no more specific requirements, we just ignore all 'F' grades.
            if grade == CourseAttendanceDetailsGrade.F:
                continue

            if course.name not in self._courseCredits:
                raise RuntimeError(f"Course not in catalog: {course.name}")

            credits = self._courseCredits[course.name]
            gradeValue = self._getGradeValue(grade)
            weightedSum += gradeValue * credits
            totalCredits += credits

        if totalCredits == 0:
            return 0.0
        return weightedSum / totalCredits

    def deleteCourse(self, courseName: str) -> None:
        if courseName is None:
            raise TypeError("courseName cannot be None")
        if not courseName or courseName.isspace():
            raise ValueError("Course name cannot be blank")
        if courseName not in self._courseCredits:
            raise RuntimeError(f"Course does not exist: {courseName}")
        del self._courseCredits[courseName]

    def updateCourse(self, courseName: str, credits: int) -> None:
        if courseName is None:
            raise TypeError("courseName cannot be None")
        if not courseName or courseName.isspace():
            raise ValueError("Course name cannot be blank")
        if credits <= 0:
            raise ValueError("Credits must be positive")
        if courseName not in self._courseCredits:
            raise RuntimeError(f"Course does not exist: {courseName}")
        self._courseCredits[courseName] = credits
