package com.Netflix.controller.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class VerifyEmailInput {
    private String otp;
}
