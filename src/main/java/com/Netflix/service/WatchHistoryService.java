package com.Netflix.service;

import com.Netflix.acessor.VideoAccessor;
import com.Netflix.acessor.WatchHistoryAccessor;
import com.Netflix.acessor.model.UserDTO;
import com.Netflix.acessor.model.VideoDTO;
import com.Netflix.acessor.model.WatchHistoryDTO;
import com.Netflix.exceptions.InvalidVideoException;
import com.Netflix.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WatchHistoryService {
    @Autowired
    private Validator validator;

    @Autowired
    private WatchHistoryAccessor watchHistoryAccessor;

    @Autowired
    private VideoAccessor videoAccessor;

    public void updateWatchHistory(final String profileId,final String videoId,final int watchedLength){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        validator.validateProfile(profileId,userDTO.getUserId());
        validator.validateVideoId(videoId);
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);


        if(watchedLength<1 || watchedLength > videoDTO.getTotalLength()){
            throw new InvalidVideoException("watchedLength"+watchedLength+"is invalid");
        }
        WatchHistoryDTO watchHistoryDTO = watchHistoryAccessor.getWatchHistory(profileId,videoId);
        //inserting for the new entry
        if(watchHistoryDTO == null){
            watchHistoryAccessor.insertWatchHistory(profileId,videoId,0.0,watchedLength);
        }
        else{
            watchHistoryAccessor.updateWatchedLength(profileId,videoId,watchedLength);
        }

    }
    public int getWatchHistory(final String profileId, final String videoId){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        validator.validateProfile(profileId,userDTO.getUserId());
        validator.validateVideoId(videoId);

        WatchHistoryDTO watchHistoryDTO = watchHistoryAccessor.getWatchHistory(profileId,videoId);
        if(watchHistoryDTO != null){
            return watchHistoryDTO.getWatchedLength();
        }
        else{
            return 0;
        }

    }
}
