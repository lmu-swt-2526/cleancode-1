from studentdb.model.AttendanceList import AttendanceList
from studentdb.model.CreateCourseName import CreateCourseName
from studentdb.model.CourseAttendanceData import CourseAttendanceData


class Stud:
    """
    Represents a student.
    """

    def __init__(self, name: str, studentId: str):
        """
        Creates a student with an empty course map.

        Args:
            name: the student's full name
            id: the student ID. Must be unique across all students.
        """
        if name is None:
            raise TypeError("name cannot be None")
        if studentId is None:
            raise TypeError("id cannot be None")
        if not name or name.isspace():
            raise ValueError("Student name cannot be blank")
        if not studentId or studentId.isspace():
            raise ValueError("Student ID cannot be blank")

        self._name = name
        self._uniqueId = studentId
        self._attendedCourses = AttendanceList()

    @property
    def name(self) -> str:
        return self._name

    @property
    def id(self) -> str:
        return self._uniqueId

    def addition(self, course: CreateCourseName, courseAttendanceDetails: CourseAttendanceData) -> None:
        """
        Adds or updates a course and its status for this student. If the course already exists, its
        status is updated.

        Args:
            course: the course to add or update
            courseAttendanceDetails: the status of the course

        Raises:
            TypeError: if course or courseAttendanceDetails is None
        """
        if course is None:
            raise TypeError("course cannot be None")
        if courseAttendanceDetails is None:
            raise TypeError("courseAttendanceDetails cannot be None")
        self._attendedCourses.put(course, courseAttendanceDetails)

    def courseAttendance(self, course: CreateCourseName) -> bool:
        """
        Returns whether this student has attended (or is attending/scheduled for) a given course.

        Args:
            course: the course to check

        Returns:
            true if the student has this course in their records, false otherwise

        Raises:
            TypeError: if course is None
        """
        if course is None:
            raise TypeError("course cannot be None")
        return self._attendedCourses.containsKey(course)

    def getStatus(self, course: CreateCourseName) -> CourseAttendanceData:
        """
        Returns the status of a given course for this student.

        Args:
            course: the course whose status to retrieve

        Returns:
            the course status

        Raises:
            TypeError: if course is None
            RuntimeError: if the course does not exist in the student's course map
        """
        if course is None:
            raise TypeError("course cannot be None")
        if not self._attendedCourses.containsKey(course):
            raise RuntimeError(f"Course not found in student's records: {course.name}")
        return self._attendedCourses.get(course)

    def getAttendedCourses(self) -> AttendanceList:
        """
        Returns the map of attended courses.

        Returns:
            the attendance list containing all courses and their status
        """
        return self._attendedCourses
