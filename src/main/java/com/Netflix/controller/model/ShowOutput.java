package com.Netflix.controller.model;

import com.Netflix.acessor.model.ShowAudience;
import com.Netflix.acessor.model.ShowGenre;
import com.Netflix.acessor.model.ShowType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShowOutput {
    private String showId;
    private String name;
    private ShowType typeOfShow;
    private ShowGenre genre;
    private ShowAudience audience;
    private Double rating;
    private int length;
    private String thumbnailPath;
}
