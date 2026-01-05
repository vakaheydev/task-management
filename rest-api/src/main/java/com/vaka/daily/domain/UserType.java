package com.vaka.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user_type")
@Data
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_type_id")
    private Integer id;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "user_type_name", nullable = false, unique = true, length = 100)
    private String name;

    public UserType() {
    }

    public UserType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
