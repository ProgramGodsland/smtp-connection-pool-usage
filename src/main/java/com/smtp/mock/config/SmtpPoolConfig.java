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
    setMaxTotal(maxTotal);
    setMinIdle(minIdle);
    setMaxIdle(maxTotal);
    setMaxWaitMillis(25000);
    //setBlockWhenExhausted(false);
    System.err.println("Smtp pool config max : " + getMaxTotal());
  }

  public int loadMaxTotal(String maxTotalKey) {
    return 3;
  }

  public int loadMinIdle(String minIdleKey) {
    return 1;
  }
}
