package com.mobicrave.eventtracker.list;

import java.util.Arrays;

public class MemIdList implements IdList {
  private long[] list;
  private int numRecords;

  public MemIdList(long[] list, int numRecords) {
    this.list = list;
    this.numRecords = numRecords;
  }

  @Override
  public void add(long id) {
    if (numRecords == list.length) {
      long[] newList = new long[list.length * 2];
      System.arraycopy(list, 0, newList, 0, list.length);
      list = newList;
    }
    list[numRecords++] = id;
  }

  @Override
  public Iterator subList(long firstStepEventId, long maxLastEventId) {
    int start = Arrays.binarySearch(list, 0, numRecords, firstStepEventId);
    if (start < 0) {
      start = -start - 1;
    }
    int end = Arrays.binarySearch(list, 0, numRecords, maxLastEventId);
    if (end < 0) {
      end = -end - 1;
    }
    return new Iterator(list, start, end);
  }

  @Override
  public IdList.Iterator subListByOffset(int startOffset, int numIds) {
    return new Iterator(list, startOffset, startOffset + numIds);
  }

  @Override
  public Iterator iterator() {
    return new Iterator(list, 0, numRecords);
  }

  @Override
  public void close() {}

  public static class Iterator implements IdList.Iterator {
    private final long[] list;
    private final int start;
    private final int end;
    private int offset;

    public Iterator(long[] list, int start, int end) {
      this.list = list;
      this.start = start;
      this.end = end;
      this.offset = 0;
    }

    @Override
    public boolean hasNext() {
      return start + offset < end;
    }

    @Override
    public long next() {
      return list[start + (offset++)];
    }
  }
}