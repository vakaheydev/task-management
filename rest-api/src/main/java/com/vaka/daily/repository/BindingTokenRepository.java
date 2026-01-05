package com.vaka.daily.repository;

import com.vaka.daily.domain.BindingToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BindingTokenRepository extends JpaRepository<BindingToken, Integer> {
    @Query("select bt from BindingToken bt where bt.user.id = :userId")
    Optional<BindingToken> findByUserId(@Param("userId") Integer userId);
    Optional<BindingToken> findByValue(String tokenValue);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM binding_token WHERE created_at < NOW() - INTERVAL '30 seconds'", nativeQuery = true)
    int deleteExpiredTokens();
}
