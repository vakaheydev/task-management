package com.vaka.daily.controller;

import com.vaka.daily.service.domain.BindingTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/binding_token")
public class BindingTokenController {
    private final BindingTokenService service;

    @Autowired
    public BindingTokenController(BindingTokenService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getById(@RequestParam("val") String tokenValue) {
        return ResponseEntity.ok(service.getByTokenValue(tokenValue));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<?> createToken(@RequestBody Integer userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createToken(userId));
    }
}
