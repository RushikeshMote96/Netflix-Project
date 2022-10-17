package com.prepfortech.controller;

import com.prepfortech.controller.model.CreateUserInput;
import com.prepfortech.controller.model.VerifyEmailInput;
import com.prepfortech.controller.model.VerifyPhoneNoInput;
import com.prepfortech.exceptions.InvalidDataException;
import com.prepfortech.security.Roles;
import com.prepfortech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.Role;
import javax.swing.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<String> addNewUser(@RequestBody CreateUserInput createUserInput){
        String name = createUserInput.getName();
        String email = createUserInput.getEmail();
        String password = createUserInput.getPassword();
        String phoneNo = createUserInput.getPhoneNo();

        try {
        userService.addNewUser(email, name, password, phoneNo);
        return ResponseEntity.status(HttpStatus.OK).body("user created successfully");
        }
        catch (InvalidDataException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/user/subscription")
    @Secured({Roles.User})

    public String activateSubscription(){
        userService.activateSubscription();
        return "Subscription ativated successfully";
    }
    @DeleteMapping("/user/subscription")
    @Secured({Roles.Customer})

    public String deleteSubscription(){
        userService.deleteSubscription();
        return "Subscription deleted successfully";
    }

    @PostMapping("/user/email")
    @Secured({Roles.User,Roles.Customer})
    public ResponseEntity<String> VerifyEmail(@RequestBody VerifyEmailInput emailInput){
        try {
            userService.verifyEmail(emailInput.getOtp());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (InvalidDataException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    @PostMapping("/user/phoneNo")
    @Secured({Roles.User,Roles.Customer})
    public ResponseEntity<String> VerifyPhoneNo(@RequestBody VerifyPhoneNoInput phoneNoInput){
        try {
            userService.verifyPhone(phoneNoInput.getOtp());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (InvalidDataException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}