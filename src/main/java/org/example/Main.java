//package org.example;
//
//import structures.trees.search.AVLSet;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.util.*;
//
//public class Main {
//  public static <T extends Comparable<T>> T max(T a, T b) {
//    return (a.compareTo(b) > 0) ? a : b;
//  }
//
//  public static void main(String[] args) throws Exception {
//    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
//    BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
//
//    StringTokenizer defaultInputTokenizer = new StringTokenizer(buffer.readLine());
//    int amount = Integer.parseInt(defaultInputTokenizer.nextToken());
//    int Y = Integer.parseInt(defaultInputTokenizer.nextToken());
//    int X = Integer.parseInt(defaultInputTokenizer.nextToken());
//
//    List<RefillLine> refillLines = new ArrayList<>();
//
//    for (int i = 0; i < amount; ++i) {
//      StringTokenizer line = new StringTokenizer(buffer.readLine());
//      int x = Integer.parseInt(line.nextToken());
//      int start = Integer.parseInt(line.nextToken());
//      int end = Integer.parseInt(line.nextToken());
//
//      refillLines.add(new RefillLine(x, start, true));
//      refillLines.add(new RefillLine(x, end, false));
//    }
//
//    refillLines.sort((l, r) -> l.y != r.y ? Integer.compare(l.y, r.y) : l.start != r.start ? Boolean.compare(l.start, r.start) : Integer.compare(l.x, r.x));
//
//    Integer[] minFuel = new Integer[Y + 1];
//    Integer[] countRefills = new Integer[X];
//    Arrays.fill(minFuel, X);
//    Arrays.fill(countRefills, 0);
//    AVLSet<Integer> refillsSegments = new AVLSet<>();
//    Map<Integer, Integer> refillsStarts = new HashMap<>();
//
//    Segment godSegment = new Segment(0, X - 1);
//    refillsSegments.add(godSegment);
//    refillsStarts.put();
//    int lastY = 0;
//
//    for (var refillLine : refillLines) {
//      if (lastY != refillLine.y) {
//        minFuel[lastY] = refillsSegments.getMax().length;
//        for (int y = lastY + 1; y < refillLine.y; ++y) {
//          System.out.println(y);
//          minFuel[y] = minFuel[y - 1];
//        }
//      }
//
//      if (refillLine.start) {
//        if (countRefills[refillLine.x] == 0) {
//          Integer last = refillsSegments.previous(refillLine.x - 1);
//          Integer next = refillsSegments.next(refillLine.x + 1);
//
//          refillsStarts.put(last, refillLine.x - last);
//          refillsStarts.put(refillLine.x, next - refillLine.x);
//
//          Segment toRemove = refillsSegments.previous(refillLine.x);
//          Segment newSegment = new Segment(refillLine.x, toRemove.end);
//          Segment updatedSegment = new Segment(toRemove.start, refillLine.x);
//
//          refillsSegments.remove(toRemove);
//          refillsSegments.add(newSegment);
//          refillsSegments.add(updatedSegment);
//        }
//
//        countRefills[refillLine.x]++;
//      } else {
//        countRefills[refillLine.x]--;
//
//        if (countRefills[refillLine.x] == 0) {
//          Segment previousSegment = refillsSegments.previous(refillLine.x - 1);
//          Segment nextSegment = refillsSegments.next(refillLine.x - 1);
//          Segment mergedSegment = new Segment(previousSegment.start, nextSegment.end);
//
//          refillsSegments.remove(previousSegment);
//          refillsSegments.remove(nextSegment);
//          refillsSegments.add(mergedSegment);
//        }
//      }
//
//      lastY = refillLine.y;
//    }
//    minFuel[lastY] = refillsSegments.getMax().length;
//    for (int y = lastY + 1; y <= Y; ++y) {
//      minFuel[y] = minFuel[y - 1];
//    }
//
//    for (int i = 0; i <= Y; ++i) {
//      log.write(minFuel[i] + "\n");
//    }
//
//    log.flush();
//  }
//
//  static class RefillLine {
//    boolean start;
//    int x;
//    int y;
//
//    public RefillLine(int x, int y, boolean start) {
//      this.x = x;
//      this.y = y;
//      this.start = start;
//    }
//  }
//}
