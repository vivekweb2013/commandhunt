package com.wirehall.commandhunt.backend.dto.filter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to receive the filter criteria from the client. It contains the pagination,
 * sort and conditions filters.
 *
 * <p>Business logic should validate the condition list and decide how the conditions should be
 * applied. e.g. for a specific entity, business logic should decide which operator (AND/OR) will be
 * applied between conditions.
 */
public class Filter {

  private List<Condition> conditions = new ArrayList<>();

  @Valid @NotNull private Pagination pagination;

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
