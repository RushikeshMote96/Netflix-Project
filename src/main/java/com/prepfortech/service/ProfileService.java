package com.prepfortech.service;

import com.prepfortech.acessor.ProfileAccessor;
import com.prepfortech.acessor.model.ProfileType;
import com.prepfortech.acessor.model.UserDTO;
import com.prepfortech.controller.model.ProfileTypeInput;
import com.prepfortech.exceptions.InvalidDataException;
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
