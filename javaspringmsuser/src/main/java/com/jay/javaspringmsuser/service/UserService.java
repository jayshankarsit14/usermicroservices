package com.jay.javaspringmsuser.service;

import com.jay.javaspringmsuser.models.Token;
import com.jay.javaspringmsuser.models.User;
import com.jay.javaspringmsuser.repository.TokenRepository;
import com.jay.javaspringmsuser.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private TokenRepository tokenRepository;

    @Autowired
    public UserService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,TokenRepository tokenRepository){
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.tokenRepository=tokenRepository;
    }
    public User signUp(String fullName,String email,String password){
        User user=new User();
        user.setEmail(email);
        user.setName(fullName);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        System.out.println("password"+user.getHashedPassword());
        return userRepository.save(user);
    }

    public Token login(String email,String password){
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            //user not found exception
            System.out.println("saveToken is null");
            return null;
        }
        User user=userOptional.get();
        if(!bCryptPasswordEncoder.matches(password,user.getHashedPassword())){
            //password not match exception
            return null;
        }
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plus(30, ChronoUnit.DAYS);

        // Convert LocalDate to Date
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setUser(user);
        token.setExpiryAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        Token saveToken=tokenRepository.save(token);
        System.out.println(saveToken);
        return saveToken;
    }

    public void logOut(String token){
      Optional<Token> token1=tokenRepository.findByValueAndDeletedEquals(token,false);
      if(token1.isEmpty()){
          //throw token is already expired
          return ;
      }
      Token tkn=token1.get();
      tkn.setDeleted(true);
      tokenRepository.save(tkn);
    }
    public User validateToken(String token){
        Optional<Token> tkn=tokenRepository.findByValueAndDeletedEqualsAndExpiryAtGreaterThan(token, false, new Date());
        if(tkn.isEmpty()){
            return null;
        }
        return tkn.get().getUser();
    }
}
