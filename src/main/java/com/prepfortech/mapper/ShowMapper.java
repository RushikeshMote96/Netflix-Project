package com.prepfortech.mapper;

import com.prepfortech.acessor.model.S3Accessor;
import com.prepfortech.acessor.model.ShowDTO;
import com.prepfortech.controller.model.ShowOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowMapper {
    @Autowired
    S3Accessor s3Accessor;
    public ShowOutput mapShowDtoToOutput(final ShowDTO input){
        ShowOutput output = ShowOutput.builder()
                .showId(input.getShowId())
                .name(input.getName())
                .typeOfShow(input.getTypeOfShow())
                .genre(input.getGenre())
                .audience(input.getAudience())
                .rating(input.getRating())
                .length(input.getLength())
                .thumbnailPath(s3Accessor.getPreSignedUrl(input.getThumbnailPath(),5*60))
                .build();
        return output;

    }
}
