package controller;

import com.vaka.daily.Application;
import com.vaka.daily.exception.ValidationException;
import com.vaka.daily.repository.ScheduleRepository;
import com.vaka.daily.repository.TaskRepository;
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
public class TaskControllerTest {
    private final static String TEST_URL = "/api/task";

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("alter sequence task_task_id_seq restart with " + getNewId())
                .executeUpdate();
    }

    Integer getNewId() {
        return Math.toIntExact(taskRepository.count() + 1);
    }

    @DisplayName("Should return all tasks")
    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(TEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Should return task with ID 1")
    @Test
    void testGetById() throws Exception {
        Integer ID = 1;
        mockMvc.perform(get(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("Прочитать книгу"));

    }

    @DisplayName("Should create new task")
    @Test
    void testPost() throws Exception {
        String jsonString = "{\"name\":\"new_task\",\"description\":\"Успешно пройти тесты\"," +
                "\"deadline\":\"2023-11-30T00:00:00\",\"status\":true, \"scheduleId\" : 2, \"taskTypeId\" : 1}";
        int newId = getNewId();

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newId))
                .andExpect(jsonPath("$.name").value("new_task"));

        mockMvc.perform(get("/api/schedule/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("main"))
                .andExpect(jsonPath("$.tasks[1].name").value("new_task"))
                .andExpect(jsonPath("$.tasks[1].description").value("Успешно пройти тесты"));
    }

    @DisplayName("Validation should failed (empty name)")
    @Test
    void testPost2() throws Exception {
        String jsonString = "{\"name\":\"\",\"description\":\"Прочитать книгу Java Core\"," +
                "\"deadline\":\"2023-11-30T00:00:00\",\"status\":true}";

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException));

    }

    @DisplayName("Should update task")
    @Test
    void testPut() throws Exception {
        String jsonString = """
{
"name" : "updated_task_name",
"description" : "Прочитать книгу Java Core",
"deadline" : "2023-11-30T00:00:00",
"status" : true,
"scheduleId" : 1,
"taskTypeId" : 1
}
""";
        Integer ID = 1;

        mockMvc.perform(put(TEST_URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("updated_task_name"));
    }

    @DisplayName("Should delete task")
    @Test
    void testDelete() throws Exception {
        Integer ID = 1;

        mockMvc.perform(delete(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
