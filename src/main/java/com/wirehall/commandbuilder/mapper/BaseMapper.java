package com.wirehall.commandbuilder.mapper;

import java.time.LocalDate;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class BaseMapper {
  public String mapString(Vertex v, String prop) {
    return (String) v.property(prop).value();
  }

  public long mapLong(Vertex v, String prop) {
    return Long.parseLong(String.valueOf(v.property(prop).value()));
  }

  public double mapDouble(Vertex v, String prop) {
    return Double.parseDouble(String.valueOf(v.property(prop).value()));
  }

  public LocalDate mapLocalDate(Vertex v, String prop) {
    return LocalDate.parse((String) v.property(prop).value());
  }
}
