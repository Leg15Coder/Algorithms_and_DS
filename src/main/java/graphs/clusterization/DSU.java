package graphs.clusterization;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class DSU<T> {
  private class VertexState {
    public List<VertexState> children = new LinkedList<>();
    public List<VertexState> notLeafChildren = new LinkedList<>();
    VertexState parent;
    T value;
    public int height;
    public int rank;

    public VertexState(T value, int height, int rank) {
      this.value = value;
      this.height = height;
      this.rank = rank;
    }
  }

  private class SetState {
    public VertexState root;
    public List<VertexState> notLeafs = new LinkedList<>();
    public List<VertexState> dfsList = new LinkedList<>();

    public SetState(VertexState root) {
      this.root = root;
    }

    public int size() {
      return dfsList.size();
    }
  }

  private static final int MIN_FULL_VERTEX_SIZE = 4;
  private final Map<T, SetState> sets = new HashMap<>();
  private int selfSize = 0;

  public void addSet(Iterable<T> elements) {}

  public void addSet(T element) {
    VertexState newVertexState = new VertexState(element, 0, 0);
    newVertexState.parent = newVertexState;
    newVertexState.children = new LinkedList<>();
    newVertexState.notLeafChildren = new LinkedList<>();

    SetState newSet = new SetState(newVertexState);
    newSet.dfsList.add(newVertexState);

    sets.put(element, newSet);
    selfSize++;
  }

  private void unionSmallSets(SetState smallSet, SetState otherSet) {
    for (VertexState v : smallSet.dfsList) {
      v.parent = otherSet.root;
      v.rank = 0;
      otherSet.root.rank = Math.max(otherSet.root.rank, 1);

      otherSet.dfsList.addAll(smallSet.dfsList);
      otherSet.dfsList.addAll(smallSet.notLeafs);

      sets.put(v.value, otherSet);
    }
  }

  public void unionByElements(T firstRoot, T secondRoot) {
    SetState firstSet = sets.get(firstRoot);
    SetState secondSet = sets.get(secondRoot);

    if (firstSet == secondSet) {
      return;
    }

    if (firstSet.size() < MIN_FULL_VERTEX_SIZE) {
      unionSmallSets(firstSet, secondSet);
      return;
    }

    if (secondSet.size() < MIN_FULL_VERTEX_SIZE) {
      unionSmallSets(secondSet, firstSet);
      return;
    }

    if (firstSet.root.rank < secondSet.root.rank) {
      SetState tmp = firstSet;
      firstSet = secondSet;
      secondSet = tmp;
    }

    secondSet.root.parent = firstSet.root;
    if (firstSet.root.rank == secondSet.root.rank) {
      firstSet.root.rank++;
    }

    firstSet.root.notLeafChildren.add(0, secondSet.root);
    firstSet.root.children.add(0, secondSet.root);

    for (VertexState v : secondSet.dfsList) {
      firstSet.dfsList.add(1, v);
      sets.put(v.value, firstSet);
    }
  }

  public Set<T> findSetByElement(T element) {
    return new HashSet<>(
        sets.getOrDefault(element, new SetState(null))
            .dfsList
            .stream()
            .map(state -> state.value)
            .toList());
  }

  private void reLink(VertexState current) {
    SetState curSet = sets.get(current.value);
    Queue<VertexState> queue = new ArrayDeque<>();
    queue.add(current);

    while (!queue.isEmpty()) {
      current = queue.remove();
      VertexState parent = current.parent;
      VertexState grandParent = parent.parent;

      parent.children.remove(current.value);
      int parentIndex = grandParent.children.indexOf(parent.value);
      boolean isLast = parentIndex == grandParent.children.size() - 1;
      grandParent.children.add(parentIndex + 1, current);

      if (!isLast) {
        int parentIndexInDFS = curSet.dfsList.indexOf(parent.value);
        int currentIndex = curSet.dfsList.indexOf(current.value);
        T parentsRightNeighbour = grandParent.children.get(parentIndex + 1).value;
        int parentsRightNeighbourIndex = curSet.dfsList.indexOf(parentsRightNeighbour);
        for (int i = currentIndex; i < parentsRightNeighbourIndex; ++i) {
          curSet.dfsList.add(parentIndexInDFS, curSet.dfsList.remove(currentIndex));
        }
      }

      if (parent.children.size() < MIN_FULL_VERTEX_SIZE - 1) {
        queue.addAll(parent.children);
      }
    }

    if (current.parent.children.isEmpty()) {
      curSet.notLeafs.remove(current.parent.value);
      current.parent.rank = 0;

      if (curSet.notLeafs.isEmpty()) {
        curSet.root.rank = 1;
      }
    }
  }

  public boolean isInSameSet(T first, T second) {
    return sets.get(first) == sets.get(second);
  }

  public boolean isInSameSet(Iterable<T> elements) {
    T fixed = elements.iterator().next();
    boolean result = true;

    while (elements.iterator().hasNext()) {
      result = result && isInSameSet(fixed, elements.iterator().next());
    }

    return result;
  }

  public void delete(T element) {}

  public int getSize() {
    return selfSize;
  }

  public int getSetsCount() {
    return sets.size();
  }
}
