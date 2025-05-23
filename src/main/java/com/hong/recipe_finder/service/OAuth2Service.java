package com.hong.recipe_finder.service;

import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.dto.UserDto;
import com.hong.recipe_finder.enums.OAuthAttributes;
import com.hong.recipe_finder.repository.UserRepository;
import com.hong.recipe_finder.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        UserDto userDto = OAuthAttributes.extract(registrationId, attributes);
        log.info("attributes = {}", attributes.toString());
        userDto.setProvider(registrationId);

        // 사용자 정보 업데이트 또는 저장
        User user = updateOrSaveUser(userDto);

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getUsername());

        // 사용자 객체에 토큰 설정
        user.setToken(token);

        // 업데이트된 사용자 정보 저장
        userRepository.save(user);

        Map<String, Object> customAttribute =
                getCustomAttribute(registrationId, userNameAttributeName, attributes, userDto);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttribute,
                userNameAttributeName);
    }

    private Map<String, Object> getCustomAttribute(String registrationId,
                                                   String userNameAttributeName,
                                                   Map<String, Object> attributes,
                                                   UserDto userProfile) {
        Map<String, Object> customAttribute = new ConcurrentHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", userProfile.getUsername());
        customAttribute.put("email", userProfile.getEmail());

        return customAttribute;
    }

    private User updateOrSaveUser(UserDto userProfile) {
        User existingUser = userRepository
                .findUserByEmailAndProvider(userProfile.getEmail(), userProfile.getProvider());
        if (existingUser != null) {
            // 기존 사용자 업데이트
            return existingUser.updateUser(userProfile.getUsername(), userProfile.getEmail());
        } else {
            // 새로운 사용자 생성 및 저장
            return userProfile.toEntity();
        }
    }

}
