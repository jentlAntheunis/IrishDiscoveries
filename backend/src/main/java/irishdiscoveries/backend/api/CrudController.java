package irishdiscoveries.backend.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.service.CrudService;

public abstract class CrudController<T> {
    protected abstract CrudService<T> service();

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service().getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service().getById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service().create(body));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody T body) {
        return ResponseEntity.ok(service().update(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        service().delete(id);
        return ResponseEntity.noContent().build();
    }
}