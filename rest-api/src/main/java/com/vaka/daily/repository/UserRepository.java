package com.vaka.daily.repository;

import com.vaka.daily.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String name);

    @Query(
            "select u from User u " +
                    "join u.userType ut " +
                    "where ut.name = :userTypeName"
    )
    List<User> findByUserTypeName(@Param("userTypeName") String userTypeName);

    @Query("select u from User u where u.telegramId = :tgId")
    Optional<User> findByTelegramId(@Param("tgId") Long tgId);
}
