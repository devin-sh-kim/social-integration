package net.ujacha.demo.social.vo;

import lombok.Data;
import net.ujacha.demo.social.enums.Social;

@Data
public class UserSocial extends Audit{
    private long userId;
    private Social social;
    private String socialAccountId;

}
