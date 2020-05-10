package com.wirehall.commandbuilder.dto.filter;

public class Pageable {

  private long offset;
  private long pageNumber;
  private long pageSize;
  private Sort sort;

  public long getOffset() {
    return offset;
  }

  public void setOffset(long offset) {
    this.offset = offset;
  }

  public long getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(long pageNumber) {
    this.pageNumber = pageNumber;
  }

  public long getPageSize() {
    return pageSize;
  }

  public void setPageSize(long pageSize) {
    this.pageSize = pageSize;
  }

  public Sort getSort() {
    return sort;
  }

  public void setSort(Sort sort) {
    this.sort = sort;
  }

  @Override
  public String toString() {
    return "Pageable{" +
        "offset=" + offset +
        ", pageNumber=" + pageNumber +
        ", pageSize=" + pageSize +
        ", sort=" + sort +
        '}';
  }
}
