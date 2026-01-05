package controller;

import com.vaka.daily.Application;
import com.vaka.daily.exception.ValidationException;
import com.vaka.daily.repository.TaskTypeRepository;
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
public class TaskTypeControllerTest {
    private final static String TEST_URL = "/api/task_type";

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TaskTypeRepository repo;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("alter sequence task_type_task_type_id_seq restart with " + getNewId())
                .executeUpdate();
    }

    Integer getNewId() {
        return Math.toIntExact(repo.count() + 1);
    }

    @DisplayName("Should return all task types")
    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(TEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Should return task type with ID 1 (singular)")
    @Test
    void testGetById() throws Exception {
        Integer ID = 1;
        mockMvc.perform(get(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("singular"));
    }

    @DisplayName("Should return task type with ID 3 (regular)")
    @Test
    void testGetById2() throws Exception {
        Integer ID = 3;
        mockMvc.perform(get(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("regular"));

    }

    @DisplayName("Should create new task type")
    @Test
    void testPost() throws Exception {
        String jsonString = "{ \"name\" : \"new_task_type\" }";
        int newId = getNewId();

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newId))
                .andExpect(jsonPath("$.name").value("new_task_type"));

    }

    @DisplayName("Validation should failed (empty name)")
    @Test
    void testPost2() throws Exception {
        String jsonString = "{ \"name\" : \"\" }";

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException));

    }

    @DisplayName("Should update task type")
    @Test
    void testPut() throws Exception {
        String jsonString = "{ \"name\" : \"updated_task_type\" }";
        Integer ID = 1;

        mockMvc.perform(put(TEST_URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("updated_task_type"));
    }

    @DisplayName("Should delete task type")
    @Test
    void testDelete() throws Exception {
        Integer ID = 1;

        mockMvc.perform(delete(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
