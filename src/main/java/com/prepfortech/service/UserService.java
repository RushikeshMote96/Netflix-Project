package com.prepfortech.service;

import com.prepfortech.acessor.EmailAccessor;
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

    @Autowired
    private EmailAccessor emailAccessor;

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
    private String generateOtp(){
        int min = 100000;
        int max = 999999;
        int otp = (int)Math.random()*(max-min+1) + min;
        return Integer.toString(otp);
    }
    public void sendEmailOtp(){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        String otp = generateOtp();

        otpAccessor.addNewOtp(userDTO.getUserId(),otp,OtpSentTo.EMAIL);
        emailAccessor.sendEmail(userDTO.getName(),userDTO.getEmail(), "otp for email verification","otp for verifying email is" + otp);
    }
}
