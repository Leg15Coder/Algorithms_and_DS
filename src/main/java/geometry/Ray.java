package geometry;

public class Ray { // todo
  private final Vector guide;
  private final Point top;

  public Ray(Point top, Point in) {
    this.top = top;
    this.guide = new Vector(top, in);
  }

  public Point getTop() {
    return this.top;
  }

  public Vector getGuide() {
    return this.guide;
  }

  public boolean isOnRay(Point p) {
    return guide.isCoDirectional(new Vector(top, p));
  }
}
