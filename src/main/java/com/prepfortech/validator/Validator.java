package com.prepfortech.validator;

import com.prepfortech.acessor.ProfileAccessor;
import com.prepfortech.acessor.VideoAccessor;
import com.prepfortech.acessor.model.ProfileDTO;
import com.prepfortech.acessor.model.VideoDTO;
import com.prepfortech.exceptions.InvalidProfileException;
import com.prepfortech.exceptions.InvalidVideoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Validator {
    @Autowired
    private ProfileAccessor profileAccessor;

    @Autowired
    private VideoAccessor videoAccessor;

    public void validateProfile(final String profileId, final String userId){
        ProfileDTO profileDTO = profileAccessor.getProfileByProfileId(profileId);
        if(profileDTO != null && profileDTO.getUserId().equals(userId)){
            throw new InvalidProfileException("profile"+ profileId +"profileId is invalid or dosent exist");
        }


    }
    public void validateVideoId(final String videoId) {
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        if (videoDTO == null) {
            throw new InvalidVideoException("Video with VideoId" + videoId + "does not exist");
        }
    }
}
