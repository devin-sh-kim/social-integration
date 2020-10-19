package net.ujacha.demo.social.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ujacha.demo.social.enums.Social;
import net.ujacha.demo.social.exception.SocialException;
import net.ujacha.demo.social.util.JwtUtil;
import net.ujacha.demo.social.util.MapUtil;
import net.ujacha.demo.social.vo.SocialAccount;
import net.ujacha.demo.social.vo.Token;
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
public class GoogleSocialService implements ISocialService {


    @Value("${social.google.clientId}")
    private String clientId;

    @Value("${social.google.clientSecret}")
    private String clientSecret;

    @Value("${social.google.redirectUrl}")
    private String redirectUrl;

    private final RestTemplate restTemplate;


    @Override
    public String getOAuthUrl(HttpServletRequest request) {

        String url = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&scope=openid profile email";
        return url;
    }

    @Override
    public Token getToken(String code, String state) throws SocialException {

        String url = "https://oauth2.googleapis.com/token";

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
            token.setAccessToken(MapUtil.get(body, "access_token", String.class));
            token.setExpiresIn(MapUtil.get(body, "expires_in", Integer.class));
            token.setTokenType(MapUtil.get(body, "token_type", String.class));
            token.setScope(MapUtil.get(body, "scope", String.class));
            token.setRefreshToken(MapUtil.get(body, "refresh_token", String.class));
            token.setIdToken(MapUtil.get(body, "id_token", String.class));

            return token;
        } else {
            log.error("GET TOKEN ERROR: {}", response);
            throw new SocialException("fail social login");
        }

    }

    @Override
    public SocialAccount getSocialAccount(String accessToken) {

        String url = "https://people.googleapis.com/v1/people/me?personFields=emailAddresses,names";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {

            ObjectMapper objectMapper = new ObjectMapper();
            try {

                log.debug("GOOGLE USER: {}", response.getBody());

                JsonNode tree = objectMapper.readTree(response.getBody());

                SocialAccount socialAccount = new SocialAccount(Social.google);

                return socialAccount;

            } catch (JsonProcessingException e) {
                log.error("{}", e.getMessage());
                throw new SocialException(e);
            }

        } else {
            throw new SocialException("fail to access social user info");
        }

    }

    public SocialAccount getSocialAccountFromIdToken(String idToken) {

        log.debug("GET SOCIAL ACCOUNT FROM ID TOKEN");
        log.debug("{}", idToken);

        String payload = JwtUtil.getPayload(idToken);
        if(payload != null){

            ObjectMapper objectMapper = new ObjectMapper();
            try {

                log.debug("GOOGLE USER: {}", payload);

                JsonNode tree = objectMapper.readTree(payload);

                SocialAccount socialAccount = new SocialAccount(Social.google);

                String id = tree.path("sub").asText();
                String email = tree.path("email").asText();
                String name = tree.path("name").asText();

                socialAccount.setId(id);
                socialAccount.setEmail(email);
                socialAccount.setName(name);

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
