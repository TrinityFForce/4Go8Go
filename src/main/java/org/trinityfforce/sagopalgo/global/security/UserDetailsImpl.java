package org.trinityfforce.sagopalgo.global.security;

import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.Collection;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.trinityfforce.sagopalgo.user.entity.User;

@NoArgsConstructor

public class UserDetailsImpl implements UserDetails {

    private User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public UserDetailsImpl(Claims claims){
        Long id = claims.get("id", Long.class);
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        String role = claims.get("role", String.class);

        this.user = new User(id,email,name,role);
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getUserName(){return user.getUsername();}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().getAuthority();
            }
        });

        return collection;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
