package net.ujacha.demo.social.vo;

import lombok.Data;
import net.ujacha.demo.social.enums.Social;

@Data
public class JoinWithSocialRequestPayload extends JoinRequestPayload{

    private Social social;
    private String socialId;
    private String accessToken;
    private String refreshToken;

}
