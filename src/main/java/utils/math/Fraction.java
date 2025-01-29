package utils.math;

import java.util.Objects;

import static utils.math.Simples.gcd;
import static utils.math.Simples.lcm;

public class Fraction extends Number implements Comparable<Number> {
  private long numerator;
  private long denominator;

  public Fraction(long numerator, long denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  public Fraction(Number num) {
    this.numerator = num.longValue();
    this.denominator = 1;
  }

  public Fraction sum(Number other) {
    return this.sum(new Fraction(other));
  }

  public void isum(Number other) {
    this.isum(new Fraction(other));
  }

  public Fraction sub(Number other) {
    return this.sub(new Fraction(other));
  }

  public void isub(Number other) {
    this.isub(new Fraction(other));
  }

  public Fraction mul(Number other) {
    return this.mul(new Fraction(other));
  }

  public void imul(Number other) {
    this.imul(new Fraction(other));
  }

  public Fraction div(Number other) {
    return this.div(new Fraction(other));
  }

  public void idiv(Number other) {
    this.idiv(new Fraction(other));
  }

  public Fraction sum(Fraction other) {
    long newDenominator = lcm(this.denominator, other.denominator);
    long newNumerator = this.numerator * (newDenominator / this.denominator) + other.numerator * (newDenominator / other.denominator);
    return new Fraction(newNumerator, newDenominator);
  }

  public void isum(Fraction other) {
    long newDenominator = lcm(this.denominator, other.denominator);
    long newNumerator = this.numerator * (newDenominator / this.denominator) + other.numerator * (newDenominator / other.denominator);
    long tmp = gcd(newNumerator, newDenominator);
    this.numerator = newNumerator / tmp;
    this.denominator = newDenominator / tmp;
  }

  public Fraction sub(Fraction other) {
    long newDenominator = lcm(this.denominator, other.denominator);
    long newNumerator = this.numerator * (newDenominator / this.denominator) - other.numerator * (newDenominator / other.denominator);
    return new Fraction(newNumerator, newDenominator);
  }

  public void isub(Fraction other) {
    long newDenominator = lcm(this.denominator, other.denominator);
    long newNumerator = this.numerator * (newDenominator / this.denominator) - other.numerator * (newDenominator / other.denominator);
    long tmp = gcd(newNumerator, newDenominator);
    this.numerator = newNumerator / tmp;
    this.denominator = newDenominator / tmp;
  }

  public Fraction mul(Fraction other) {
    long thisNumTmp = gcd(this.numerator, other.denominator);
    long thisDeNumTmp = gcd(this.denominator, other.numerator);
    return new Fraction(
        this.numerator / thisNumTmp * other.numerator,
        this.denominator / thisDeNumTmp * other.denominator
    );
  }

  public void imul(Fraction other) {
    long thisNumTmp = gcd(this.numerator, other.denominator);
    long thisDeNumTmp = gcd(this.denominator, other.numerator);
    long newNum = this.numerator / thisNumTmp * other.numerator;
    long newDeNum = this.denominator / thisDeNumTmp * other.denominator;
    long tmp = gcd(newDeNum, newNum);
    this.denominator = newDeNum / tmp;
    this.numerator = newNum / tmp;
  }

  public Fraction div(Fraction other) {
    long thisNumTmp = gcd(this.numerator, other.numerator);
    long thisDeNumTmp = gcd(this.denominator, other.denominator);
    return new Fraction(
        this.numerator / thisNumTmp * other.denominator,
        this.denominator / thisDeNumTmp * other.numerator
    );
  }

  public void idiv(Fraction other) {
    long thisNumTmp = gcd(this.numerator, other.numerator);
    long thisDeNumTmp = gcd(this.denominator, other.denominator);
    long newNum = this.numerator / thisNumTmp * other.denominator;
    long newDeNum = this.denominator / thisDeNumTmp * other.numerator;
    long tmp = gcd(newDeNum, newNum);
    this.denominator = newDeNum / tmp;
    this.numerator = newNum / tmp;
  }

  @Override
  public int intValue() {
    return (int) floatValue();
  }

  @Override
  public long longValue() {
    return (long) doubleValue();
  }

  @Override
  public float floatValue() {
    return 1.0F * numerator / denominator;
  }

  @Override
  public double doubleValue() {
    return 1.0D * numerator / denominator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Fraction fraction = (Fraction) o;
    return numerator == fraction.numerator && denominator == fraction.denominator;
  }

  @Override
  public int hashCode() {
    return Objects.hash(numerator, denominator);
  }

  @Override
  public int compareTo(Number o) {
    return Double.compare(1.0D * numerator / denominator, o.doubleValue());
  }
}
