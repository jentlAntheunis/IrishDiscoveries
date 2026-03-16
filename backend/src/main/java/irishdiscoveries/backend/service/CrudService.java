package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

public interface CrudService<T> {
    List<T> getAll();
    T getById(UUID id);
    T create(T entity);
    T update(UUID id, T patch);
    void delete(UUID id);
}
