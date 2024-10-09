package org.example;

import memory.manager.MemoryManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Exception {
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer BaseInputTokenizer = new StringTokenizer(buffer.readLine());
    int MaxLength = Integer.parseInt(BaseInputTokenizer.nextToken());
    int amount = Integer.parseInt(BaseInputTokenizer.nextToken());
    BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
    MemoryManager memoryManager = new MemoryManager(MaxLength, amount);
    for (int i = 1; i <= amount; i++) {
      StringTokenizer tokenizer = new StringTokenizer(buffer.readLine());
      int request = Integer.parseInt(tokenizer.nextToken());
      if (request > 0) {
        long result = memoryManager.allocate(request);
        log.write(result + "\n");
      } else {
        memoryManager.free(-request);
      }
    }
    log.flush();
  }
}
