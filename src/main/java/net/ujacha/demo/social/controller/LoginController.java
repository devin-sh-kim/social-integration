package net.ujacha.demo.social.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ujacha.demo.social.service.*;
import net.ujacha.demo.social.util.PasswordEncoder;
import net.ujacha.demo.social.vo.LoginRequestPayload;
import net.ujacha.demo.social.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;
    private final LoginService loginService;

    private final KakaoSocialService kakaoSocialService;
    private final NaverSocialService naverSocialService;
    private final FacebookSocialService facebookSocialService;
    private final GoogleSocialService googleSocialService;

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {

        String kakaoOAuthUrl = kakaoSocialService.getOAuthUrl(request);
        model.addAttribute("kakaoOAuthUrl", kakaoOAuthUrl);

        String naverOAuthUrl = naverSocialService.getOAuthUrl(request);
        model.addAttribute("naverOAuthUrl", naverOAuthUrl);

        String facebookOAuthUrl = facebookSocialService.getOAuthUrl(request);
        model.addAttribute("facebookOAuthUrl", facebookOAuthUrl);

        String googleOAuthUrl = googleSocialService.getOAuthUrl(request);
        model.addAttribute("googleOAuthUrl", googleOAuthUrl);


        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequestPayload payload, HttpServletRequest request, Model model) {

        log.debug("LOGIN PAYLOAD: {}", payload);

        User user = userService.getUser(payload.getEmail());

        if (user != null) {

            String reqPassword = PasswordEncoder.md5(payload.getPassword());
            if (StringUtils.equals(reqPassword, user.getPassword())) {

                log.debug("LOGIN SUCCESS: {}", user);

                loginService.login(request, user);


            } else {

                log.debug("LOGIN FAIL - PASSWORD");
                model.addAttribute("error", "wrong password.");
                return "login";
            }

        } else {
            log.debug("LOGIN FAIL - EMAIL");
            model.addAttribute("error", "not found user email.");
            return "login";
        }


        return "redirect:/";

    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        // logout
        log.debug("LOGOUT");

        // Remove user session
        loginService.logout(request);

        // redirect to index page
        return "redirect:/";
    }

}
