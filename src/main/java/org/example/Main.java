package org.example;

import memory.manager.MemoryManager;
import java.util.Scanner;


public class Main {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    int MaxLength = input.nextInt();
    int amount = input.nextInt();
    MemoryManager memoryManager = new MemoryManager(MaxLength, amount);
    for (int i = 1; i <= amount; i++) {
      int request = input.nextInt();
      if (request > 0) {
        long result = memoryManager.allocate(request);
        System.out.println(result);
      } else {
        memoryManager.free(-request);
      }
    }
  }
}
