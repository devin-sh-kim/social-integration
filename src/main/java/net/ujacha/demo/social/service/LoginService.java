package net.ujacha.demo.social.service;

import lombok.extern.slf4j.Slf4j;
import net.ujacha.demo.social.vo.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class LoginService {

    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String LOGIN_EMAIL = "LOGIN_EMAIL";

    public void login(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);

        session.setAttribute(LOGIN_ID, user.getId());
        session.setAttribute(LOGIN_EMAIL, user.getEmail());
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        session.removeAttribute(LOGIN_ID);
        session.removeAttribute(LOGIN_EMAIL);
        session.invalidate();
    }
}
