package com.smtp.mock.config;

import javax.annotation.PostConstruct;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmtpPoolConfig extends GenericObjectPoolConfig {

  private static int maxTotal = DEFAULT_MAX_TOTAL;
  private static int minIdle = DEFAULT_MIN_IDLE;

  private static final String MAX_TOTAL_KEY = "smtp.config.max.total";
  private static final String MIN_IDLE_KEY = "smtp.config.min.idle";

  public SmtpPoolConfig() {
    super();
  }
  
  @PostConstruct
  private void init() {
    maxTotal = loadMaxTotal(MAX_TOTAL_KEY);
    minIdle = loadMinIdle(MIN_IDLE_KEY);
    setBlockWhenExhausted(false);
    setMaxTotal(maxTotal);
    setMinIdle(minIdle);
    setMaxIdle(4);
//    setMaxIdle(maxTotal);
    setMaxWaitMillis(25000);
    setTimeBetweenEvictionRunsMillis(1);
    System.err.println("Smtp pool config max : " + getMaxTotal());
  }

  public int loadMaxTotal(String maxTotalKey) {
    return 10;
  }

  public int loadMinIdle(String minIdleKey) {
    return 2;
  }
}
