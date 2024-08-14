package com.hong.recipe_finder.service;

import com.hong.recipe_finder.domain.User;
import com.hong.recipe_finder.dto.UserDto;
import com.hong.recipe_finder.enums.OAuthAttributes;
import com.hong.recipe_finder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

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
        userDto.setProvider(registrationId);

        updateOrSaveUser(userDto);

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

    private void updateOrSaveUser(UserDto userProfile) {
        User user = userRepository
                .findUserByEmailAndProvider(userProfile.getEmail(), userProfile.getProvider())
                .map(value -> value.updateUser(userProfile.getUsername(), userProfile.getEmail()))
                .orElse(userProfile.toEntity());

        userRepository.save(user);
    }
}
