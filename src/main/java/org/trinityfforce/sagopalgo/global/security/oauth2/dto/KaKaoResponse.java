package org.trinityfforce.sagopalgo.global.security.oauth2.dto;

import java.util.Map;

public class KaKaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KaKaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");
        return account.get("email").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return profile.get("nickname").toString();
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return profile.get("thumbnail_image_url").toString();
    }
}
