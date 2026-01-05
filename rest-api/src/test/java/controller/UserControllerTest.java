package controller;

import com.vaka.daily.Application;
import com.vaka.daily.exception.ValidationException;
import com.vaka.daily.repository.ScheduleRepository;
import com.vaka.daily.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@Slf4j
@Transactional
public class UserControllerTest {
    private final static String TEST_URL = "/api/user";

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    void setUp() {
        entityManager
                .createNativeQuery("alter sequence daily_user_user_id_seq restart with " + getNewId())
                .executeUpdate();

        entityManager
                .createNativeQuery("alter sequence schedule_schedule_id_seq restart with " + (scheduleRepository.count() + 1))
                .executeUpdate();
    }

    Integer getNewId() {
        return Math.toIntExact(userRepository.count() + 1);
    }

    @DisplayName("Should return all user")
    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(TEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Should return user with ID 1")
    @Test
    void testGetByIdOne() throws Exception {
        Integer ID = 1;
        mockMvc.perform(get(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.login").value("vaka"));

    }

    @DisplayName("Should return user with ID 3")
    @Test
    void testGetByIdThree() throws Exception {
        Integer ID = 3;
        mockMvc.perform(get(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.login").value("aka"))
                .andExpect(jsonPath("$.userType.id").value("1"))
                .andExpect(jsonPath("$.userType.name").value("user"));

    }

    @DisplayName("Should create new user (all fields)")
    @Test
    void testPost() throws Exception {
        String jsonString = """
{
    "login": "new_user",
    "password": "new_password",
    "firstName": "Иван",
    "secondName": "Новгородов",
    "patronymic": "Андреевич",
    "userType": {
        "id": 2,
        "name": "vip"
    },
    "schedules": [],
    "telegramId": 1
}
""";
        int newId = getNewId();

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newId))
                .andExpect(jsonPath("$.login").value("new_user"))
                .andExpect(jsonPath("$.password").value("new_password"))
                .andExpect(jsonPath("$.userType.name").value("vip"))
                .andExpect(jsonPath("$.telegramId").value(1));

    }

    @DisplayName("Should create new user (some fields)")
    @Test
    void testPost2() throws Exception {
        String jsonString = "{\"login\":\"new_user\",\"password\":\"new_password\",\"firstName\":\"Иван\"," +
                "\"secondName\":\"Новгородов\",\"patronymic\":\"Андреевич\"}";
        int newId = getNewId();

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newId))
                .andExpect(jsonPath("$.login").value("new_user"))
                .andExpect(jsonPath("$.password").value("new_password"));

    }

    @DisplayName("Validation should failed (empty login)")
    @Test
    void testPost3() throws Exception {
        String jsonString = "{\"login\":\"\",\"password\":\"new_password\",\"firstName\":\"Иван\"," +
                "\"secondName\":\"Новгородов\",\"patronymic\":\"Андреевич\"}";

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException));
    }

    @DisplayName("Should throws UserNotFoundException by TG ID")
    @Test
    void testGetByWrongTgId() throws Exception {
        long tgId = -1L;

        mockMvc.perform(get(TEST_URL + "/search?tgId=" + tgId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Should create new user with default schedule")
    @Test
    void testPost4() throws Exception {
        String jsonString = """
{
    "login": "new_user",
    "password": "new_password",
    "firstName": "Иван",
    "secondName": "Новгородов",
    "patronymic": "Андреевич",
    "userType": {
        "id": 2,
        "name": "admin"
    },
    "schedules": []
}
""";
        int newId = getNewId();

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newId))
                .andExpect(jsonPath("$.login").value("new_user"))
                .andExpect(jsonPath("$.password").value("new_password"))
                .andExpect(jsonPath("$.userType.name").value("admin"))
                .andExpect(jsonPath("$.schedules[0].name").value("main"))
                .andExpect(jsonPath("$.schedules[0].user").value(newId));
    }

    @DisplayName("Should update user")
    @Test
    void testPut() throws Exception {
        String jsonString = """
{
    "id": 1,
    "login": "new_user",
    "password": "new_password",
    "firstName": "Иван",
    "secondName": "Новгородов",
    "patronymic": "Андреевич",
    "userType": {
        "id": 2,
        "name": "vip"
    },
    "schedules": []
}
""";
        Integer ID = 1;

        mockMvc.perform(put(TEST_URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.login").value("new_user"))
                .andExpect(jsonPath("$.password").value("new_password"))
                .andExpect(jsonPath("$.userType.name").value("vip"));
    }

    @DisplayName("Should delete user")
    @Test
    void testDelete() throws Exception {
        Integer ID = 1;

        mockMvc.perform(delete(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("Should get user with TG id")
    @Test
    void testGetWithTgId() throws Exception {
        long tgId = 1531192384;
        mockMvc.perform(get(TEST_URL + "/search?tgId=" + tgId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("vaka"))
                .andExpect(jsonPath("$.telegramId").value(tgId));
    }
}
