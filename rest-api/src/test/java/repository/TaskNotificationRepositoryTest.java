package repository;

import com.vaka.daily.Application;
import com.vaka.daily.domain.Task;
import com.vaka.daily.repository.TaskNotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(classes = {Application.class})
@Slf4j
@Transactional
public class TaskNotificationRepositoryTest {
    @Autowired
    TaskNotificationRepository taskRepository;

    @DisplayName("Get tasks for notification (not completed and deadline > now)")
    @Test
    public void shouldGetNotCompletedTasks() {
        List<Task> tasksForNotification = taskRepository.findTasksForNotification(LocalDateTime.now());
        log.info(tasksForNotification.toString());

        Assertions.assertEquals(6, tasksForNotification.size());
    }
}
