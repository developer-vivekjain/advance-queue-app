package com.javadeveloper.advancequeue.service;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import oracle.jms.AQjmsFactory;
import oracle.jms.AQjmsSession;

@Service
public class AQConsumerService {
	Logger logger = LoggerFactory.getLogger(AQConsumerService.class);

	@Value("${queue.name}")
	String queueName;

	@Value("${db.username}")
	String userName;

	@Value("${db.password}")
	String password;

	@Value("${db.url}")
	String url;

	@Value("${db.schema.name}")
	String schemaName;

	public void consumeMessage() {

		Connection connection = null;
		Session session = null;
		MessageConsumer consumer = null;

		try {
			// Create a connection factory
			ConnectionFactory connectionFactory = AQjmsFactory.getConnectionFactory(url, null);

			// Create a connection
			connection = connectionFactory.createConnection(userName, password);
			connection.start();

			// Create a session
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

			// Create a destination (queue)
			Destination destination = ((AQjmsSession) session).getQueue(schemaName, queueName);

			// Create a consumer
			consumer = session.createConsumer(destination);

			// Receive messages
			while (true) {
				Message message = consumer.receive(); // We can also specify a timeout

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					logger.info("Received message: " + text);			

					// Acknowledge the message
					message.acknowledge();
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			// Close resources in reverse order
			try {
				if (consumer != null) {
					consumer.close();
				}
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
