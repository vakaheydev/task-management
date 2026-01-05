package controller;

import com.vaka.daily.Application;
import com.vaka.daily.exception.ValidationException;
import com.vaka.daily.repository.UserTypeRepository;
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
public class UserTypeControllerTest {
    private final static String TEST_URL = "/api/user_type";

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserTypeRepository repo;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("alter sequence user_type_user_type_id_seq restart with " + getNewId())
                .executeUpdate();
    }

    Integer getNewId() {
        return Math.toIntExact(repo.count() + 1);
    }

    @DisplayName("Should return all user types")
    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(TEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(4));
    }

    @DisplayName("Should return user type with ID 1 (user)")
    @Test
    void testGetById() throws Exception {
        Integer ID = 1;
        mockMvc.perform(get(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("user"))
                .andExpect(jsonPath("$.users.length()").value(2));

    }

    @DisplayName("Should return user type with ID 4 (developer)")
    @Test
    void testGetById2() throws Exception {
        Integer ID = 4;
        mockMvc.perform(get(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("developer"))
                .andExpect(jsonPath("$.users.length()").value(1))
                .andExpect(jsonPath("$.users[0].login").value("vaka"));

    }

    @DisplayName("Should create new user type")
    @Test
    void testPost() throws Exception {
        String jsonString = "{ \"name\" : \"new_user_type\" }";
        int newId = getNewId();

        mockMvc.perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newId))
                .andExpect(jsonPath("$.name").value("new_user_type"));

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

    @DisplayName("Should update user type")
    @Test
    void testPut() throws Exception {
        String jsonString = "{ \"name\" : \"updated_user_type\" }";
        Integer ID = 1;

        mockMvc.perform(put(TEST_URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("updated_user_type"));
    }

    @DisplayName("Should delete user type")
    @Test
    void testDelete() throws Exception {
        Integer ID = 1;

        mockMvc.perform(delete(TEST_URL + "/{id}", ID))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
