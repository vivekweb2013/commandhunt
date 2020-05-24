package com.wirehall.commandbuilder.dto.filter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Pageable {

  @Min(1)
  @NotNull
  private long pageNumber;

  @Min(5)
  @NotNull
  private long pageSize;

  @Valid
  @NotNull
  private Sort sort;

  public long getOffset() {
    return (this.pageNumber - 1) * this.getPageSize();
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
    return "Pageable{" + "pageNumber=" + pageNumber
        + ", pageSize=" + pageSize + ", sort=" + sort + '}';
  }
}
