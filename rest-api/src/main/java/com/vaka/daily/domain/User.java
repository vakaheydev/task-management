package com.vaka.daily.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "daily_user")
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "user_login", nullable = false, length = 100)
    private String login;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "user_password", nullable = false, length = 100)
    private String password;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "user_first_name", nullable = false, length = 100)
    private String firstName;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "user_second_name", nullable = false, length = 100)
    private String secondName;

    @Size(max = 100)
    @Column(name = "user_patronymic", length = 100)
    private String patronymic;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_user_type", nullable = false)
    private UserType userType;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Schedule> schedules = new ArrayList<>();

    @Column(name = "user_telegram_id", unique = true)
    private Long telegramId;

    @JsonProperty("userTypeId")
    public Integer getUserTypeId() {
        return userType.getId();
    }

    @JsonProperty("schedulesId")
    public List<Integer> getSchedulesId() {
        return schedules.stream().map(Schedule::getId).toList();
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String login, String password, String firstName, String secondName, String patronymic,
                UserType userType, Long telegramId) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.patronymic = patronymic;
        this.userType = userType;
        this.telegramId = telegramId;
    }

    public User(Integer id, String login, String password, String firstName, String secondName, String patronymic,
                UserType userType) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.patronymic = patronymic;
        this.userType = userType;
    }

    public User() {
    }

    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", tgId(optional)=" + telegramId + '\'' +
                ", userType=" + userType.getName() +
                '}';
    }
}
