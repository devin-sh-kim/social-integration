package net.ujacha.demo.social;

import net.ujacha.demo.social.service.UserService;
import net.ujacha.demo.social.vo.JoinRequestPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SocialIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialIntegrationApplication.class, args);
    }

    @Component
    static class Init implements CommandLineRunner {

        @Autowired
        private UserService userService;

        @Override
        public void run(String... args) throws Exception {

            JoinRequestPayload payload = new JoinRequestPayload();
            payload.setAgreeTerms(true);
            payload.setEmail("lemite4u@daum.net");
            payload.setPassword("1111");
            payload.setName("김상훈");
            payload.setGender("M");
            payload.setAge("30");
            payload.setMobile("01099867529");

            userService.createUser(payload);

        }
    }

}
