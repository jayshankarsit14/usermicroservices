package com.jay.javaspringmsuser.repository;

import com.jay.javaspringmsuser.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Token save(Token token);
    Optional<Token> findByValue(String value);

    Optional<Token> findByValueAndDeletedEquals(String value,boolean isDeleted);
    Optional<Token> findByValueAndDeletedEqualsAndExpiryAtGreaterThan(String value, boolean isDeleted, Date expiryGreaterThan);

}
