package com.hong.recipe_finder.enums;

import com.hong.recipe_finder.dto.UserDto;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    GOOGLE("google", (attribute) -> {
        UserDto userDto = new UserDto();
        userDto.setUserName((String)attribute.get("name"));
        userDto.setEmail((String)attribute.get("email"));

        return userDto;
    }),

    NAVER("naver", (attribute) -> {
        UserDto userProfile = new UserDto();

        Map<String, String> responseValue = (Map)attribute.get("response");

        userProfile.setUserName(responseValue.get("name"));
        userProfile.setEmail(responseValue.get("email"));

        return userProfile;
    }),

    KAKAO("kakao", (attribute) -> {

        Map<String, Object> account = (Map)attribute.get("kakao_account");
        Map<String, String> profile = (Map)account.get("profile");

        UserDto userProfile = new UserDto();
        userProfile.setUserName(profile.get("nickname"));
        userProfile.setEmail((String)account.get("email"));

        return userProfile;
    });

    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, UserDto> of; // 로그인한 사용자의 정보를 통하여 UserDto 가져옴

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserDto> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserDto extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}