package org.trinityfforce.sagopalgo.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.trinityfforce.sagopalgo.user.dto.request.SignUpRequestDto;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    public User(SignUpRequestDto requestDto, String encryptpassword) {
        this.email = requestDto.getEmail();
        this.password = encryptpassword;
        this.username = requestDto.getUsername();
        this.role = UserRoleEnum.USER;
    }

    public User(Long id, String email, String username, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role.equals("ROLE_ADMIN") ? UserRoleEnum.ADMIN : UserRoleEnum.USER;
    }

    public User(String name, String email, String password, SocialType socialType, String socialId,
        UserRoleEnum role) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
    }
}
