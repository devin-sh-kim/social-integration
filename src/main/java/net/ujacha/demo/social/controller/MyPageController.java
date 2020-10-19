package net.ujacha.demo.social.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ujacha.demo.social.service.UserService;
import net.ujacha.demo.social.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("my-page")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final UserService userService;

    @GetMapping
    public String myPage(HttpSession session, Model model) {

        if (session != null && session.getAttribute("LOGIN_ID") != null) {
            long userId = (long) session.getAttribute("LOGIN_ID");;
            User user = userService.getUser(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login";
        }

        return "myPage";
    }


}
