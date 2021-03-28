package com.wirehall.commandhunt.backend.dto.filter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Pagination {

  @Min(1)
  @NotNull
  private int pageNumber;

  @Min(5)
  @NotNull
  private int pageSize;

  @Valid
  @NotNull
  private Sort sort;

  public int getOffset() {
    return (this.pageNumber - 1) * this.pageSize;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
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
    return "Pagination{" + "pageNumber=" + pageNumber
        + ", pageSize=" + pageSize + ", sort=" + sort + '}';
  }
}
