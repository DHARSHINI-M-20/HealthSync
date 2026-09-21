package com.healthsync.repository.mongo;

import org.bson.Document;

/** Converts a domain object to and from a MongoDB document. */
public interface DocumentMapper<T> { Document toDocument(T entity); T fromDocument(Document document); }
