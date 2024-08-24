package com.Netflix.acessor.model;
import lombok.Builder;
import  lombok.Getter;
@Builder
@Getter
public class UserDTO {
    private String userId;
    private String name;
    private String email;
    private String phoneNo;
    private String password;
    private UserState state;
    private UserRole role;
    private EmailVerificationStatus emailVerificationStatus;
    private PhoneVerificationStatus phoneVerificationStatus;


}
