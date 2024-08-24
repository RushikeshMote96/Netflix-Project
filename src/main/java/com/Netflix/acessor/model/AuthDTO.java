package com.Netflix.acessor.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthDTO {
    private String authId;
    private String token;
    private String userId;
}
