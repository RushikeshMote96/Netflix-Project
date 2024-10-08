package com.Netflix.service;

import com.Netflix.acessor.VideoAccessor;
import com.Netflix.acessor.model.S3Accessor;
import com.Netflix.acessor.model.VideoDTO;
import com.Netflix.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoService {
    @Autowired
    VideoAccessor videoAccessor;

    @Autowired
    S3Accessor s3Accessor;

    public String getVideoUrl(final String videoId) {
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        if (videoDTO == null) {
            throw new InvalidDataException("Video with VideoId" + videoId + "does not exist");
        }
        String videoPath = videoDTO.getVideoPath();
        return s3Accessor.getPreSignedUrl(videoPath, videoDTO.getTotalLength() * 60);

    }

    public String getVideoThumbnailPath(final String videoId) {
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        if (videoDTO == null) {
            throw new InvalidDataException("Video with VideoId" + videoId + "does not exist");
        }
        String thumbnailPath = videoDTO.getThumbnailPath();
        return s3Accessor.getPreSignedUrl(thumbnailPath, 2 * 60);
    }
}