package net.ujacha.demo.social.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ujacha.demo.social.enums.Social;
import net.ujacha.demo.social.exception.SocialException;
import net.ujacha.demo.social.service.*;
import net.ujacha.demo.social.vo.SocialAccount;
import net.ujacha.demo.social.vo.Token;
import net.ujacha.demo.social.vo.User;
import net.ujacha.demo.social.vo.UserSocial;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("social")
@RequiredArgsConstructor
@Slf4j
public class SocialController {

    private final KakaoSocialService kakaoSocialService;
    private final NaverSocialService naverSocialService;
    private final FacebookSocialService facebookSocialService;
    private final GoogleSocialService googleSocialService;
    private final UserService userService;
    private final LoginService loginService;

    @GetMapping("{socialVendor}/redirect")
    public String redirectJoin(
            @PathVariable String socialVendor,
            @RequestParam(required = false, defaultValue = "") String code,
            @RequestParam(required = false, defaultValue = "") String error,
            @RequestParam(required = false, defaultValue = "") String state,
            @RequestParam(required = false, defaultValue = "", name = "error_description") String errorDescription,
            HttpServletRequest request
    ) {

        log.debug("social: {}, code: {}, error: {}", socialVendor, code, error);

        if (StringUtils.isNotEmpty(error)) {
            throw new SocialException(error);
        }

        Social social = Social.valueOf(socialVendor);

        SocialAccount socialAccount;
        Token token;

        switch (social) {
            case kakao:
                token = kakaoSocialService.getToken(code, null);
                socialAccount = kakaoSocialService.getSocialAccount(token.getAccessToken());
                break;

            case naver:
                HttpSession session = request.getSession(true);
                String s = (String) session.getAttribute("NAVER_LOGIN_STATE");
                if (!StringUtils.equals(s, state)) {
                    throw new SocialException("fail to validate state: " + s + ", " + state);
                }
                token = naverSocialService.getToken(code, state);
                socialAccount = naverSocialService.getSocialAccount(token.getAccessToken());
                break;

            case facebook:
                token = facebookSocialService.getToken(code, null);
                socialAccount = facebookSocialService.getSocialAccount(token.getAccessToken());
                break;

            case google:
                token = googleSocialService.getToken(code, null);
                socialAccount = googleSocialService.getSocialAccountFromIdToken(token.getIdToken());
                break;

            default:
                token = null;
                socialAccount = null;
        }

        log.debug("TOKEN: {}", token);
        log.debug("SOCIAL USER: {}", socialAccount);

        if (socialAccount == null) {
            throw new SocialException("fail to access social user info");
        }

        // check exist user
        UserSocial userSocial = userService.getUserSocial(socialAccount.getSocial(), socialAccount.getId());
        log.debug("USER_SOCIAL: {}", userSocial);
        if (userSocial == null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("socialToken", token);
            session.setAttribute("socialAccount", socialAccount);
            return "redirect:/join-with-social";

        } else {
            // force login
            User user = userService.getUser(userSocial.getUserId());
            loginService.login(request, user);

            return "redirect:/";
        }


    }


}
