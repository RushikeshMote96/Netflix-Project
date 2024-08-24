package com.Netflix.controller;

import com.Netflix.controller.model.GetWatchHistoryInput;
import com.Netflix.controller.model.WatchHistoryInput;
import com.Netflix.exceptions.InvalidProfileException;
import com.Netflix.exceptions.InvalidVideoException;
import com.Netflix.security.Roles;
import com.Netflix.service.WatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
public class WatchHistoryController {
    @Autowired
    WatchHistoryService watchHistoryService;
    @PostMapping("/video/videoId/watchedTime")
    @Secured({Roles.Customer})
    public ResponseEntity<Void> updateWatchHistory(@PathVariable("videoId") String videoId, @RequestBody WatchHistoryInput watchedHistoryInput){
        try {
            String profileId =watchedHistoryInput.getProfileId();
            int watchTime = watchedHistoryInput.getWatchedTime();
            watchHistoryService.updateWatchHistory(profileId,videoId,watchTime);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (InvalidVideoException | InvalidProfileException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/video/{videoId}/watchTime")
    @Secured({Roles.Customer})
    public ResponseEntity<Integer> getWatchHistory(@PathVariable("videoId") String videoId
                                                    , @RequestBody GetWatchHistoryInput input){
        String profileId = input.getProfileId();
        try{
            int watchLength = watchHistoryService.getWatchHistory(profileId,videoId);
            return ResponseEntity.status(HttpStatus.OK).body(watchLength);
        }
        catch (InvalidVideoException | InvalidProfileException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }



    }


}
