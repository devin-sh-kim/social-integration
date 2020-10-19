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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoSocialService implements ISocialService {

    @Value("${social.kakao.clientId}")
    private String clientId;

    @Value("${social.kakao.clientSecret}")
    private String clientSecret;

    @Value("${social.kakao.redirectUrl}")
    private String redirectUrl;


    private final RestTemplate restTemplate;

    @Override
    public String getOAuthUrl(HttpServletRequest request) {
        return  "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrl;
    }

    @Override
    public Token getToken(String code, String state) throws SocialException {

        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("redirect_uri", redirectUrl);
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ParameterizedTypeReference<Map<String, Object>> responseType =
                new ParameterizedTypeReference<Map<String, Object>>() {
                };

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);

        log.debug("RESPONSE: {}", response);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            Token token = new Token();
            token.setTokenType(MapUtil.get(body, "token_type", String.class));
            token.setAccessToken(MapUtil.get(body, "access_token", String.class));
            token.setExpiresIn(MapUtil.get(body, "expires_in", Integer.class));
            token.setRefreshToken(MapUtil.get(body, "refresh_token", String.class));
            token.setRefreshTokenExpiresIn(MapUtil.get(body, "refresh_token_expires_in", Integer.class));
            token.setScope(MapUtil.get(body, "scope", String.class));

            return token;
        } else {
            log.error("GET TOKEN ERROR: {}", response);
            throw new SocialException("fail social login");
        }
    }

    @Override
    public SocialAccount getSocialAccount(String accessToken) {

        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {

            ObjectMapper objectMapper = new ObjectMapper();
            try {

                log.debug("KAKAO USER: {}", response.getBody());

                JsonNode tree = objectMapper.readTree(response.getBody());

                int id = tree.get("id").asInt();
                String email = tree.path("kakao_account").path("email").asText();
                String ageRange = tree.path("kakao_account").path("age_range").asText();
                String gender = tree.path("kakao_account").path("gender").asText();

                log.debug("{}, {}, {}, {}", id, email, ageRange, gender);
                SocialAccount socialAccount = new SocialAccount(Social.kakao);

                socialAccount.setId(String.valueOf(id));

                if (StringUtils.isNotEmpty(email)) {
                    socialAccount.setEmail(email);
                }

                if (StringUtils.isNotEmpty(ageRange)) {

                    String t = ageRange.split("~")[0];
                    int i = Integer.parseInt(t);

                    if(i < 20){
                        socialAccount.setAge("10");
                    }else if(i > 79){
                        socialAccount.setAge("80");
                    }else{
                        socialAccount.setAge(String.valueOf(i));
                    }

                }

                if (StringUtils.isNotEmpty(gender)) {
                    socialAccount.setGender(gender.substring(0,1).toUpperCase());
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
