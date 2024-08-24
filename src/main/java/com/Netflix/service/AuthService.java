package com.Netflix.service;

import com.Netflix.acessor.AuthAccessor;
import com.Netflix.acessor.UserAccessor;
import com.Netflix.acessor.model.UserDTO;
import com.Netflix.exceptions.InvalidCredentialException;
import com.Netflix.security.SecurityConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthService {
    @Autowired
    private UserAccessor userAccessor;
    @Autowired
    private AuthAccessor authAccessor;

    /**
     *
     * @param email:email of the user who want to login
     * @param password:password of the user who eant to login
     * @return return jwt token if password and email are correct
     */

    public String login(final String email, final String password) {
        UserDTO userDTO = userAccessor.getUserByEmail(email);

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
        //System.out.println("logout service" + token);
        authAccessor.deleteAuthByToken(token);
    }

}
