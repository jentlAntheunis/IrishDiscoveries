package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import irishdiscoveries.backend.dto.BaseDto;

public interface CrudService<T, C extends BaseDto, U extends BaseDto> {
    List<T> getAll();

    T getById(UUID id);

    T create(C createPayload);

    T update(UUID id, U patch);

    void delete(UUID id);
}
