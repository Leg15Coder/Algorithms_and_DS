import static java.util.Collections.sort;

import java.io.*;
import java.util.*;

public class Main {
  /**
   * Метод нахождения двух минимальных значений из двух пар значений с игнорированием повторяющихся значений
   *
   * @param left  первая пара значений.
   * @param right вторая пара значений.
   * @return пара значений, содержащая два минимальных значения из всех входных значений.
   */
  public static <T extends Comparable<T>> Pair<T, T> min(
      Pair<T, T> left,
      Pair<T, T> right) {

    if (left == null && right == null) {
      return new Pair<>(null, null);
    }
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }

    List<T> toSort = new ArrayList<>();

    if (left.first != null
        && !(left.first.equals(right.first) || left.first.equals(right.second))) {
      toSort.add(left.first);
    }
    if (left.second != null
        && !(left.second.equals(right.first) || left.second.equals(right.second))) {
      toSort.add(left.second);
    }
    if (right.first != null) {
      toSort.add(right.first);
    }
    if (right.second != null) {
      toSort.add(right.second);
    }

    if (toSort.isEmpty()) {
      return new Pair<>(null, null);
    }
    if (toSort.size() == 1) {
      return new Pair<>(toSort.get(0), null);
    }
    sort(toSort);
    return new Pair<>(toSort.get(0), toSort.get(1));
  }

  /**
   * Вспомогательный класс для хранения пары объектов.
   *
   * @param <T> тип первого элемента.
   * @param <E> тип второго элемента.
   */
  public static class Pair<T, E> {
    public T first;
    public E second;

    /**
     * Конструктор для инициализации пары.
     *
     * @param first  первый элемент пары.
     * @param second второй элемент пары.
     */
    public Pair(T first, E second) {
      this.first = first;
      this.second = second;
    }
  }

  /**
   * Класс для представления и работы с SparseTable.
   * Реализует структуру данных для эффективного выполнения запросов 2-й порядковой статистики на целых числах
   */
  public static class SparseTable {

    /**
     * Вспомогательный класс для хранения индекса и значения элемента.
     */
    private static class LongWrapper implements Comparable<LongWrapper> {
      public Long index;
      public Long value;

      public LongWrapper(long index, long value) {
        this.value = value;
        this.index = index;
      }

      @Override
      public int compareTo(LongWrapper o) {
        return value.compareTo(o.value);
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongWrapper that = (LongWrapper) o;
        return Objects.equals(index, that.index) && Objects.equals(value, that.value);
      }

      @Override
      public int hashCode() {
        return Objects.hash(index, value);
      }
    }

    private final List<List<Pair<LongWrapper, LongWrapper>>> table = new ArrayList<>();
    private final List<Integer> logs = new ArrayList<>();

    public SparseTable(List<Long> array) {
      build(array);
      calculateLogs(array.size());
    }

    /**
     * Построение SparseTable.
     *
     * @param array массив значений для построения таблицы.
     */
    private void build(List<Long> array) {
      this.table.add(new ArrayList<>());
      for (int i = 0; i < array.size(); ++i) {
        this.table.get(0).add(new Pair<>(new LongWrapper(i, array.get(i)), null));
      }

      int offset = 1;
      while (offset < array.size()) {
        int index = 0;
        this.table.add(new ArrayList<>());

        while (index + offset < table.get(table.size() - 2).size()) {
          Pair<LongWrapper, LongWrapper> lastLevelLeft = table.get(table.size() - 2).get(index);
          Pair<LongWrapper, LongWrapper> lastLevelRight =
              table.get(table.size() - 2).get(index + offset);
          this.table.get(table.size() - 1).add(min(lastLevelLeft, lastLevelRight));
          index++;
        }

        offset <<= 1;
      }
    }

    /**
     * Вычисление логарифмов для оптимизации запросов.
     *
     * @param maxLog до какого логарифма проводить расчёт.
     */
    private void calculateLogs(int maxLog) {
      int log = 0;
      for (int k = 0; k <= maxLog; ++k) {
        while ((k > (1 << log)) && !(k < (1 << (log + 1)))) {
          log++;
        }
        this.logs.add(log);
      }
    }

    /**
     * Метод для нахождения второго минимального значения на отрезке.
     *
     * @param left  левая граница отрезка (1-индексация).
     * @param right правая граница отрезка (1-индексация).
     * @return второе минимальное значение на заданном отрезке.
     */
    public Long secondMinOnSegment(int left, int right) {
      left--;
      right--;

      int level = logs.get(right - left + 1);
      Pair<LongWrapper, LongWrapper> stats =
          min(table.get(level).get(left), table.get(level).get(right - (1 << level) + 1));

      assert stats != null;
      return stats.second.value;
    }

    @Override
    public String toString() {
      StringBuilder result = new StringBuilder();
      for (var arr : table) {
        for (var element : arr) {
          result.append("{");
          if (element.first == null) {
            result.append("null");
          } else {
            result.append(element.first.value);
          }
          result.append(" ");
          if (element.second == null) {
            result.append("null");
          } else {
            result.append(element.second.value);
          }
          result.append("} ");
        }
        result.append("\n");
      }
      return result.toString();
    }
  }

  public static void main(String[] args) throws IOException {
    Scanner input = new Scanner(System.in);
    BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));

    int lengthOfSequence = input.nextInt();
    int amountOfRanges = input.nextInt();

    List<Long> sequence = new ArrayList<>();

    for (int i = 0; i < lengthOfSequence; ++i) {
      sequence.add(input.nextLong());
    }

    SparseTable sparseTable = new SparseTable(sequence);

    for (int i = 0; i < amountOfRanges; ++i) {
      int from = input.nextInt();
      int to = input.nextInt();

      // Обработка запросов
      log.write(sparseTable.secondMinOnSegment(from, to) + "\n");
    }

    log.flush();
  }
}
