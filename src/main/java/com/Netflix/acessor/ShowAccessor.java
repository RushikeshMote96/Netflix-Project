package com.Netflix.acessor;

import com.Netflix.acessor.model.ShowAudience;
import com.Netflix.acessor.model.ShowDTO;
import com.Netflix.acessor.model.ShowGenre;
import com.Netflix.acessor.model.ShowType;
import com.Netflix.exceptions.DependencyFailureExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ShowAccessor {
    @Autowired
    DataSource dataSource;

    public List<ShowDTO> getShowByName(final String showName) {
        String query = "select shoeId, name, typeOfShow, genre, audience, rating, length, thumbnailPath where"
                + " showName like %?%";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, showName);
            ResultSet resultSet = pstmt.executeQuery();
            List<ShowDTO> showList = new ArrayList<>();
            while (resultSet.next()) {
                ShowDTO showDTO = ShowDTO.builder()
                        .showId(resultSet.getString(1))
                        .name(resultSet.getString(2))
                        .typeOfShow(ShowType.valueOf(resultSet.getString(3)))
                        .genre(ShowGenre.valueOf(resultSet.getString(4)))
                        .audience(ShowAudience.valueOf(resultSet.getString(5)))
                        .rating(resultSet.getDouble(6))
                        .length(resultSet.getInt(7))
                        .thumbnailPath(resultSet.getString(8))
                        .build();
                showList.add(showDTO);

            }
            return showList;

        }
        catch (SQLException ex) {
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);

        }


    }
}
