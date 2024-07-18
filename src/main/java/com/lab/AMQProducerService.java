package com.lab;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSRuntimeException;
import jakarta.jms.Session;

@ApplicationScoped
public class AMQProducerService {

  @Inject
  ConnectionFactory connectionFactory;

  public void sendMessage(String message) {
    try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
      context.createProducer().send(context.createQueue("testerQueue"), message);
      System.out.println("Message sent: " + message);
    } catch (JMSRuntimeException ex) {
      System.err.println("Error sending message: " + ex.getMessage());
    }
  }
}
