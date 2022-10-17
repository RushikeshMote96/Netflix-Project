package com.prepfortech.controller.model;

import com.prepfortech.security.Roles;
import com.prepfortech.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShowController {
    @Autowired
    ShowService showService;
    @GetMapping("/show")
    @Secured({Roles.Customer})

    public ResponseEntity<List<ShowOutput>> getShowByName(@RequestParam("ShowName") String ShowName){
        try {
            List<ShowOutput> showList = showService.getShowsByName(ShowName);

            return ResponseEntity.status(HttpStatus.OK).body(showList);

        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
