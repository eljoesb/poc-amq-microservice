package com.lab;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class AMQConsumerService implements Runnable {

  @Inject
  ConnectionFactory connectionFactory;

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  void onStart(@Observes StartupEvent ev) {
    scheduler.scheduleWithFixedDelay(this, 0L, 5L, TimeUnit.SECONDS);
  }

  void onStop(@Observes ShutdownEvent ev) {
    scheduler.shutdown();
  }

  @Override
  public void run() {
    try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
      JMSConsumer consumer = context.createConsumer(context.createQueue("testerQueue"));
      while (true) {
        Message message = consumer.receive();
        if (message == null) {
          return;
        }
        JsonReader jsonReader = Json.createReader(new StringReader(message.getBody(String.class)));

        JsonObject object = jsonReader.readObject();
        System.out.println("Message received: " + object);
      }
    } catch (JMSException e) {
      System.err.println("Error receiving message: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
