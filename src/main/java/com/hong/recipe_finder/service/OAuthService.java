package com.hong.recipe_finder.service;

import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;



@Service
public class OAuthService extends DefaultOAuth2UserService {

    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자 정보 추출
        String provider = userRequest.getClientRegistration().getRegistrationId();  // kakao
        String providerId = oAuth2User.getAttribute("id");  // 카카오톡에서 제공한 ID
        String email = oAuth2User.getAttribute("email");

        // 사용자 정보 확인 및 등록
        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setProvider(provider);
                    newUser.setProviderId(providerId);
                    newUser.setEmail(email);  // 이메일이 제공된 경우만 설정
                    return userRepository.save(newUser);
                });

        return oAuth2User;  // 또는 필요한 경우 사용자 정보 반환
    }

    public boolean authenticate(String provider, String token) {
        return true;
    }
}
