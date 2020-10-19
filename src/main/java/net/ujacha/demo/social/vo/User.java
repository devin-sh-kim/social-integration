package net.ujacha.demo.social.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User extends Audit{
    private long id;
    private String email;
    private String password;
    private String name;
    private String gender;
    private String age;
    private String mobile;
}
