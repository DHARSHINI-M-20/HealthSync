package com.healthsync.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Resolves MongoDB settings from system properties, environment, or application.properties. */
public final class MongoConfiguration {
    private static final String URI_KEY = "healthsync.mongodb.uri";
    private static final String DATABASE_KEY = "healthsync.mongodb.database";
    private final String connectionUrl;
    private final String databaseName;

    private MongoConfiguration(String connectionUrl, String databaseName) {
        this.connectionUrl = connectionUrl;
        this.databaseName = databaseName;
    }

    public static MongoConfiguration load() {
        Properties properties = new Properties();
        try (InputStream stream = MongoConfiguration.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (stream != null) properties.load(stream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load MongoDB configuration", exception);
        }
        String uri = firstNonBlank(System.getProperty(URI_KEY), System.getenv("HEALTHSYNC_MONGODB_URI"), properties.getProperty(URI_KEY), "mongodb://localhost:27017");
        String database = firstNonBlank(System.getProperty(DATABASE_KEY), System.getenv("HEALTHSYNC_MONGODB_DATABASE"), properties.getProperty(DATABASE_KEY), "healthsync");
        return new MongoConfiguration(uri, database);
    }

    public String getConnectionUrl() { return connectionUrl; }
    public String getDatabaseName() { return databaseName; }

    private static String firstNonBlank(String... values) {
        for (String value : values) if (value != null && !value.isBlank()) return value;
        throw new IllegalStateException("MongoDB configuration must not be blank");
    }
}
