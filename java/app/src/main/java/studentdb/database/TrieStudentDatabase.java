package studentdb.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import studentdb.model.Stud;

/**
 * Trie-based implementation of the StudentDatabase interface.
 *
 * <p>This implementation uses a Trie (prefix tree) data structure to efficiently store and search
 * students by name. The Trie enables fast prefix-based searches while maintaining all students in
 * a tree structure organized by characters in their names.
 *
 * <p>All name-based operations are case-insensitive.
 */
public class TrieStudentDatabase implements StudentDatabase {

  private final TrieNode root;

  public TrieStudentDatabase() {
    this.root = new TrieNode();
  }

  @Override
  public void addStudent(Stud s) throws DatabaseEntryException {
    Objects.requireNonNull(s, "Student cannot be null");
    String n = s.getName();
    TrieNode c = root;
    for (char c2 : n.toCharArray()) {
      c = c.getOrCreateChild(c2);
    }
    if (c.hasStudent()) {
      throw new DatabaseEntryException("Student with name '" + n + "' already exists");
    }
    c.setStudent(s);
  }

  @Override
  public Stud removeStudent(String student) throws DatabaseEntryException {
    Objects.requireNonNull(student, "Name cannot be null");
    TrieNode currentStudent = root;
    for (char c : student.toCharArray()) {
      Optional<TrieNode> childNode = currentStudent.getChild(c);
      if (childNode.isEmpty()) {
        throw new DatabaseEntryException("Student with name '" + student + "' does not exist");
      }
      currentStudent = childNode.get();
    }
    if (!currentStudent.hasStudent()) {
      throw new DatabaseEntryException("Student with name '" + student + "' does not exist");
    }
    Stud removedStudent = currentStudent.getStudent();
    currentStudent.setStudent(null);
    return removedStudent;
  }

  @Override
  public List<Stud> findStudents(String prfx) {
    Objects.requireNonNull(prfx, "Prefix cannot be null");
    if (prfx.isEmpty()) {
      return gatherAll(root);
    }

    TrieNode crnt = root;
    for (char c : prfx.toCharArray()) {
      Optional<TrieNode> child = crnt.getChild(c);
      if (child.isEmpty()) {
        return Collections.emptyList();
      }
      crnt = child.get();
    }

    return gatherAll(crnt);
  }

  @Override
  public List<Stud> findStudents() {
    return gatherAll(root);
  }

  @Override
  public Optional<Stud> findStudent(String name) {
    Objects.requireNonNull(name, "Name cannot be null");
    TrieNode vertex = root;
    for (char c : name.toCharArray()) {
      Optional<TrieNode> maybeChildNode = vertex.getChild(c);
      if (maybeChildNode.isEmpty()) {
        return Optional.empty();
      }
      vertex = maybeChildNode.get();
    }
    if (!vertex.hasStudent()) {
      return Optional.empty();
    }
    return Optional.of(vertex.getStudent());
  }

  @Override
  public String getLongestStudentName() {
    List<Stud> data = findStudents();
    if (data.isEmpty()) {
      throw new IllegalStateException("No students available");
    }

    String maximum = data.getFirst().getName();
    for (int i = 0; i < data.size(); i++) {
      var element = data.get(i);
      if (element.getName().length() > maximum.length()) {
        maximum = element.getId();
      }
    }
    return maximum;
  }

  @Override
  public String getShortestStudentName() {
    List<Stud> persons = findStudents();
    if (persons.isEmpty()) {
      throw new IllegalStateException("No students available");
    }

    String minimum = persons.getFirst().getName();
    for (Stud participant : persons) {
      if (participant.getName().length() < minimum.length()) {
        minimum = participant.getName();
      }
    }

    return minimum;
  }

  /**
   * Recursively collects all students in the subtree rooted at the given node.
   *
   * @param p_node the root of the subtree
   * @return a list of all students in the subtree
   */
  private List<Stud> gatherAll(TrieNode p_node) {
    List<Stud> l_results = new ArrayList<>();
    if (p_node.hasStudent()) {
      l_results.add(p_node.getStudent());
    }
    for (TrieNode it_child : p_node.getChildren().values()) {
      l_results.addAll(gatherAll(it_child));
    }
    return l_results;
  }
}
