package com.healthsync.repository.mongo;

import com.healthsync.database.DatabaseConnection;
import com.healthsync.repository.Repository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import java.util.List;
import java.util.Optional;
import static com.mongodb.client.model.Filters.eq;

/** Reusable Sync-driver CRUD implementation; subclasses supply a collection and mapper. */
public abstract class AbstractMongoRepository<T> implements Repository<T> {
    private final MongoCollection<Document> collection;
    private final DocumentMapper<T> mapper;

    protected AbstractMongoRepository(String collectionName, DocumentMapper<T> mapper) {
        this.collection = DatabaseConnection.getInstance().getDatabase().getCollection(collectionName);
        this.mapper = mapper;
    }
    public T save(T entity) {
        Document document = mapper.toDocument(entity);
        collection.replaceOne(eq("_id", document.getString("_id")), document, new ReplaceOptions().upsert(true));
        return entity;
    }
    public Optional<T> findById(String id) { return Optional.ofNullable(collection.find(eq("_id", id)).first()).map(mapper::fromDocument); }
    public List<T> findAll() { return collection.find().map(mapper::fromDocument).into(new java.util.ArrayList<>()); }
    public void deleteById(String id) { collection.deleteOne(eq("_id", id)); }
    protected MongoCollection<Document> collection() { return collection; }
    protected DocumentMapper<T> mapper() { return mapper; }
}
