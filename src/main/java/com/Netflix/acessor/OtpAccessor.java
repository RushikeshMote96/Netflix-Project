package com.prepfortech.acessor;

import com.prepfortech.acessor.model.OtpDTO;
import com.prepfortech.acessor.model.OtpSentTo;
import com.prepfortech.acessor.model.OtpState;
import com.prepfortech.exceptions.DependencyFailureExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

@Repository
public class OtpAccessor {
    @Autowired
    DataSource dataSource;

    public OtpDTO getUnusedOtp(final String userId, final String otp, final OtpSentTo otpSentTo){
        String query = "select * from otp where userId = ? and otp = ? and state = ? and sentTO = ?";

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,userId);
            pstmt.setString(2,otp);
            pstmt.setString(3, OtpState.UNUSED.toString());
            pstmt.setString(4,otpSentTo.toString());

            ResultSet resultSet = pstmt.executeQuery();
            System.out.println(resultSet);
            //System.out.println(resultSet);
            if(resultSet.next()){
                return OtpDTO.builder()
                        .otpId(resultSet.getString(1))
                        .userId(userId)
                        .otp(otp)
                        .state(OtpState.UNUSED)
                        .createdAt(resultSet.getDate(5))
                        .sentTo(OtpSentTo.valueOf(resultSet.getString(6)))
                        .build();


            }
            else{
                return null;
            }

        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);
        }
    }
    public void updateOtpState(final String otpId,final OtpState otpState){
        String query = "update otp set state= ? where otpId= ?";

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,otpState.toString());
            pstmt.setString(2,otpId);
            pstmt.executeUpdate();


        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);
        }

    }
    public void addNewOtp(final String userId, final String otp, final OtpSentTo sentTo) {
        String query = "insert  into otp values(?,?,?,?,?,?)";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2,userId);
            pstmt.setString(3,otp);
            pstmt.setString(4,OtpState.UNUSED.name());
            pstmt.setDate(5,new Date(System.currentTimeMillis()));
            pstmt.setString(6,sentTo.name());
            pstmt.executeUpdate();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);
            
        }

    }
}
