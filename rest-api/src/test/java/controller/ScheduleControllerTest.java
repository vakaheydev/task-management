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
public class ScheduleControllerTest {
    private final static String TEST_URL = "/api/schedule";

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("alter sequence schedule_schedule_id_seq restart with " + getNewId())
                .executeUpdate();
    }

    Integer getNewId() {
        return Math.toIntExact(scheduleRepository.count() + 1);
    }

    @DisplayName("Should return all schedules")
    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(TEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Should return schedule with ID 1")
    @Test
    void testGetById() throws Exception {
        Integer ID = 1;
        mockMvc.perform(get(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("main"));

    }

    @DisplayName("Should create new schedule")
    @Test
    void testPost() throws Exception {
        String jsonString = """
{
    "name" : "new_schedule_name",
    "userId" : 1
}
""";
        int newId = getNewId();

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newId))
                .andExpect(jsonPath("$.name").value("new_schedule_name"));

        mockMvc.perform(get("/api/user/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("vaka"))
                .andExpect(jsonPath("$.schedules[0].name").value("main"));
    }

    @DisplayName("Validation should failed (empty name)")
    @Test
    void testPost2() throws Exception {
        String jsonString = "{ \"name\" : \"\", \"user\" : {\"id\" : \"1\" }}";

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException));

    }

    @DisplayName("Should update schedule")
    @Test
    void testPut() throws Exception {
        String jsonString = """
{
    "id": 1,
    "name": "updated_schedule_name",
    "tasks": [
        
    ],
    "userId": 1
}
""";
        Integer ID = 1;

        mockMvc.perform(put(TEST_URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("updated_schedule_name"));
    }

    @DisplayName("Should delete schedule")
    @Test
    void testDelete() throws Exception {
        Integer ID = 1;

        mockMvc.perform(delete(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
