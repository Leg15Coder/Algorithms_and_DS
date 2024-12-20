import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  public record Part(long leftTime, long rightTime, long cord, long speed, long boost) {}

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    long currentSpeed = 0L;
    long currentDist = 0L;

    int countOfIntervals = input.nextInt();
    ArrayList<Part> intervals = new ArrayList<>();

    for (int i = 0; i < countOfIntervals; ++i) {
      long leftTime = input.nextLong();
      long rightTime = input.nextLong();
      long boost = input.nextLong();

      long time = rightTime - leftTime;

      intervals.add(new Part(leftTime, rightTime, currentDist, currentSpeed, boost));
      currentDist += 2L * time * currentSpeed + time * time * boost;
      currentSpeed += time * boost;
    }

    long left = intervals.get(countOfIntervals - 1).rightTime;
    intervals.add(countOfIntervals, new Part(left, left + 1L, currentDist, currentSpeed, 0L));
    int queries = input.nextInt();
    for (int i = 0; i < queries; ++i) {
      long dist = input.nextLong();
      System.out.println(getTimeFromDist(intervals, 2 * dist));
    }
  }

  public static long getTimeFromDist(ArrayList<Part> data, long dist) {
    long left = 0L;
    long right = data.size();

    while (right - left > 1) {
      long middle = left + (right - left) / 2;
      if (data.get((int) middle).cord <= dist) {
        left = middle;
      } else {
        right = middle;
      }
    }
    return getTimeFromDist(data.get((int) left), dist);
  }

  public static long getTimeFromDist(Part part, long dist) {
    long distDelta = dist - part.cord;
    if (part.boost != 0) {
      long descriptor = part.speed * part.speed + part.boost * distDelta;
      return ((long) sqrt(descriptor) - part.speed) / part.boost + part.leftTime;
    }

    return distDelta / (2 * part.speed) + part.leftTime;
  }

  public static long binarySearchOnParabola(Part part, long dist) { // old solve
    long left = part.leftTime;
    long right = part.rightTime;

    while (right - left > 1) {
      long middle = left + (right - left) / 2;
      long current =
          part.cord
              + 2 * (middle - part.leftTime) * part.speed
              + (middle - part.leftTime) * (middle - part.leftTime) * part.boost;

      if (current <= dist) {
        left = middle;
      } else {
        right = middle;
      }
    }

    return left;
  }
}
