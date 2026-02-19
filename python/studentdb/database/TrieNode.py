from typing import Dict, Optional

from studentdb.model.Stud import Stud


class TrieNode:
    """
    A node in the Trie data structure for storing students.

    Each node represents a character in a student's name and contains:

    - A map of child nodes for the next characters
    - An optional Student reference if this node represents the end of a name

    The Trie is case-insensitive, so all characters are stored in lowercase.
    """

    def __init__(self):
        self._children: Dict[str, 'TrieNode'] = {}
        self._student: Optional[Stud] = None

    def getChild(self, c: str) -> Optional['TrieNode']:
        """
        Returns the child node for the given character.

        Args:
            c: the character

        Returns:
            an Optional containing the child node if it exists, or None otherwise
        """
        return self._children.get(c.lower())

    def getOrCreateChild(self, c: str) -> 'TrieNode':
        """
        Creates and returns a child node for the given character if it doesn't exist.

        Args:
            c: the character

        Returns:
            the child node (existing or newly created)
        """
        lower_c = c.lower()
        if lower_c not in self._children:
            self._children[lower_c] = TrieNode()
        return self._children[lower_c]

    def getStudent(self) -> Optional[Stud]:
        """
        Returns the student stored at this node.

        Returns:
            the student if this node represents the end of a name
        """
        return self._student

    def setStudent(self, student: Optional[Stud]) -> None:
        """
        Stores a student at this node, marking this as the end of a name.

        Args:
            student: the student to store
        """
        self._student = student

    def hasStudent(self) -> bool:
        """
        Checks if this node has a student stored.

        Returns:
            true if a student is stored at this node, false otherwise
        """
        return self._student is not None

    def getChildren(self) -> Dict[str, 'TrieNode']:
        """
        Returns a map of all child nodes.

        Returns:
            the children map
        """
        return self._children
