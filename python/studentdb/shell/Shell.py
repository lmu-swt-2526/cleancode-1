import sys

from studentdb.database.DatabaseEntryException import DatabaseEntryException
from studentdb.database.StudentDatabase import StudentDatabase
from studentdb.database.TrieStudentDatabase import TrieStudentDatabase
from studentdb.model.Stud import Stud


# Initial version:
# Author: Thomas Lemberger
# Date: 2025-10-27
# Description: A simple command-line shell for interacting with the student database.
#
# Change 1:
# Author: Thomas Lemberger
# Date: 2025-10-27
# Description: Added menu options for adding the ECTS credits a course provides
#
# Change 2:
# Author: Thomas Lemberger
# Date: 2025-10-27
# Description: CreditManager didn't work, commented for now until we have time to continue on this (probably January)
#
# Chagne 3:
# Author: Thomas Lemberger
# Date: 2025-10-27
# Description: Fixed indices in menu (7: Exit -> 6: Exit)


class Shell:
    """The shell."""

    ######### Constructors of Shell #########

    def __init__(self):
        # Student database
        self._database: StudentDatabase = TrieStudentDatabase()
        # Credit manager
        # self._creditManager = CreditManager()

        self._currentMainMenuActionChoice: int = -1
        self._isShellRunning: bool = True

    ######### End Constructors of Shell #########

    @staticmethod
    def main():
        # It may look a bit over-the-top to create a Shell instance just to call run(),
        # but we keep Shell dynamic on purpose to be able to extend it more easily later on.
        shell = Shell()
        shell.run()

    ##########  Public Methods of Shell Class ##########

    def _exitShell(self) -> None:
        self._isShellRunning = False
        print("Goodbye!")

    def run(self) -> None:
        actions = {
            1: self._addStudent,
            2: self._removeStudent,
            3: self._searchByName,
            4: self._searchByPrefix,
            5: self._listAllStudents,
            # 6: self._addCourseToCatalog,
            6: self._exitShell
        } # endDict actions line 60

        while self._isShellRunning:
            self._printMenu()
            self._setMainMenuInput()
            if self._currentMainMenuActionChoice in actions:
                actions[self._currentMainMenuActionChoice]()
            else:
                print("Invalid choice. Please try again.")
            print()
        # endWhile line 70
    # endMethod run line 59

    ##################################################
    ########## Private Methods of Shell Class ##########
    ##################################################

    def _printMenu(self) -> None:
        print("Menu:")
        print("  1. Add student")
        print("  2. Remove student")
        print("  3. Search by name")
        print("  4. Search by prefix")
        print("  5. List all students")
        # print("  6. Add course to catalog")
        print("  6. Exit")
        print("Enter choice: ", end="")

    def _setMainMenuInput(self) -> None:
        choice = input().strip()
        try:
            # Decides on the concrete action
            self._currentMainMenuActionChoice = int(choice)
        except ValueError:
            # Invalid input means we just loop again
            pass

    def _addStudent(self) -> None:
        print("Enter student name: ", end="")
        # The input() function takes user input from stdin until the user presses Enter.
        # It then returns the full line including leading/trailing spaces to us.
        name = input().strip()
        if len(name) == 0:
            print("Error: Name cannot be empty")
            return

        # print() prints the student ID to the console that is visible to the user.
        print("Enter student ID: ", end="")
        id = input().strip()
        if len(id) == 0:
            print("Error: ID cannot be empty")
            return

        try:
            student = Stud(name, id)
            # The TrieStudentDatabase will do some checks on the student first and then store it in a format
            # that allows us to do fast prefix searches.
            self._database.addStudent(student)
            print("Student added successfully")
        except DatabaseEntryException as e:
            # This exception can only happen if a student with the same name already exists,
            # so provide specific error message.
            print(f"Error: Student {name} already exists")

    def _removeStudent(self) -> None:
        print("Enter student name: ", end="")
        name = input().strip()
        if len(name) == 0:
            print("Error: Name cannot be empty")
            return
        try:
            self._database.removeStudent(name)
            print("Student removed successfully")
        except DatabaseEntryException as e:
            print(f"Error: {str(e)}")

    def _searchByName(self) -> None:
        print("Enter student name: ", end="")
        name = input().strip()
        if len(name) == 0:
            print("Error: Name cannot be empty")
            return

        student = self._database.findStudent(name)
        if student is not None:
            self._printStudent(student)
        else:
            print(f"No student found with name: {name}")

    def _searchByPrefix(self) -> None:
        print("Enter name prefix (or press Enter for all): ", end="")
        prefix = input().strip()

        students = self._database.findStudents(prefix)
        if len(students) == 0:
            print(f"No students found with prefix: {prefix}")
        else:
            print(f"Found {len(students)} student(s):")
            for student in students:
                self._printStudent(student)

    def _listAllStudents(self) -> None:
        students = self._database.findStudents()
        if len(students) == 0:
            print("No students in database")
        else:
            print(f"Total students: {len(students)}")
            for student in students:
                self._printStudent(student)

    def _printStudent(self, student: Stud) -> None:
        print(f"  - {student.name} (ID: {student.id})")

    # def _addCourseToCatalog(self) -> None:
    #     print("Enter course name: ", end="")
    #     courseName = input().strip()
    #     if len(courseName) == 0:
    #         print("Error: Course name cannot be empty")
    #         return
    #
    #     print("Enter ECTS credits: ", end="")
    #     creditsInput = input().strip()
    #     if len(creditsInput) == 0:
    #         print("Error: Credits cannot be empty")
    #         return
    #
    #     try:
    #         credits = int(creditsInput)
    #         self._creditManager.addCourse(courseName, credits)
    #         print("Course added successfully")
    #     except ValueError as e:
    #         print("Error: Credits must be a valid integer")
    #     except ValueError as e:
    #         print(f"Error: {str(e)}")
    #     except RuntimeError as e:
    #         print(f"Error: {str(e)}")


if __name__ == "__main__":
    Shell.main(sys.argv)
