package com.vaka.daily.service.domain;

import com.vaka.daily.domain.UserType;
import com.vaka.daily.exception.notfound.UserTypeNotFoundException;
import com.vaka.daily.repository.UserTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserTypeServiceImpl implements UserTypeService {
    private final UserTypeRepository repository;

    @Autowired
    public UserTypeServiceImpl(UserTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserType> getAll() {
        return repository.findAll();
    }

    @Override
    public UserType getById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new UserTypeNotFoundException("id", id));
    }

    @Override
    public UserType getByUniqueName(String name) {
        return repository.findByName(name).orElseThrow(() -> new UserTypeNotFoundException("name", name));
    }

    @Override
    public UserType getDefaultUserType() {
        return repository.findById(1).orElseThrow(() -> new IllegalStateException("Default user type not found"));
    }

    @Override
    public UserType create(UserType entity) {
        return repository.save(entity);
    }

    @Override
    public UserType updateById(Integer id, UserType entity) {
        if (!repository.existsById(id)) {
            throw new UserTypeNotFoundException("id", id);
        }

        entity.setId(id);
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new UserTypeNotFoundException("id", id);
        }

        repository.deleteById(id);
    }
}
