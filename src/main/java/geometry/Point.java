package geometry;

import structures.common.Pair;
import static utils.Compare.min;

public class Point {
  public final Double x;
  public final Double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Point(Vector vec) {
    this.x = vec.x;;
    this.y = vec.y;
  }

  public Point sum(Vector vec) {
    return new Point(
        x + vec.x,
        y + vec.y
    );
  }

  public Point sub(Vector vec) {
    return new Point(
        x - vec.x,
        y - vec.y
    );
  }

  public Point sum(Point p) {
    return new Point(
        x + p.x,
        y + p.y
    );
  }

  public Point sub(Point p) {
    return new Point(
        x - p.x,
        y - p.y
    );
  }

  public Point reverse() {
    return new Point(
        -x,
        -y
    );
  }

  public double dist(Point p) {
    return (new Vector(this, p)).length();
  }

  public double dist(Line ln) {
    Vector normal = ln.normalVector();
    Line tmpLine = new Line(this, normal);
    Point intersection = ln.intersection(tmpLine);
    return dist(intersection);
  }

  public double dist(Ray ray) {
    Line rayLine = new Line(ray);
    Vector normal = rayLine.normalVector();
    Line pointLine = new Line(this, normal);
    Point intersection = rayLine.intersection(pointLine);

    if (ray.isOnRay(intersection)) {
      return dist(intersection);
    }
    return dist(ray.getTop());
  }

  public double dist(Segment seg) {
    Line segLine = new Line(seg);
    Vector normal = segLine.normalVector();
    Line pointLine = new Line(this, normal);
    Point intersection = segLine.intersection(pointLine);

    if (seg.isOnSegment(intersection)) {
      return dist(intersection);
    }
    Pair<Point, Point> tops = seg.getTops();

    return min(dist(tops.first), dist(tops.second));
  }
}
