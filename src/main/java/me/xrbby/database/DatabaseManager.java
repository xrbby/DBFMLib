package me.xrbby.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseManager {

    private final DatabaseCredentials databaseCredentials;
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;

    public DatabaseManager(DatabaseCredentials databaseCredentials) { this.databaseCredentials = databaseCredentials; }

    public void connect(Runnable runnable) {

        String username = databaseCredentials.username();
        String password = databaseCredentials.password();
        String server = databaseCredentials.server();
        String database = databaseCredentials.database();

        this.mongoClient = MongoClients.create(String.format("mongodb+srv://%1$s:%2$s@%3$s/?retryWrites=true&w=majority", username, password, server));

        this.mongoDatabase = mongoClient.getDatabase(database);

        runnable.run();
    }

    public void close() { mongoClient.close(); }

    public MongoDatabase getMongoDatabase() { return mongoDatabase; }
}