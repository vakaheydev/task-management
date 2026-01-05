package service;

import com.vaka.daily.Application;
import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;
import com.vaka.daily.repository.TaskNotificationRepository;
import com.vaka.daily.service.domain.TaskNotificationService;
import com.vaka.daily.service.domain.TaskService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {Application.class})
@Slf4j
@Transactional
public class TaskNotificationServiceTest {
    @Autowired
    TaskNotificationService taskNotificationService;

    @Autowired
    TaskService TaskService;

    @Autowired
    TaskNotificationRepository repo;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setup() {
        entityManager.createNativeQuery("alter sequence task_notification_task_notification_id_seq restart with " + getNewId())
                .executeUpdate();
    }

    Integer getNewId() {
        return Math.toIntExact(repo.count() + 1);
    }

    @DisplayName("Should create new notification")
    @Test
    public void shouldCreate() {
        Task Task = TaskService.getById(1);
        TaskNotification taskNotification = new TaskNotification();

        taskNotification.setTask(Task);
        taskNotification.setLastNotifiedAt(LocalDateTime.now());

        taskNotificationService.create(taskNotification);

        TaskNotification byTaskId = taskNotificationService.getByTaskId(1);

        log.info(byTaskId.toString());
        assertNotNull(byTaskId);

        TaskNotification byId = taskNotificationService.getById(1);
        log.info(byId.toString());

        assertNotNull(byId);

        List<TaskNotification> notifications = taskNotificationService.getAll();

        assertEquals(1, notifications.size());
        assertEquals(1, notifications.get(0).getId());

        assertEquals(taskNotification.getLastNotifiedAt(), taskNotificationService.getById(1).getLastNotifiedAt());
    }
}
