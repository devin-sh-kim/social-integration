package net.ujacha.demo.social.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ujacha.demo.social.enums.Social;
import net.ujacha.demo.social.exception.SocialException;
import net.ujacha.demo.social.util.MapUtil;
import net.ujacha.demo.social.vo.SocialAccount;
import net.ujacha.demo.social.vo.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacebookSocialService implements ISocialService {

    @Value("${social.facebook.clientId}")
    private String clientId;

    @Value("${social.facebook.clientSecret}")
    private String clientSecret;

    @Value("${social.facebook.redirectUrl}")
    private String redirectUrl;

    private final RestTemplate restTemplate;

    @Override
    public String getOAuthUrl(HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        session.setAttribute("FACEBOOK_LOGIN_STATE", state);

        return "https://www.facebook.com/v8.0/dialog/oauth" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&state=" + state + "&scope=email";
    }

    @Override
    public Token getToken(String code, String state) throws SocialException {

        String url = "https://graph.facebook.com/v8.0/oauth/access_token" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&client_secret=" + clientSecret +
                "&code=" + code;

        ParameterizedTypeReference<Map<String, Object>> responseType =
                new ParameterizedTypeReference<Map<String, Object>>() {
                };

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        log.debug("RESPONSE: {}", response);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            Token token = new Token();
            token.setTokenType(MapUtil.get(body, "token_type", String.class));
            token.setAccessToken(MapUtil.get(body, "access_token", String.class));
            token.setExpiresIn(MapUtil.get(body, "expires_in", Integer.class));

            return token;
        } else {
            log.error("GET TOKEN ERROR: {}", response);
            throw new SocialException("fail social login");
        }

    }

    @Override
    public SocialAccount getSocialAccount(String accessToken) {

        String url = "https://graph.facebook.com/v8.0/me?fields=email,name&access_token=" + accessToken;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {

                log.debug("FACEBOOK USER: {}", response.getBody());

                JsonNode tree = objectMapper.readTree(response.getBody());

                String id = tree.path("id").asText();
                String email = tree.path("email").asText();
                String name = tree.path("name").asText();

                SocialAccount socialAccount = new SocialAccount(Social.facebook);
                socialAccount.setId(id);

                if(StringUtils.isNotBlank(email)){
                    socialAccount.setEmail(email);
                }

                if(StringUtils.isNotBlank(name)){
                    socialAccount.setName(name);
                }

                return socialAccount;

            } catch (JsonProcessingException e) {
                log.error("{}", e.getMessage());
                throw new SocialException(e);
            }

        } else {
            throw new SocialException("fail to access social user info");
        }
    }
}
