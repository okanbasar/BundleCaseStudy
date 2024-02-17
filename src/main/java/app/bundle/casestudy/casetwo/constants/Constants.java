package app.bundle.casestudy.casetwo.constants;

public class Constants {

    // Socket constants
    public static final String SOCKET_IP = "localhost";
    public static final int SOCKET_PORT = 8080;

    // MQ constants
    public static final String ACTIVE_MQ_BROKER_URL = "tcp://localhost:61616";
    public static final String ACTIVE_MQ_QUEUE_NAME = "random_data_queue";

    // MongoDB constants
    public static final String MONGO_URI = "mongodb://localhost:27017";
    public static final String MONGO_DB_NAME = "mongodb";
    public static final String MONGO_COLLECTION_NAME = "randomdata";

    // SQL DB constants
    public static final String SQL_DB_URL = "jdbc:mysql://localhost:3306/mydatabase";
    public static final String SQL_DB_USER = "username";
    public static final String SQL_DB_PASSWORD = "password";
    public static final String SQL_DB_TABLENAME = "RANDOMDATA";
}
