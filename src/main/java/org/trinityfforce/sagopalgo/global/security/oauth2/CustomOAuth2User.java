package org.trinityfforce.sagopalgo.global.security.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.trinityfforce.sagopalgo.user.entity.User;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final User user;

    public User getUser() {
        return user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return user.getRole().toString();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

}
