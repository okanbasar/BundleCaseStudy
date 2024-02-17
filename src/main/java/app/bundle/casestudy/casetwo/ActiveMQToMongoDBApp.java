package app.bundle.casestudy.casetwo;

import app.bundle.casestudy.casetwo.connectors.ActiveMQConsumer;
import app.bundle.casestudy.casetwo.models.RandomData;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.TextMessage;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;

import static app.bundle.casestudy.casetwo.constants.Constants.*;

public class ActiveMQToMongoDBApp {

    public static final int NESTED_CONDITION_MIN_HASH_VALUE = 0x99;

    public static void main(String[] args) {
        try {
            // Connect to MQ and create consumer
            MessageConsumer consumer = new ActiveMQConsumer().getMessageConsumer();

            // Connect to MongoDB
            com.mongodb.client.MongoClient mongoClient = MongoClients.create(MONGO_URI);
            MongoDatabase database = mongoClient.getDatabase(MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection(MONGO_COLLECTION_NAME);

            // Listen MQ and write to MongoDB collection
            Integer previousHashDecimal = null;
            BsonValue previousDocumentBsonValue = null;
            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage textMessage) {
                    String data = textMessage.getText();
                    RandomData randomData = new RandomData(data);
                    String randomDataJson = randomData.toJSON();

                    if (previousHashDecimal != null && previousHashDecimal > NESTED_CONDITION_MIN_HASH_VALUE) {
                        // nested insert to the previous document, isNested flag set to TRUE.
                        previousDocumentBsonValue = insertDataIntoMongoDB(collection, data, true, previousDocumentBsonValue);
                    } else {
                        // new insert, isNested flag set to FALSE.
                        previousDocumentBsonValue = insertDataIntoMongoDB(collection, data, false, null);
                    }
                    previousHashDecimal = Integer.parseInt(randomData.getHash(), 16);

                    System.out.println("Data is inserted to MongoDB collection: " + randomDataJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BsonValue insertDataIntoMongoDB(MongoCollection<Document> collection, String randomDataJson, boolean isNested, BsonValue previousDocumentBsonValue) {
        BsonValue newDocumentBsonValue = null;
        try {
            Document newDocument = Document.parse(randomDataJson);
            String hash = newDocument.getString("hash");

            if (isNested && previousDocumentBsonValue != null) {
                // Hash > NESTED_CONDITION_MIN_HASH_VALUE
                // update previous document and add this document nested

                Bson prevDocFilter = Filters.eq("_id", previousDocumentBsonValue.asObjectId().getValue());
                FindIterable<Document> documents = collection.find(prevDocFilter);

                MongoCursor<Document> cursor = documents.iterator();
                Document existingDocument = null;
                if (cursor.hasNext()) {
                    existingDocument = cursor.next();
                }

                if (existingDocument != null) {
                    // Document found, replace with nested one
                    newDocument.append("nested_data", existingDocument);
                    newDocumentBsonValue = collection.replaceOne(prevDocFilter, newDocument).getUpsertedId();
                } else {
                    // Document not found, not an expected situation, but may happen if replace/delete occurs in between two random data generation (<200ms)
                    // insert new document
                    newDocumentBsonValue = collection.insertOne(newDocument).getInsertedId();
                }
            } else {
                // Hash <= NESTED_CONDITION_MIN_HASH_VALUE
                // insert new document
                newDocumentBsonValue = collection.insertOne(newDocument).getInsertedId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDocumentBsonValue;
    }
}
