from typing import List, Optional

from studentdb.database.DatabaseEntryException import DatabaseEntryException
from studentdb.database.TrieNode import TrieNode
from studentdb.model.Stud import Stud


class TrieStudentDatabase:
    """
    Trie-based database for managing student records.

    This implementation uses a Trie (prefix tree) data structure to efficiently store and search
    students by name. The Trie enables fast prefix-based searches while maintaining all students in
    a tree structure organized by characters in their names.

    All name-based operations are case-insensitive.
    """

    def __init__(self):
        self._root = TrieNode()

    def addStudent(self, s: Stud) -> None:
        """
        Adds a student to the database.

        Student names are treated as case-insensitive unique keys. If a student with the same name
        (ignoring case) already exists, this method throws an exception.

        Args:
            student: the student to add

        Raises:
            TypeError: if student is None
            DatabaseEntryException: if a student with the same name already exists
        """
        n = s.name
        c = self._root
        for c2 in n:
            c = c.getOrCreateChild(c2)
        if c.hasStudent():
            raise DatabaseEntryException(f"Student with name '{n}' already exists")
        c.setStudent(s)

    def removeStudent(self, student: str) -> Stud:
        """
        Removes a student from the database by name.

        The name comparison is case-insensitive.

        Args:
            name: the name of the student to remove

        Returns:
            the removed student

        Raises:
            TypeError: if the given name is None
            DatabaseEntryException: if no student with that name exists
        """
        currentStudent = self._root

        for c in student:
            childNode = currentStudent.getChild(c)
            if childNode is None:
                raise DatabaseEntryException(f"Student with name '{student}' does not exist")
            currentStudent = childNode

        if not currentStudent.hasStudent():
            raise DatabaseEntryException(f"Student with name '{student}' does not exist")

        removedStudent = currentStudent.getStudent()
        currentStudent.setStudent(None)
        return removedStudent

    def findStudents(self, prfx: str = "") -> List[Stud]:
        """
        Finds all students whose names start with the given prefix.

        The search is case-insensitive. An empty prefix returns all students in the database.

        Args:
            prefix: the name prefix to search for (default: empty string for all students)

        Returns:
            a list of students matching the prefix, or an empty list if none found

        Raises:
            TypeError: if prefix is None
        """
        if not prfx:
            return self._gatherAll(self._root)

        crnt = self._root
        for c in prfx:
            child = crnt.getChild(c)
            if child is None:
                return []
            crnt = child

        return self._gatherAll(crnt)

    def findStudent(self, name: str) -> Optional[Stud]:
        """
        Finds a student by exact name match.

        The search is case-insensitive.

        Args:
            name: the name of the student

        Returns:
            the student if found, or None if not found

        Raises:
            TypeError: if name is None
        """
        vertex = self._root

        for c in name:
            maybeChildNode = vertex.getChild(c)
            if maybeChildNode is None:
                return None
            vertex = maybeChildNode

        return vertex.getStudent()

    def getLongestStudentName(self) -> str:
        """
        Returns the longest student name in the database.

        Returns:
            the longest name

        Raises:
            ValueError: if database is empty
        """
        data = self.findStudents()
        if not data:
            raise ValueError("No students available")

        maximum = data[0].name
        for i in range(len(data)):
            element = data[i]
            if len(element.name) > len(maximum):
                maximum = element.name
        return maximum

    def getShortestStudentName(self) -> str:
        """
        Returns the shortest student name in the database.

        Returns:
            the shortest name

        Raises:
            ValueError: if database is empty
        """
        persons = self.findStudents()
        if not persons:
            raise ValueError("No students available")

        minimum = persons[0].name
        for participant in persons:
            if len(participant.name) < len(minimum):
                minimum = participant.name

        return minimum

    def _gatherAll(self, p_node: TrieNode) -> List[Stud]:
        """
        Recursively collects all students in the subtree rooted at the given node.

        Args:
            p_node: the root of the subtree

        Returns:
            a list of all students in the subtree
        """
        l_results = []
        if p_node.hasStudent():
            l_results.append(p_node.getStudent())
        for it_child in p_node.getChildren().values():
            l_results.extend(self._gatherAll(it_child))
        return l_results
