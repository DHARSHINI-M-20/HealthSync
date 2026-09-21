package com.healthsync.repository;
import java.util.List;
import java.util.Optional;
/** Common persistence contract. */
public interface Repository<T> { T save(T entity); Optional<T> findById(String id); List<T> findAll(); void deleteById(String id); }
