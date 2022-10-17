package com.prepfortech.service;

import com.prepfortech.acessor.OtpAccessor;
import com.prepfortech.acessor.UserAccessor;
import com.prepfortech.acessor.model.*;
import com.prepfortech.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserService {
    @Autowired
    private UserAccessor userAccessor;

    @Autowired
    private OtpAccessor otpAccessor;

    public void addNewUser(final String email, final String name, final String password, final String phoneNo){
        if(phoneNo.length()!= 10){
            throw new InvalidDataException("phoneNo"+ phoneNo +"is invalid");
        }
        if(password.length()<6){
            throw new InvalidDataException("password is too simple");
        }
        if(name.length()<5){
            throw new InvalidDataException("name is not correct");
        }
        String emaiRegex = "^[a-zA-Z0-9_+&*]+(?://."+
                            "[a-zA-Z0-9+&*-]+)*@"+
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z"+
                            "A_Z]{2,7}$";
        Pattern pat = Pattern.compile(emaiRegex);
        if(!pat.matcher(email).matches()){
            throw new InvalidDataException("email is not correct");

        }
        UserDTO userDTO = userAccessor.getUserByEmail(email);
        if(userDTO != null){
            throw new InvalidDataException("user with emailid already exist");
        }
        userDTO = userAccessor.getUserByPhoneNo(phoneNo);
        if(userDTO != null){
            throw new InvalidDataException("user with phoneNo already exist");
        }

        userAccessor.addNewUser(email,name,password,phoneNo, UserState.ACTIVE, UserRole.ROLE_USER);

    }

    public void activateSubscription(){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        userAccessor.updateUserRole(userDTO.getUserId(),UserRole.ROLE_CUSTOMER);
    }
    public void deleteSubscription(){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        userAccessor.updateUserRole(userDTO.getUserId(),UserRole.ROLE_USER);

    }
    public void verifyEmail(final String otp){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        if (userDTO.getEmailVerificationStatus().equals(EmailVerificationStatus.UNVERIFIED)){
            OtpDTO otpDTO = otpAccessor.getUnusedOtp(userDTO.getUserId(), otp, OtpSentTo.EMAIL);
            if(otp!= null){
                userAccessor.updateEmailVerificationStatus(userDTO.getUserId(),EmailVerificationStatus.VERIFIED);
                otpAccessor.updateOtpState(otpDTO.getOtpId(), OtpState.USED);
            }
            else{
                throw new InvalidDataException("otp doesn't exist");
            }
        }

    }
    public void verifyPhone(final String otp){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        if (userDTO.getPhoneVerificationStatus().equals(PhoneVerificationStatus.UNVERIFIED)){
            OtpDTO otpDTO = otpAccessor.getUnusedOtp(userDTO.getUserId(), otp, OtpSentTo.PHONE);
            if(otp!= null){
                userAccessor.updatePhoneVerificationStatus(userDTO.getUserId(),PhoneVerificationStatus.VERIFIED);
                otpAccessor.updateOtpState(otpDTO.getOtpId(), OtpState.USED);
            }
            else{
                throw new InvalidDataException("otp doesn't exist");
            }
        }

    }
}
