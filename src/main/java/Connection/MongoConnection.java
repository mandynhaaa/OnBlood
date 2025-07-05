package Connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {          
            mongoClient = MongoClients.create("mongodb+srv://ruan:nDVI4EUPmC3DDmVr@onblood.uuiqcsu.mongodb.net/?retryWrites=true&w=majority&appName=OnBlood");
        }
        return mongoClient.getDatabase("dbOnBlood");
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}