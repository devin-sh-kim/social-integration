package net.ujacha.demo.social.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ujacha.demo.social.service.LoginService;
import net.ujacha.demo.social.service.UserService;
import net.ujacha.demo.social.vo.JoinRequestPayload;
import net.ujacha.demo.social.vo.JoinWithSocialRequestPayload;
import net.ujacha.demo.social.vo.User;
import net.ujacha.demo.social.vo.UserSocial;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class JoinController {

    private final UserService userService;
    private final LoginService loginService;

    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    @PostMapping("/join")
    public String join(JoinRequestPayload payload, HttpServletRequest request) {
        log.debug("PAYLOAD: {}", payload);

        User user = userService.createUser(payload);

        log.debug("CREATED USER: {}", user);

        loginService.login(request, user);

        return "redirect:/";
    }

    @GetMapping("/join-with-social")
    public String joinWithSocialPage() {
        return "joinWithSocial";
    }

    @PostMapping("/join-with-social")
    public String joinWithSocial(JoinWithSocialRequestPayload payload, HttpServletRequest request) {
        log.debug("PAYLOAD: {}", payload);

        User user = userService.createUserWithSocial(payload);
        log.debug("CREATED USER: {}", user);



        loginService.login(request, user);

        return "redirect:/";
    }



}
