package geometry;

import structures.common.Pair;

public class Segment {
  private final Point left;
  private final Point right;

  public Segment(Point p1, Point p2) {
    if (p1.x < p2.x || p1.x.equals(p2.x) && p1.y < p2.y) {
      this.left = p1;
      this.right = p2;
    } else {
      this.left = p2;
      this.right = p1;
    }
  }

  public Vector toVector() {
    return new Vector(
        left,
        right
    );
  }

  public Pair<Point, Point> getTops() {
    return new Pair<>(left, right);
  }

  public boolean isOnSegment(Point p) {
    return (new Ray(left, right)).isOnRay(p) && (new Ray(right, left)).isOnRay(p);
  }
}
