package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.User;
import com.vaka.daily.domain.UserType;
import com.vaka.daily.domain.dto.UserDto;
import com.vaka.daily.exception.notfound.UserNotFoundException;
import com.vaka.daily.repository.UserRepository;
import com.vaka.daily.security.SecurityUtils;
import com.vaka.daily.telegram.TelegramClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserTypeService userTypeService;
    private final ScheduleService scheduleService;
    private final TelegramClient telegramClient;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserTypeService userTypeService,
                           ScheduleService scheduleService,
                           TelegramClient telegramClient) {
        this.userRepository = userRepository;
        this.userTypeService = userTypeService;
        this.scheduleService = scheduleService;
        this.telegramClient = telegramClient;
    }

    @Override
    public List<User> getAll() {
        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return userRepository.findAll();
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public User getById(Integer id) {
        if (SecurityUtils.currentUserHasRole("ADMIN") || SecurityUtils.currentUser().getId().equals(id)) {
            return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id", id));
        }

        throw new AuthorizationDeniedException("Access denied");
    }

    @Override
    public User getByUniqueName(String login) {
        if (SecurityUtils.currentUserHasRole("ADMIN") || SecurityUtils.currentUser().getUsername().equals(login)) {
            return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException("name", login));
        }

        throw new AuthorizationDeniedException("Access denied");
    }

    @Override
    public User getByTgId(Long tgId) {
        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return userRepository.findByTelegramId(tgId).orElseThrow(() -> new UserNotFoundException("telegramId", tgId));
        }

        throw new AuthorizationDeniedException("Access denied");
    }

    @Override
    public List<User> getByUserTypeName(String userTypeName) {
        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return userRepository.findByUserTypeName(userTypeName);
        }

        throw new AuthorizationDeniedException("Access denied");
    }

    @Override
    public User createFromDTO(UserDto userDTO) {
        User user = convertFromDTO(userDTO);

        UserType defaultUserType = userTypeService.getDefaultUserType();
        user.setUserType(defaultUserType);

        Schedule schedule = scheduleService.createDefaultSchedule(user);
        user.addSchedule(schedule);

        return userRepository.save(user);
    }

    @Override
    public User create(User entity) {
        if (entity.getUserType() == null) {
            UserType defaultUserType = userTypeService.getDefaultUserType();
            entity.setUserType(defaultUserType);
        }

        User saved = userRepository.save(entity);

        if (entity.getSchedules().isEmpty()) {
            saved.addSchedule(scheduleService.createDefaultSchedule(entity));
        }

        return saved;
    }

    @Override
    public User updateById(Integer id, User entity) {
        User oldUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id", id));

        entity.setId(id);

        if (oldUser.getTelegramId() == null && entity.getTelegramId() != null) {
            telegramClient.sendMessage(entity.getTelegramId(), String.format("Рад знакомству, %s!\n\nТеперь я буду присылать Вам уведомления, чтобы Вы ничего не забыли <3\n\nПри вводе любого символа появляется меню бота",  entity.getLogin()));
        }

        return userRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            if (!userRepository.existsById(id)) {
                throw new UserNotFoundException("id", id);
            }
            userRepository.deleteById(id);
        }

        throw new AuthorizationDeniedException("Access denied");
    }

    private User convertFromDTO(UserDto userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setSecondName(userDTO.getSecondName());
        user.setPatronymic(userDTO.getPatronymic());

        return user;
    }
}
