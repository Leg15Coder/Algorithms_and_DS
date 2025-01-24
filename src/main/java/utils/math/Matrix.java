package utils.math;

import structures.common.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static utils.math.Basic.pow;

public class Matrix {
  private final Double[][] matrix;
  private final Pair<Integer, Integer> size;
  private boolean isChanged = true;
  private Double determinant = null;

  public Matrix(Double[][] matrix) {
    this.matrix = matrix;
    this.size = new Pair<>(matrix.length, matrix[0].length);

    if (size.first <= 0 || size.second <= 0) {
      throw new IllegalArgumentException("Размер матрицы должен быть невырожденным");
    }
  }

  public Double getAt(int row, int col) {
    return matrix[row - 1][col - 1];
  }

  public Matrix sum(Matrix other) {
    if (!size.equals(other.size)) {
      throw new IllegalArgumentException("Размеры матриц должны совпадать");
    }

    Double[][] newMatrix = new Double[size.first][size.second];
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        newMatrix[i][j] = matrix[i][j] + other.matrix[i][j];
      }
    }
    
    return new Matrix(newMatrix);
  }
  
  public void isum(Matrix other) {
    if (!size.equals(other.size)) {
      throw new IllegalArgumentException("Размеры матриц должны совпадать");
    }
    
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        this.matrix[i][j] += other.matrix[i][j];
      }
    }

    isChanged = true;
  }

  public Matrix sub(Matrix other) {
    if (!size.equals(other.size)) {
      throw new IllegalArgumentException("Размеры матриц должны совпадать");
    }

    Double[][] newMatrix = new Double[size.first][size.second];
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        newMatrix[i][j] = matrix[i][j] - other.matrix[i][j];
      }
    }

    return new Matrix(newMatrix);
  }

  public void isub(Matrix other) {
    if (!size.equals(other.size)) {
      throw new IllegalArgumentException("Размеры матриц должны совпадать");
    }

    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        this.matrix[i][j] -= other.matrix[i][j];
      }
    }

    isChanged = true;
  }

  public Matrix mul(Number other) {
    Double[][] newMatrix = new Double[size.first][size.second];
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        newMatrix[i][j] = matrix[i][j] * other.doubleValue();
      }
    }

    return new Matrix(newMatrix);
  }

  public void imul(Number other) {
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        this.matrix[i][j] *= other.doubleValue();
      }
    }

    isChanged = true;
  }
  
  public Matrix mul(Matrix other) {
    if (!size.second.equals(other.size.first)) {
      throw new IllegalArgumentException("Количество столбцов левой матрицы должно равняться количеству строк правой матрицы");
    }

    Double[][] newMatrix = new Double[size.first][other.size.second];
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < other.size.second; ++j) {
        newMatrix[i][j] = 0D;
        for (int k = 0; k < size.second; ++k) {
          newMatrix[i][j] += matrix[i][k] * other.matrix[k][j];
        }
      }
    }

    return new Matrix(newMatrix);
  }
  
  public Matrix transpose() {
    return T();
  }
  
  public Matrix T() {
    Double[][] newMatrix = new Double[size.second][size.first];
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        newMatrix[i][j] = matrix[j][i];
      }
    }

    return new Matrix(newMatrix);
  }
  
  public Double det() {
    if (isChanged) {
      this.determinant = recursiveDet();
      isChanged = false;
    }

    return determinant;
  }

  private Double recursiveDet() {
    if (!isSquareMatrix()) {
      throw new IllegalStateException("Детерминант имеют только квадратные матрицы");
    }

    if (size.first == 1) {
      return matrix[0][0];
    }

    double result = 0L;
    for (int k = 0; k < size.first; ++k) {
      result += matrix[0][k] * AlgebraicComplement(0, k);
    }

    return result;
  }
  
  public Double AlgebraicComplement(int row, int col) {
    return pow(-1, row + col) * Minor(row, col);
  }
  
  public Double Minor(long row, long col) {
    if (!isSquareMatrix()) {
      throw new IllegalStateException("Детерминант имеют только квадратные матрицы");
    }
    --row;
    --col;

    Double[][] newMatrix = new Double[size.first - 1][size.second - 1];
    int difI;
    int difJ;
    
    for (int i = 0; i < size.first; ++i) {
      if (i != row) {
        for (int j = 0; j < size.second; ++j) {
          if (j != col) {
            difI = i >= row ? -1 : 0;
            difJ = j >= row ? -1 : 0;
            
            newMatrix[i][j] = matrix[i + difI][j + difJ];
          }
        }
      }
    }

    return new Matrix(newMatrix).det();
  }

  public Pair<Integer, Integer> getSize() {
    return size;
  }
  
  public Matrix reverse() {
    if (!isSquareMatrix()) {
      throw new IllegalStateException("Обратную матрицу можно найти только для квадратной матрицы");
    }

    Double[][] newMatrix = new Double[size.first][size.second];
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        newMatrix[i][j] = matrix[j][i] / det();
      }
    }

    return new Matrix(newMatrix);
  }

  public Matrix neutral() {
    Double[][] newMatrix = new Double[size.first][size.second];
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        newMatrix[i][j] = (i == j ? 1D : 0D);
      }
    }

    return new Matrix(newMatrix);
  }

  public Matrix zero() {
    Double[][] newMatrix = new Double[size.first][size.second];
    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        newMatrix[i][j] = 0D;
      }
    }

    return new Matrix(newMatrix);
  }

  public boolean isSquareMatrix() {
    return !size.first.equals(size.second);
  }

  public boolean isDegenerateMatrix() {
    return det() == 0D;
  }

  public ArrayList<Double> solveAsLinearSystem() {
    Double[][] freeMembersVec = new Double[1][size.first];

    for (int i = 0; i < size.first; ++i) {
      freeMembersVec[0][i] = 0D;
    }
    Matrix freeMembers = new Matrix(freeMembersVec);

    return solveAsLinearSystem(freeMembers);
  }

  public ArrayList<Double> solveAsLinearSystem(Matrix freeMembers) {
    if (!isSquareMatrix()) {
      throw new IllegalArgumentException("Недостаточно уравнений или слишком много переменных");
    }

    if (freeMembers.size.first != 1) {
      freeMembers = freeMembers.T();
    }
    if (freeMembers.size.first != 1 || !freeMembers.size.second.equals(size.second)) {
      throw new IllegalArgumentException("Недостаточно свободных членов для решения системы");
    }

    if (isDegenerateMatrix()) {
      return null;
    }

    ArrayList<Double> result = new ArrayList<>();

    for (int i = 0; i < size.first; ++i) {
      Double[][] tmpMatrix = matrix.clone();
      for (int j = 0; j < size.second; ++j) {
        tmpMatrix[j][i] = freeMembers.getAt(0, j);
      }

      Matrix newMatrix = new Matrix(tmpMatrix);
      result.add(newMatrix.det() / det());
    }

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Matrix matrix1 = (Matrix) o;
    return Objects.deepEquals(matrix, matrix1.matrix) && Objects.equals(getSize(), matrix1.getSize());
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.deepHashCode(matrix), getSize());
  }
  
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < size.first; ++i) {
      for (int j = 0; j < size.second; ++j) {
        result.append(matrix[i][j]).append(" ");
      }
      result.append("\n");
    }
    
    return result.toString();
  }
}
