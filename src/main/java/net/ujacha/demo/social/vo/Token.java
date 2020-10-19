package net.ujacha.demo.social.vo;

import lombok.Data;

@Data
public class Token {
    private String tokenType;
    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private int refreshTokenExpiresIn;
    private String scope;
    private String idToken;
}
