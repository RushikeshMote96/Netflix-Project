package com.Netflix.acessor;

import com.Netflix.acessor.model.VideoDTO;
import com.Netflix.exceptions.DependencyFailureExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class VideoAccessor {
    @Autowired
    DataSource dataSource;

    public VideoDTO getVideoByVideoId(final String videoId){
        String query = "select name, seriesId, showId, rating, releaseDate,totalLength, videoPath, thumbnailPath from video where videoId = ?";

        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,videoId);

            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                VideoDTO videoDTO = VideoDTO.builder()
                        .videoId(videoId)
                        .name(resultSet.getString(1))
                        .seriesId(resultSet.getString(2))
                        .showId(resultSet.getString(3))
                        .rating(resultSet.getDouble(4))
                        .releaseDate(resultSet.getDate(5))
                        .totalLength(resultSet.getInt(6))
                        .videoPath(resultSet.getString(7))
                        .thumbnailPath(resultSet.getString(8))
                        .build();
                return videoDTO;

            }
            else {
                return null;
            }


        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);
        }

    }
}
