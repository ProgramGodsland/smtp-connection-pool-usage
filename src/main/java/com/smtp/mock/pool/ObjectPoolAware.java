package com.smtp.mock.pool;

/**
 * Created by nlabrot on 30/04/15.
 */
public interface ObjectPoolAware<T> {

  void setObjectPool(SmtpConnectionPool objectPool);

  SmtpConnectionPool getObjectPool();
}
