package com.Netflix.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginInput {
    private String email;
    private String password;
}
