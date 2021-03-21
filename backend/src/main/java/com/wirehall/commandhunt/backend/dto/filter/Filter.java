package com.wirehall.commandhunt.backend.dto.filter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Filter {

  private List<Condition> conditions = new ArrayList<>();

  @Valid
  @NotNull
  private Pagination pagination;

  public List<Condition> getConditions() {
    return conditions;
  }

  public void setConditions(List<Condition> conditions) {
    this.conditions = conditions;
  }

  public Pagination getPagination() {
    return pagination;
  }

  public void setPagination(Pagination pagination) {
    this.pagination = pagination;
  }

  @Override
  public String toString() {
    return "Filter{" + "conditions=" + conditions + ", pagination=" + pagination + '}';
  }
}
