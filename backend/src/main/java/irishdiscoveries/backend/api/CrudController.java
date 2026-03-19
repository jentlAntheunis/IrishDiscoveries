package irishdiscoveries.backend.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.dto.BaseDto;
import irishdiscoveries.backend.service.CrudService;

public abstract class CrudController<T, C extends BaseDto, U extends BaseDto> {
    protected abstract CrudService<T, C, U> service();

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service().getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service().getById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody C body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service().create(body));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody U body) {
        return ResponseEntity.ok(service().update(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        service().delete(id);
        return ResponseEntity.noContent().build();
    }
}