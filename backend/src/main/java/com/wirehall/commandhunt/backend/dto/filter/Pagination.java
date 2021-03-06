package com.wirehall.commandhunt.backend.dto.filter;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class Pagination {

  @Min(1)
  @NotNull
  private int pageNumber;

  @Min(5)
  @NotNull
  private int pageSize;

  @Valid @NotNull private Sort sort;

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
    return "Pagination{"
        + "pageNumber="
        + pageNumber
        + ", pageSize="
        + pageSize
        + ", sort="
        + sort
        + '}';
  }

  public static class Sort {

    @NotEmpty private String[] by;

    private SortOrder order = SortOrder.ASC;

    public String[] getBy() {
      return by;
    }

    public void setBy(String[] by) {
      this.by = by;
    }

    public SortOrder getOrder() {
      return order;
    }

    public void setOrder(SortOrder order) {
      this.order = order;
    }

    @Override
    public String toString() {
      return "Sort{" + "by='" + Arrays.toString(by) + '\'' + ", order=" + order + '}';
    }

    public enum SortOrder {
      ASC,
      DESC;

      @JsonValue
      public String toLowerCase() {
        return name().toLowerCase();
      }
    }
  }
}
