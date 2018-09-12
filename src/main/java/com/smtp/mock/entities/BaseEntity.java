package com.smtp.mock.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smtp.mock.config.AppConfig;


@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseEntity implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1926200196809941334L;

  private static ObjectMapper mapper = AppConfig.internalObjMapper;
  private static final String toStringError = "ERROR";

  @Override
  public String toString() {
    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException | NullPointerException e) {
      return toStringError;
    }
  }

}
