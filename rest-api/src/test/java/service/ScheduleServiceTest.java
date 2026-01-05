package service;

import com.vaka.daily.Application;
import com.vaka.daily.domain.Schedule;
import com.vaka.daily.service.domain.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {Application.class})
@Slf4j
@Transactional
public class ScheduleServiceTest {
    @Autowired
    ScheduleService service;

    @DisplayName("Should return all schedules")
    @Test
    void testGetAll() {
        List<Schedule> all = service.getAll();
        Schedule firstSchedule = all.get(0);

        assertEquals(3, all.size());
        assertEquals("main", firstSchedule.getName());
        assertEquals(1, firstSchedule.getId());
        assertEquals("vaka", firstSchedule.getUser().getLogin());
    }
}
