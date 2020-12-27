package com.wirehall.commandhunt.dto.filter;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

  private long pageNumber = 1L;
  private long totalSize = 0L;
  private long pageSize = 10L;

  private List<T> records = new ArrayList<>();

  public long getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(long pageNumber) {
    this.pageNumber = pageNumber;
  }

  public long getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(long totalSize) {
    this.totalSize = totalSize;
  }

  public long getPageSize() {
    return pageSize;
  }

  public void setPageSize(long pageSize) {
    this.pageSize = pageSize;
  }

  public long getTotalPages() {
    return (long) Math.ceil(this.totalSize / ((double) this.pageSize));
  }

  public List<T> getRecords() {
    return records;
  }

  public void setRecords(List<T> records) {
    this.records = records;
  }

  public void addRecord(T record) {
    this.records.add(record);
  }

  @Override
  public String toString() {
    return "Page{" + "pageNumber=" + pageNumber + ", totalSize=" + totalSize
        + ", pageSize=" + pageSize + ", records=" + records + '}';
  }
}
