package com.wirehall.commandhunt.backend.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionValueConverter implements AttributeConverter<List<String>, String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(OptionValueConverter.class);


  @Override
  public String convertToDatabaseColumn(List<String> values) {
    String jsonArrayString = null;
    ObjectMapper objectMapper = new ObjectMapper();
    if (values == null || values.isEmpty()) {
      return null;
    }

    try {
      jsonArrayString = objectMapper.writeValueAsString(values);
    } catch (JsonProcessingException e) {
      LOGGER.error("Error converting object {} to json array", values);
    }
    return jsonArrayString;
  }

  @Override
  public List<String> convertToEntityAttribute(String jsonArrayString) {
    List<String> values = null;
    ObjectMapper objectMapper = new ObjectMapper();

    if (jsonArrayString == null || jsonArrayString.isEmpty()) {
      return null;
    }
    try {
      values = objectMapper.readerForListOf(String.class).readValue(jsonArrayString);
    } catch (JsonProcessingException e) {
      LOGGER.error("Error converting json array {} to object", jsonArrayString);
    }
    return values;
  }
}
