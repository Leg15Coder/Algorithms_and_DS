package geometry;

import static java.lang.Math.*;

public class Vector {
  public final Double x;
  public final Double y;

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector(Vector vec) {
    this.x = vec.x;;
    this.y = vec.y;
  }

  public Vector(Point p1, Point p2) {
    this.x = p2.x - p1.x;;
    this.y = p2.y - p1.y;
  }

  public double length() {
    return sqrt(x * x + y * y);
  }

  public Vector sum(Vector vec) {
    return new Vector(
        x + vec.x,
        y + vec.y
    );
  }

  public Vector sub(Vector vec) {
    return new Vector(
        x - vec.x,
        y - vec.y
    );
  }

  public Vector reverse() {
    return new Vector(
        -x,
        -y
    );
  }

  public Vector conjugate() {
    return new Vector(
        x,
        -y
    );
  }

  public Vector normalize() {
    return new Vector(
        -y,
        x
    );
  }

  public double scalar(Vector vec) {
    return x * vec.x + y * vec.y;
  }

  public double vector(Vector vec) {
    return x * vec.y - y * vec.x;
  }

  public double angle(Vector vec) {
    return acos((vector(vec)) / (length() * vec.length()));
  }

  public double typeOfAngle(Vector vec) {
    return (vector(vec)) / (length() * vec.length());
  }

  public double minAngle(Vector vec) {
    return acos(abs((vector(vec)) / (length() * vec.length())));
  }

  public boolean isCollinear(Vector vec) {
    return x * vec.y == y * vec.x;
  }

  public boolean isCoDirectional(Vector vec) {
    return isCollinear(vec) && (x * vec.x >= 0 && y * vec.y >= 0);
  }
}
