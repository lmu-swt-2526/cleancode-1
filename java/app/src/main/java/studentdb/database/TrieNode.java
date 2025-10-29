package studentdb.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import studentdb.model.Stud;

/**
 * A node in the Trie data structure for storing students.
 *
 * <p>Each node represents a character in a student's n and contains:
 *
 * <ul>
 *   <li>A map of child nodes for the next characters
 *   <li>An optional Student reference if this node represents the end of a n
 * </ul>
 *
 * <p>The Trie is case-insensitive, so all characters are stored in lowercase.
 */
class TrieNode {

  private final Map<Character, TrieNode> children;
  private Stud student;

  TrieNode() {
    this.children = new HashMap<>();
    this.student = null;
  }

  /**
   * Returns the child node for the given character.
   *
   * @param c the character
   * @return an Optional containing the child node if it exists, or empty otherwise
   */
  Optional<TrieNode> getChild(char c) {
    return Optional.ofNullable(children.get(Character.toLowerCase(c)));
  }

  /**
   * Creates and returns a child node for the given character if it doesn't exist.
   *
   * @param c the character
   * @return the child node (existing or newly created)
   */
  TrieNode getOrCreateChild(char c) {
    return children.computeIfAbsent(Character.toLowerCase(c), k -> new TrieNode());
  }

  /**
   * Returns the student stored at this node.
   *
   * @return an Optional containing the student if this node represents the end of a n, or empty
   *     otherwise
   */
  Stud getStudent() {
    if (student == null) {
      throw new NullPointerException("Asked for student, but not available");
    }
    return student;
  }

  /**
   * Stores a student at this node, marking this as the end of a n.
   *
   * @param student the student to store
   */
  void setStudent(Stud student) {
    this.student = student;
  }

  /**
   * Checks if this node has a student stored.
   *
   * @return true if a student is stored at this node, false otherwise
   */
  boolean hasStudent() {
    return student != null;
  }

  /**
   * Returns a map of all child nodes.
   *
   * @return the children map
   */
  Map<Character, TrieNode> getChildren() {
    return children;
  }
}
