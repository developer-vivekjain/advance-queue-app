package com.javadeveloper.advancequeue.service;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AQConsumerService {
	Logger logger = LoggerFactory.getLogger(AQConsumerService.class);	

	@Value("${queue.broker.url}")
	String brokerUrl;

	@Value("${queue.name}")
	String queueName;

	public void consumeMessage() {

		Connection connection = null;
		try {
			// Create a connection factory
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

			// Create a connection
			connection = connectionFactory.createConnection();

			// Create a session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Create the destination (queue)
			Destination destination = session.createQueue(queueName);

			// Create a consumer
			MessageConsumer consumer = session.createConsumer(destination);

			// Start the connection
			connection.start();

			// Receive messages
			while (true) {
				Message message = consumer.receive();
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					logger.info("Received message: %s",  textMessage.getText());
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			// Close the connection to release resources
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
