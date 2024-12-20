import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Exception {
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer tokenizer = new StringTokenizer(buffer.readLine());
    int amount = Integer.parseInt(tokenizer.nextToken());
    int length = Integer.parseInt(tokenizer.nextToken());
    List<List<Long>> array = new ArrayList<>();
    for (int j = 0; j < amount; j++) {
      tokenizer = new StringTokenizer(buffer.readLine());

      array.add(new ArrayList<>());
      for (int i = 0; i < length; i++) {
        array.get(array.size() - 1).add(Long.parseLong(tokenizer.nextToken()));
      }
    }

    List<Long> result = mergeAll(array);

    BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
    for (Long element : result) {
      log.write(element + " ");
    }
    log.flush();
  }

  static List<Long> mergeAll(List<List<Long>> arrayOfArrays) {
    while (arrayOfArrays.size() > 1) {
      List<List<Long>> newArrayOfArrays = new ArrayList<>();

      for (int i = 1; i < arrayOfArrays.size(); i+=2) {
        newArrayOfArrays.add(mergeTwoArrays(arrayOfArrays.get(i - 1), arrayOfArrays.get(i)));
      }

      if (arrayOfArrays.size() % 2 == 1) {
        newArrayOfArrays.add(arrayOfArrays.get(arrayOfArrays.size() - 1));
      }

      arrayOfArrays = newArrayOfArrays;
    }

    return arrayOfArrays.get(0);
  }

  static List<Long> mergeTwoArrays(List<Long> leftArray, List<Long> rightArray) {
    List<Long> resultArray = new ArrayList<>();

    int i = 0, j = 0;

    while (i < leftArray.size() && j < rightArray.size()) {
      if (leftArray.get(i) <= rightArray.get(j)) {
        resultArray.add(leftArray.get(i++));
      } else {
        resultArray.add(rightArray.get(j++));
      }
    }
    while (i < leftArray.size()) {
      resultArray.add(leftArray.get(i++));
    }
    while (j < rightArray.size()) {
      resultArray.add(rightArray.get(j++));
    }

    return resultArray;
  }
}
