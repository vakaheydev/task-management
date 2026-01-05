package com.vaka.daily.service.domain;

import com.vaka.daily.domain.BindingToken;
import com.vaka.daily.domain.User;
import com.vaka.daily.exception.notfound.BindingTokenNotFoundException;
import com.vaka.daily.repository.BindingTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class BindingTokenServiceImpl implements BindingTokenService {
    private final BindingTokenRepository repo;

    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    public BindingTokenServiceImpl(BindingTokenRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<BindingToken> getAll() {
        return repo.findAll();
    }

    @Override
    public BindingToken getByTokenValue(String tokenValue) {
        return repo.findByValue(tokenValue).orElseThrow(() -> new BindingTokenNotFoundException("value", tokenValue));
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public int deleteExpiredTasks() {
        return repo.deleteExpiredTokens();
    }

    @Override
    public BindingToken createToken(Integer userId) {
        BindingToken newToken = generateToken(userId);

        return repo.save(newToken);
    }

    @Override
    public BindingToken getByUserId(Integer userId) {
        return repo.findByUserId(userId).orElseThrow(() -> new BindingTokenNotFoundException("userId", userId));
    }

    @Override
    public BindingToken getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new BindingTokenNotFoundException("id", id));
    }

    private BindingToken generateToken(Integer userId) {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String tokenValue = base64Encoder.encodeToString(randomBytes);
        return new BindingToken(null, tokenValue, new User(userId), LocalDateTime.now());
    }
}
