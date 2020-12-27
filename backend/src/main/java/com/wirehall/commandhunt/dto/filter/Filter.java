package com.wirehall.commandhunt.dto.filter;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Filter {

  private List<Condition> conditions = new ArrayList<>();

  @Valid
  @NotNull
  private Pageable pageable;

  public List<Condition> getConditions() {
    return conditions;
  }

  public void setConditions(List<Condition> conditions) {
    this.conditions = conditions;
  }

  public Pageable getPageable() {
    return pageable;
  }

  public void setPageable(Pageable pageable) {
    this.pageable = pageable;
  }

  @Override
  public String toString() {
    return "Filter{" + "conditions=" + conditions + ", pageable=" + pageable + '}';
  }
}
