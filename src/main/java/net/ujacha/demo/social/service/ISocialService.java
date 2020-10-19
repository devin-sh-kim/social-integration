package net.ujacha.demo.social.service;

import net.ujacha.demo.social.exception.SocialException;
import net.ujacha.demo.social.vo.SocialAccount;
import net.ujacha.demo.social.vo.Token;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

public interface ISocialService {

    String getOAuthUrl(HttpServletRequest request);

    Token getToken(String code, String state) throws SocialException;

    SocialAccount getSocialAccount(String accessToken);

}
