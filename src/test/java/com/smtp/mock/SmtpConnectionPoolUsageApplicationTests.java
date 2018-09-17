package com.smtp.mock;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.smtp.mock.entities.EmailMessage;
import com.smtp.mock.service.SmtpServiceWrap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmtpConnectionPoolUsageApplicationTests {

  private CountCheck counter = new CountCheck();
  // private static final int THREAD_COUNT = 2;

  private List<Long> timers = new ArrayList<Long>();

  @Autowired
  private SmtpServiceWrap wrap;

  @Before
  public void setUp() {
    counter.initialize(11);
  }

  // @Test
  // @ThreadCount(THREAD_COUNT)
  // public void contextLoads() {
  // counter.getCount();
  // System.err.println("Counter: " + counter);
  // EmailMessage response =
  // wrap.sendThroughPool("test3001.rbbn@gmail.com", EmailMessageFactory.createMessage());
  //
  // // RestTemplate template = new RestTemplate();
  // // String url = "http://localhost:8080/cpaas/email/v1/test3001.rbbn@gmail.com/messages";
  // // HttpEntity<EmailMessage> requestEntity = new
  // // HttpEntity<>(EmailMessageFactory.createMessage());
  // // ResponseEntity<EmailMessage> response =
  // // template.exchange(url, HttpMethod.POST, requestEntity, EmailMessage.class);
  // System.err.println("Response: " + response);
  // assertNotNull(response);
  // }


  @Test
  public void addsAndRetrieves() {
    int threads = counter.getCount();
    System.err.println("Retrieved counter: " + threads);
    ExecutorService service = Executors.newFixedThreadPool(threads);
    System.err.println("Service: " + service);
    Collection<Future<EmailMessage>> futures = new ArrayList<>(threads);
    for (int t = 0; t < threads; ++t) {
      System.err.println("Add email sending thread!!");
      Long timer = System.currentTimeMillis();
      System.err.println("----------Starting timer: " + timer);
      futures.add(service.submit(() -> wrap.sendThroughPool("test3001.rbbn@gmail.com",
          EmailMessageFactory.createMessage())));
      
      Long sentTimer = System.currentTimeMillis();
      System.err.println("----------Sent timer: " + sentTimer);
      
      Long duration = sentTimer - timer;
      System.err.println("----------Sending time: " + duration);
      timers.add(duration);
    }
    int responseSize = 0;
    for (Future<EmailMessage> f : futures) {
      try {
        System.err.println("Getting response " + f);
        System.err.println("get future: " + f.get());
        if (f.get() != null) {
          responseSize++;
        }
      } catch (InterruptedException | ExecutionException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    timers.stream().forEach(timer -> System.err.println("Duration " + timer));

    assertEquals(responseSize, threads);
  }


  @After
  public void tearDown() {
    assertEquals("Value should be 11", 11, counter.getCount());
  }

}
