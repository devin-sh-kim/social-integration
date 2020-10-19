package net.ujacha.demo.social.vo;

import lombok.Data;

@Data
public class JoinRequestPayload {
    private boolean agreeTerms;
    private String email;
    private String password;
    private String name;
    private String gender;
    private String age;
    private String mobile;
}
