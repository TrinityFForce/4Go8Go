package org.trinityfforce.sagopalgo.global.security.oauth2;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.trinityfforce.sagopalgo.global.security.oauth2.dto.GoogleResponse;
import org.trinityfforce.sagopalgo.global.security.oauth2.dto.KaKaoResponse;
import org.trinityfforce.sagopalgo.global.security.oauth2.dto.NaverResponse;
import org.trinityfforce.sagopalgo.global.security.oauth2.dto.OAuth2Response;
import org.trinityfforce.sagopalgo.user.entity.SocialType;
import org.trinityfforce.sagopalgo.user.entity.User;
import org.trinityfforce.sagopalgo.user.entity.UserRoleEnum;
import org.trinityfforce.sagopalgo.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2Response = new KaKaoResponse(oAuth2User.getAttributes());
        }

        User findUser = userRepository.findBySocialTypeAndSocialId(
            SocialType.valueOf(oAuth2Response.getProvider().toUpperCase()),
            oAuth2Response.getProviderId());

        if (findUser == null) {
            String name = oAuth2Response.getName();
            String email = oAuth2Response.getEmail();
            String password = passwordEncoder.encode(UUID.randomUUID().toString());
            SocialType socialType = SocialType.valueOf(oAuth2Response.getProvider().toUpperCase());
            String socialId = oAuth2Response.getProviderId();
            UserRoleEnum role = UserRoleEnum.USER;

            User user = new User(name, email, password, socialType, socialId, role);
            userRepository.save(user);
            return new CustomOAuth2User(user);
        } else {
            return new CustomOAuth2User(findUser);
        }
    }
}