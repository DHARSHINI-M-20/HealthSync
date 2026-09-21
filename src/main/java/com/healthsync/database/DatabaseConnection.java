package com.healthsync.database;

import com.healthsync.config.MongoConfiguration;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/** Thread-safe Singleton supplying the configured MongoDB Sync driver database. */
public final class DatabaseConnection implements AutoCloseable {
    private final MongoClient client;
    private final MongoDatabase database;

    private DatabaseConnection() {
        MongoConfiguration configuration = MongoConfiguration.load();
        client = MongoClients.create(configuration.getConnectionUrl());
        database = client.getDatabase(configuration.getDatabaseName());
    }

    private static class Holder { private static final DatabaseConnection INSTANCE = new DatabaseConnection(); }

    public static DatabaseConnection getInstance() { return Holder.INSTANCE; }
    public MongoDatabase getDatabase() { return database; }
    @Override public void close() { client.close(); }
}
