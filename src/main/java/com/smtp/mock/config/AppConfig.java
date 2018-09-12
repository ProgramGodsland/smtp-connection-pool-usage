package com.smtp.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class AppConfig {
  
  /**
   * Common use. This object mapper serves internal json conversion purposes.
   */
  public static ObjectMapper internalObjMapper = new ObjectMapper();

  /**
   * 
   * This object mapper serves spring boot rest json conversion. For unwrap root value for http
   * request and wrap root value for http response.
   * 
   */
  @Bean
  public ObjectMapper objMapperForSpringboot() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
    mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
    return mapper;
  }

}
