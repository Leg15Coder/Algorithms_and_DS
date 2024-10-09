package tmp.tests;

import memory.manager.MemoryManager;

import java.util.Random;

public class StressTests {
   static Random rnd = new Random();

  public static void main(String[] args) {
    while (true) {
      int MaxLength = rnd.nextInt(1 << 31 - 1);
      int amount = rnd.nextInt(100_000);
      /*String[] input = new String[amount + 1];
      input[0] = MaxLength + " " + amount;*/
      MemoryManager memoryManager = new MemoryManager(MaxLength, amount);
      int request;
      for (int i = 1; i <= amount; i++) {
        if (i % 2 == 0 && i > 2) {
          request = rnd.nextInt(-(i - 1), -1);
        } else {
          request = rnd.nextInt(1 << 10);
        }
        if (request > 0) {
          long result = memoryManager.allocate(request);
          System.out.println(result);
        } else {
          memoryManager.free(-request);
        }
      }
    }
  }
}

