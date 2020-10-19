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
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverSocialService implements ISocialService{

    @Value("${social.naver.clientId}")
    private String clientId;

    @Value("${social.naver.clientSecret}")
    private String clientSecret;

    @Value("${social.naver.redirectUrl}")
    private String redirectUrl;

    private final RestTemplate restTemplate;

    @Override
    public String getOAuthUrl(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        session.setAttribute("NAVER_LOGIN_STATE", state);
        return "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrl + "&state=" + state;
    }

    @Override
    public Token getToken(String code, String state) throws SocialException {

        String url = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code + "&state=" + state;

        ParameterizedTypeReference<Map<String, Object>> responseType =
                new ParameterizedTypeReference<Map<String, Object>>() {};

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        log.debug("RESPONSE: {}", response);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            Token token = new Token();
            token.setTokenType(MapUtil.get(body, "token_type", String.class));
            token.setAccessToken(MapUtil.get(body, "access_token", String.class));
            token.setRefreshToken(MapUtil.get(body, "refresh_token", String.class));
            token.setExpiresIn(Integer.parseInt(MapUtil.get(body, "expires_in", String.class)));

            return token;
        } else {
            log.error("GET TOKEN ERROR: {}", response);
            throw new SocialException("fail social login");
        }

    }

    @Override
    public SocialAccount getSocialAccount(String accessToken) {

        String url = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {

                log.debug("NAVER USER: {}", response.getBody());

                JsonNode tree = objectMapper.readTree(response.getBody());

                String id = tree.path("response").path("id").asText();
                String email = tree.path("response").path("email").asText();
                String ageRange = tree.path("response").path("age").asText();
                String gender = tree.path("response").path("gender").asText();
                String name = tree.path("response").path("name").asText();


                SocialAccount socialAccount = new SocialAccount(Social.naver);
                socialAccount.setId(id);

                if(StringUtils.isNotBlank(email)){
                    socialAccount.setEmail(email);
                }

                if(StringUtils.isNotBlank(name)){
                    socialAccount.setName(name);
                }

                if(StringUtils.isNotBlank(gender)){
                    socialAccount.setGender(gender);
                }

                if (StringUtils.isNotEmpty(ageRange)) {

                    String t = ageRange.split("-")[0];
                    int i = Integer.parseInt(t);

                    if(i < 20){
                        socialAccount.setAge("10");
                    }else if(i > 79){
                        socialAccount.setAge("80");
                    }else{
                        socialAccount.setAge(String.valueOf(i));
                    }

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
