import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
  /**
   * Интерфейс, определяющий операции двоичного дерева поиска.
   * @param <T> тип элементов, хранимых в дереве, который должен быть сравнимым.
   */
  public interface BinarySearchTreeInterface<T extends Comparable<T>> {
    /**
     * Добавляет новый узел с заданным ключом и значением в дерево.
     * Если узел с таким ключом уже существует, он заменяется новым.
     *
     * @param key ключ узла.
     * @param value значение, связанное с ключом.
     */
    void add(T key, T value);

    /**
     * Удаляет значение из дерева.
     * @param value значение для удаления.
     * @return true, если значение было успешно удалено, иначе false.
     */
    boolean remove(T value);

    /**
     * Проверяет наличие значения в дереве.
     * @param value значение для поиска.
     * @return true, если значение существует, иначе false.
     */
    boolean get(T value);

    /**
     * Возвращает минимальное значение в дереве.
     * @return минимальное значение в дереве или null, если дерево пустое.
     */
    T getMin();

    /**
     * Возвращает максимальное значение в дереве.
     * @return максимальное значение в дереве или null, если дерево пустое.
     */
    T getMax();

    /**
     * Проверяет, пусто ли дерево.
     * @return true, если дерево пустое, иначе false.
     */
    boolean isEmpty();

    /**
     * Очищает дерево, удаляя все элементы.
     */
    void clear();
  }

  /**
   * Реализация двоичного дерева поиска с использованием дерева Splay.
   * @param <T> тип элементов, хранимых в дереве, который должен быть сравнимым.
   */
  public static class SplayTree<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
    Node root = null; // Корень дерева Splay

    protected class Node {
      Node left = null; // Левый потомок узла
      Node right = null; // Правый потомок узла
      T key; // Ключ узла
      T value; // Значение, связанное с ключом

      /**
       * Конструктор узла с заданным ключом и значением.
       * @param key ключ узла.
       * @param value значение, связанное с ключом.
       */
      public Node(T value, T key) {
        this.key = value;
        this.value = key;
      }

      public void setLeft(Node left) {
        this.left = left;
      }

      public void setRight(Node right) {
        this.right = right;
      }

      public Node getLeft() {
        return this.left;
      }

      public Node getRight() {
        return this.right;
      }

      public T getKey() {
        return this.key;
      }

      public T getValueFromKey() {
        return this.value;
      }
    }

    /**
     * Выполняет операцию Splay, поднимая узел с заданным ключом к корню дерева.
     * @param cur текущий узел.
     * @param key ключ для поднятия.
     * @return новый корень после выполнения операции.
     */
    private Node splay(Node cur, T key) {
      if (cur == null) {
        return null;
      }
      if (cur.getKey().compareTo(key) > 0) {
        if (cur.getLeft() == null) {
          return cur;
        }
        if (cur.getLeft().getKey().compareTo(key) > 0) {
          cur = zigZig(cur, key, true);
        } else if (cur.getLeft().getKey().compareTo(key) < 0) {
          cur = zigZag(cur, key, true);
        }
        if (cur.getLeft() == null) {
          return cur;
        }
        return zig(cur, false);
      }
      if (cur.getKey().compareTo(key) < 0) {
        if (cur.getRight() == null) {
          return cur;
        }
        if (cur.getRight().getKey().compareTo(key) > 0) {
          cur = zigZag(cur, key, false);
        } else if (cur.getRight().getKey().compareTo(key) < 0) {
          cur = zigZig(cur, key, false);
        }
        if (cur.getRight() == null) {
          return cur;
        }
        return zig(cur, true);
      }
      return cur;
    }

    /**
     * Выполняет поворот дерева вокруг текущего узла в заданном направлении.
     * @param cur текущий узел.
     * @param left true для левого поворота, false для правого поворота.
     * @return новый корень после поворота.
     */
    private Node zig(Node cur, boolean left) {
      if (left) {
        Node tmp = cur.getRight();
        cur.setRight(tmp.getLeft());
        tmp.setLeft(cur);
        return tmp;
      }
      Node tmp = cur.getLeft();
      cur.setLeft(tmp.getRight());
      tmp.setRight(cur);
      return tmp;
    }

    /**
     * Выполняет двойной поворот Zig-Zig вокруг текущего узла.
     * Используется, когда ключ находится в поддереве с той же стороны
     * два уровня подряд (например, левый-левый или правый-правый).
     *
     * @param cur текущий узел.
     * @param key ключ, для которого выполняется операция Zig-Zig.
     * @param left true, если ключ находится в левом поддереве, false — если в правом.
     * @return новый корень после выполнения операции Zig-Zig.
     */
    private Node zigZig(Node cur, T key, boolean left) {
      if (left) {
        cur.getLeft().setLeft(splay(cur.getLeft().getLeft(), key));
        return zig(cur, false);
      }
      cur.getRight().setRight(splay(cur.getRight().getRight(), key));
      return zig(cur, true);
    }

    /**
     * Выполняет двойной поворот Zig-Zag вокруг текущего узла.
     * Используется, когда ключ находится в поддереве с противоположной стороны
     * два уровня подряд (например, левый-правый или правый-левый).
     *
     * @param cur текущий узел.
     * @param key ключ, для которого выполняется операция Zig-Zag.
     * @param left true, если ключ находится в левом поддереве, false — если в правом.
     * @return новый корень после выполнения операции Zig-Zag.
     */
    private Node zigZag(Node cur, T key, boolean left) {
      if (left) {
        cur.getLeft().setRight(splay(cur.getLeft().getRight(), key));
        if (cur.getLeft().getRight() != null) {
          cur.setLeft(zig(cur.getLeft(), true));
        }
        return cur;
      }
      cur.getRight().setLeft(splay(cur.getRight().getLeft(), key));
      if (cur.getRight().getLeft() != null) {
        cur.setRight(zig(cur.getRight(), false));
      }
      return cur;
    }

    @Override
    public void add(T key, T value) {
      Node newNode = new Node(key, value);
      this.root = splay(this.root, key);

      if (isEmpty()) {
        this.root = newNode;
        return;
      }

      if (key.compareTo(root.getKey()) < 0) {
        newNode.setLeft(root.getLeft());
        newNode.setRight(root);
        this.root.setLeft(null);
      } else if (key.compareTo(root.getKey()) > 0) {
        newNode.setRight(root.getRight());
        newNode.setLeft(root);
        this.root.setRight(null);
      } else {
        newNode.setLeft(root.getLeft());
        newNode.setRight(root.getRight());
      }
      this.root = newNode;
    }

    @Override
    public boolean remove(T value) {
      this.root = splay(this.root, value);
      if (isEmpty() || !this.root.getKey().equals(value)) {
        return false;
      }
      if (root.getLeft() == null) {
        this.root = this.root.getRight();
      } else {
        Node right = root.getRight();
        this.root = root.getLeft();
        this.root = splay(this.root, getMax(root).getKey());
        this.root.setRight(right);
      }
      return true;
    }

    @Override
    public boolean get(T value) {
      return getAt(value) != null;
    }

    /**
     * Возвращает значение, связанное с заданным ключом, если оно присутствует в дереве.
     * Этот метод поднимает узел с заданным ключом к корню с помощью операции Splay.
     *
     * @param value ключ для поиска.
     * @return значение, связанное с ключом, или null, если ключ не найден в дереве.
     */
    public T getAt(T value) {
      this.root = splay(this.root, value);
      if (!isEmpty()) {
        return this.root.getValueFromKey();
      }
      return null;
    }

    @Override
    public T getMin() {
      if (isEmpty()) {
        return null;
      }
      return getMin(this.root).getKey();
    }

    private Node getMin(Node cur) {
      if (cur.getLeft() == null) {
        return cur;
      }
      return getMin(cur.getLeft());
    }

    @Override
    public T getMax() {
      if (isEmpty()) {
        return null;
      }
      return getMax(this.root).getKey();
    }

    private Node getMax(Node cur) {
      if (cur.getRight() == null) {
        return cur;
      }
      return getMax(cur.getRight());
    }

    @Override
    public boolean isEmpty() {
      return root == null;
    }

    @Override
    public void clear() {
      this.root = null;
    }
  }

  public static void main(String[] args) throws Exception {
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));

    StringTokenizer amountInputTokenizer = new StringTokenizer(buffer.readLine());
    int amount = Integer.parseInt(amountInputTokenizer.nextToken());
    // Создаем объект SplayTree, предполагается, что это структура данных, подобно словарю, работает с парами ключ-значение
    SplayTree<String> tree = new SplayTree<>();

    for (int i = 0; i < amount; ++i) {
      StringTokenizer line = new StringTokenizer(buffer.readLine());
      String first = line.nextToken();
      String second = line.nextToken();

      // Добавляем оба направления связи в дерево: first -> second и second -> first
      tree.add(first, second);
      tree.add(second, first);
    }

    StringTokenizer queriesInputTokenizer = new StringTokenizer(buffer.readLine());
    int queries = Integer.parseInt(queriesInputTokenizer.nextToken());

    for (int i = 0; i < queries; ++i) {
      StringTokenizer line = new StringTokenizer(buffer.readLine());
      String key = line.nextToken();

      // Получаем значение из дерева по ключу и записываем результат в лог
      log.write(tree.getAt(key));
      log.write("\n");
    }

    log.flush();
  }
}
