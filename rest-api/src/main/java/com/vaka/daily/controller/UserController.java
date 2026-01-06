package com.vaka.daily.controller;

import com.vaka.daily.domain.User;
import com.vaka.daily.domain.dto.UserDto;
import com.vaka.daily.exception.ValidationException;
import com.vaka.daily.service.domain.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasAnyRole('USER', 'NOTIFIER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(name = "user_type_name", required = false) String userTypeName,
            @RequestParam(name = "login", required = false) String login,
            @RequestParam(name = "tgId", required = false) Long tgId) {
        if (userTypeName != null) {
            return ResponseEntity.ok(service.getByUserTypeName(userTypeName));
        } else if (login != null) {
            return ResponseEntity.ok(service.getByUniqueName(login));
        } else if (tgId != null) {
            return ResponseEntity.ok(service.getByTgId(tgId));
        }

        throw new IllegalArgumentException("No specified criteria");
        // TODO: 6/19/2024 Implements criteria and specifications
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/dto")
    public ResponseEntity<?> createFromDTO(@RequestBody @Valid UserDto userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createFromDTO(userDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Integer id, @RequestBody @Valid User user,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.ok(service.updateById(id, user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
