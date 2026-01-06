package com.vaka.daily.controller;

import com.vaka.daily.domain.UserType;
import com.vaka.daily.exception.ValidationException;
import com.vaka.daily.service.domain.UserTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user_type")
@Slf4j
public class UserTypeController {
    private final UserTypeService service;

    @Autowired
    public UserTypeController(UserTypeService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    public ResponseEntity<?> getByUniqueName(@RequestParam("name") String name) {
        return ResponseEntity.ok(service.getByUniqueName(name));
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid UserType userType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userType));
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Integer id, @RequestBody @Valid UserType userType,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.ok(service.updateById(id, userType));
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
