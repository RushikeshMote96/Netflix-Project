package com.Netflix.acessor;


import com.Netflix.acessor.model.WatchHistoryDTO;

import com.Netflix.exceptions.DependencyFailureExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class WatchHistoryAccessor {
    @Autowired
    DataSource dataSource;

    public void updateWatchedLength(final String profileId,final String videoId,final int watchedLength){
        String query = "update watchedHistory set watchedLength = ?,lastWatchedAt=? where profileId = ? and " +
                "videoId = ?";

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,watchedLength);
            pstmt.setDate(2,new Date(System.currentTimeMillis()));
            pstmt.setString(3,profileId);
            pstmt.setString(4,videoId);
            pstmt.execute();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);
        }

    }

    public void insertWatchHistory(final String profileId,final String videoId,final double rating,
                                   final int watchedLength){
    String query = "insert into watchHistory values(?,?,?,?,?,?)";

    try (Connection connection = dataSource.getConnection()){
        PreparedStatement pstmt = connection.prepareStatement(query);
        Date currentDate = new Date(System.currentTimeMillis());
        pstmt.setString(1,profileId);
        pstmt.setString(2,videoId);
        pstmt.setDouble(3,rating);
        pstmt.setInt(4,watchedLength);
        pstmt.setDate(5,currentDate);
        pstmt.setDate(6,currentDate);
        pstmt.execute();

    }
    catch (SQLException ex){
        ex.printStackTrace();
        throw new DependencyFailureExceptions(ex);
    }
    }

    public WatchHistoryDTO getWatchHistory(final String profileId,final String videoId){
        String query = "select rating, watchedLength, lastWatchedAt,firstWatchedAt from watchHistory where " +
                "profileId = ? and videoId=?";

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,profileId);
            pstmt.setString(2,videoId);
            ResultSet resultSet = pstmt.executeQuery();


            if(resultSet.next()){
                WatchHistoryDTO watchedHistoryDTO = WatchHistoryDTO.builder()
                        .profileId(profileId)
                        .videoId((videoId))
                        .rating(resultSet.getDouble(1))
                        .watchedLength(resultSet.getInt(2))
                        .lastWatchedAt(resultSet.getDate(3))
                        .firstWatchedAt(resultSet.getDate(4))
                        .build();

                return watchedHistoryDTO;

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
