package com.prepfortech.acessor;

import com.prepfortech.acessor.model.OtpDTO;
import com.prepfortech.acessor.model.OtpSentTo;
import com.prepfortech.acessor.model.OtpState;
import com.prepfortech.exceptions.DependencyFailureExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String query = "update otp set state=? where optId=?";

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

        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new DependencyFailureExceptions(ex);
            
        }

    }
}
