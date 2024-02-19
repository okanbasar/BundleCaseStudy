package app.bundle.casestudy.casetwo;

import app.bundle.casestudy.casetwo.connectors.ActiveMQConsumer;
import app.bundle.casestudy.casetwo.models.RandomData;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.TextMessage;

import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static app.bundle.casestudy.casetwo.constants.Constants.*;

public class ActiveMQToSQLApp {

    public static void main(String[] args) {
        try {
            // Connect to MQ and create consumer
            MessageConsumer consumer = new ActiveMQConsumer().getMessageConsumer();

            // Connect to SQL server
            java.sql.Connection dbConnection = DriverManager.getConnection(SQL_DB_URL, SQL_DB_USER, SQL_DB_PASSWORD);

            // Listen MQ and write to MongoDB collection
            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage textMessage) {
                    String data = textMessage.getText();
                    RandomData randomData = new RandomData(data);
                    insertDataIntoSQLDatabase(dbConnection, randomData);
                    System.out.println("Data is inserted to SQL table: " + randomData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertDataIntoSQLDatabase(java.sql.Connection connection, RandomData randomData) throws Exception {
        // Before inserting the data, make sure DB is installed, can be connected and the table SQL_DB_TABLENAME is created.
        String sql = "INSERT INTO " + SQL_DB_TABLENAME + " (TIMESTAMP,RANDOM,HASH) VALUES (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, randomData.getTimestamp());
        statement.setInt(2, randomData.getRandom());
        statement.setString(3, randomData.getHash());
        statement.executeUpdate();
        statement.close();
    }
}
