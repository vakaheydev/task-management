package com.vaka.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "binding_token")
public class BindingToken {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "token_id")
    private Integer id;

    @Size(max = 64)
    @NotNull
    @Column(name = "token_value", unique = true, nullable = false, length = 64)
    private String value;

    @OneToOne
    @JoinColumn(name = "id_user", unique = true)
    @JsonIgnore
    private User user;

    @NotNull
    @Column(name = "created_at", nullable = false)
    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonProperty("userId")
    public Integer getUserId() {
        return user != null ? user.getId() : null;
    }
}
