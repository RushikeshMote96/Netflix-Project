package com.prepfortech.acessor;

import com.prepfortech.acessor.model.AuthDTO;
import com.prepfortech.exceptions.DependencyFailureExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
@Repository
public class AuthAccessor {
/*store the token for given userid .if works else throws exceptions*/
    @Autowired
    DataSource dataSource;
    public void storeToken(final String userId, final String token){
        String insertQuery = "insert into auth (authId,token,userId) values(?,?,?)";
        String uuid = UUID.randomUUID().toString();

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(insertQuery);
            pstmt.setString(1,uuid);
            pstmt.setString(2,token);
            pstmt.setString(3,userId);
            pstmt.executeUpdate();

                 }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);
        }
    }
    public AuthDTO getAuthByToken(final String token){
        String query = "select authId,token,userId from auth where token =?";
        //System.out.println("auth token accessor"+token);
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,token);
            ResultSet resultSet = pstmt.executeQuery();
            //System.out.println("auth token accessor result set"+resultSet);
            if(resultSet.next()){
                AuthDTO authDTO = AuthDTO.builder()
                        .authId(resultSet.getString(1))
                        .token(resultSet.getString(2))
                        .userId(resultSet.getString(3))
                        .build();
                //System.out.println("get by auth token"+authDTO.getAuthId());
                return authDTO;

            }
            return null;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);
        }
    }

    public void deleteAuthByToken(final String token){
        String query = "delete from auth where token = ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,token);
            pstmt.execute();

        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);
        }


    }
}
