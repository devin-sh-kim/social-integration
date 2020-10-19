package net.ujacha.demo.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ujacha.demo.social.enums.Social;
import net.ujacha.demo.social.repository.UserRepository;
import net.ujacha.demo.social.repository.UserSocialRepository;
import net.ujacha.demo.social.util.MapUtil;
import net.ujacha.demo.social.util.PasswordEncoder;
import net.ujacha.demo.social.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserSocialRepository userSocialRepository;

    public User createUser(JoinRequestPayload payload) {

        RepositoryParam param = RepositoryParam.builder().add("payload", payload).build();
        if (StringUtils.isNotBlank(payload.getPassword())) {
            String encPassword = PasswordEncoder.md5(payload.getPassword());
            param.put("encPassword", encPassword);
        }

        userRepository.insertUser(param);
        log.debug("INSERT: {}", param);

        Long id = MapUtil.get(param, "id", Long.class);

        User user = userRepository.selectUserById(
                RepositoryParam.builder()
                        .add("id", id)
                        .build()
        );

        return user;
    }

    public User getUser(long userId) {
        return userRepository.selectUserById(RepositoryParam.builder().add("id", userId).build());
    }

    public User getUser(String email) {
        return userRepository.selectUserByEmail(RepositoryParam.builder().add("email", email).build());
    }

    public UserSocial getUserSocial(Social social, String socialAccountId) {
        return userSocialRepository.selectUserSocial(RepositoryParam.builder()
                .add("social", social)
                .add("socialAccountId", socialAccountId)
                .build());
    }

    public UserSocial createUserSocial(long userId, JoinWithSocialRequestPayload payload) {

        RepositoryParam param = RepositoryParam.builder()
                .add("userId", userId)
                .add("social", payload.getSocial())
                .add("socialAccountId", payload.getSocialId())
                .add("accessToken", payload.getAccessToken())
                .add("refreshToken", payload.getRefreshToken())
                .build();
        userSocialRepository.insertUserSocial(param);

        return this.getUserSocial(payload.getSocial(), payload.getSocialId());
    }

    @Transactional
    public User createUserWithSocial(JoinWithSocialRequestPayload payload) {
        User user = createUser(payload);
        UserSocial userSocial = createUserSocial(user.getId(), payload);
        log.debug("CREATED USER_SOCIAL: {}", userSocial);
        return user;
    }
}
