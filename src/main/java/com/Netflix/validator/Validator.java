package com.Netflix.validator;

import com.Netflix.acessor.ProfileAccessor;
import com.Netflix.acessor.VideoAccessor;
import com.Netflix.acessor.model.ProfileDTO;
import com.Netflix.acessor.model.VideoDTO;
import com.Netflix.exceptions.InvalidProfileException;
import com.Netflix.exceptions.InvalidVideoException;
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
