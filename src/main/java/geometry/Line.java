package geometry;

public class Line {
  private final Point p0;
  private final Vector guide;

  public Line(double a, double b, double c) {
    this.guide = (new Vector(a, b)).normalize();
    if (b != 0) {
      this.p0 = new Point(0, -c / b);
    } else if (a != 0) {
      this.p0 = new Point(-c / a, 0);
    } else {
      throw new IllegalArgumentException("Это не прямая");
    }
  }

  public Line(Point p1, Point p2) {
    this.p0 = p1;
    this.guide = new Vector(p1, p2);
  }

  public Line(Point p0, Vector guide) {
    this.p0 = p0;
    this.guide = guide;
  }

  public Line(Ray ray) {
    this.p0 = ray.getTop();
    this.guide = ray.getGuide();
  }

  public Line(Line ln) {
    this.p0 = ln.p0;
    this.guide = ln.guide;
  }

  public Line(Segment seg) {
    this.p0 = seg.getTops().first;
    this.guide = seg.toVector();
  }

  public boolean isOnLine(Point p) {
    return guide.isCollinear(new Vector(p0, p));
  }

  public boolean isParallel(Line ln) {
    return guide.isCollinear(ln.guide);
  }

  public Point intersection(Point p) {
    if (isOnLine(p)) {
      return p;
    }
    return null;
  }

  public Point intersection(Line ln) {
    if (isParallel(ln)) {
      return null;
    }
    // x1 + at1 = x2 + ct2; t1 = (x2 - x1 + ct2) / a; t2 = (x1 - x2 + at1) / c
    // y1 + bt1 = y2 + dt2; t1 = (y2 - y1 + dt2) / c; t2 = (y1 - y2 + ct1) / d
    double delta = -guide.x * ln.guide.y + guide.y * ln.guide.x;
    if (delta == 0) {
      return null;
    }

    double deltax = -(ln.p0.x - p0.x) * ln.guide.y + (ln.p0.y - p0.y) * ln.guide.x;
    double deltay = -guide.x * (ln.p0.y - p0.y) + guide.y * (ln.p0.x - p0.x);

    double t1 = deltax / delta;
    double t2 = deltay / delta;

    return new Point(
        p0.x + t1 * guide.x,
        p0.y + t2 * guide.y
    );
  }

  public Vector normalVector() {
    return guide.normalize();
  }

  public Vector guidingVector() {
    return guide;
  }
}
