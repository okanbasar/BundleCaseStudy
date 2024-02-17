package app.bundle.casestudy.casetwo.connectors;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import static app.bundle.casestudy.casetwo.constants.Constants.ACTIVE_MQ_BROKER_URL;
import static app.bundle.casestudy.casetwo.constants.Constants.ACTIVE_MQ_QUEUE_NAME;

public class ActiveMQProducer {

    private static MessageProducer messageProducer;
    private static Session session;
    private static Connection connection;

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    public Session getSession() {
        return session;
    }

    public void closeConnection() {
        try {
            session.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public ActiveMQProducer() {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVE_MQ_BROKER_URL);
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(ACTIVE_MQ_QUEUE_NAME);

            messageProducer = session.createProducer(queue);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}