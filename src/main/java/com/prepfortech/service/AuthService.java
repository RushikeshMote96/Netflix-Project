package com.prepfortech.service;

import com.prepfortech.acessor.AuthAccessor;
import com.prepfortech.acessor.UserAccessor;
import com.prepfortech.acessor.model.UserDTO;
import com.prepfortech.exceptions.DependencyFailureExceptions;
import com.prepfortech.exceptions.InvalidCredentialException;
import com.prepfortech.security.SecurityConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthService {
    @Autowired
    private UserAccessor userAccessor;
    @Autowired
    private AuthAccessor authAccessor;

    public String login(final String email, final String password){
        UserDTO userDTO = userAccessor.getUserByEmail(email);
        System.out.println("userDto"+userDTO.getPassword());

            if (userDTO != null && userDTO.getPassword().equals(password)) {
                String token = Jwts.builder()
                        .setSubject(email)
                        .setAudience(userDTO.getRole().name())
                        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS512, SecurityConstant.SECRET_KEY.getBytes())
                        .compact();
                authAccessor.storeToken(userDTO.getUserId(), token);
                return token;
            }
            throw new InvalidCredentialException("either the email or password is incorrect");

    }
    public void logout(final String token){
        authAccessor.deleteAuthByToken(token);
    }

}
