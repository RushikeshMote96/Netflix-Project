package com.Netflix.service;

import com.Netflix.acessor.ProfileAccessor;
import com.Netflix.acessor.model.ProfileType;
import com.Netflix.acessor.model.UserDTO;
import com.Netflix.controller.model.ProfileTypeInput;
import com.Netflix.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ProfileService {

    @Autowired
    ProfileAccessor profileAccessor;

    public void activateProfile(final String name, final ProfileTypeInput type){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        if(name.length()<5 || name.length()>20){
            throw new InvalidDataException("name should be b/w 5 to 20");
        }
        profileAccessor.addNewProfile(userDTO.getUserId(),name, ProfileType.valueOf(type.name()));
    }
    public void deactivateProfile(final String profileId){
        profileAccessor.deleteProfile(profileId);
    }
}
