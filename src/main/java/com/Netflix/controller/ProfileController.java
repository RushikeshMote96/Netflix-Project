package com.Netflix.controller;

import com.Netflix.controller.model.CreateProfileInput;
import com.Netflix.security.Roles;
import com.Netflix.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @PostMapping("/user/profile")
    @Secured({Roles.Customer,Roles.Anonymous})

    public ResponseEntity<Void> activateNewProfile(@RequestBody CreateProfileInput createProfileInput){

        try {
            profileService.activateProfile(createProfileInput.getName(),createProfileInput.getType());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }
    @DeleteMapping("/user/profile/{profileId}")
    @Secured({Roles.Customer})
    public ResponseEntity<Void> deactivateProfile(@PathVariable("profileId") String profileId){
        try {
            profileService.deactivateProfile(profileId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
