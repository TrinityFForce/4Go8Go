package org.trinityfforce.sagopalgo.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.trinityfforce.sagopalgo.common.TestValue.*;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.trinityfforce.sagopalgo.user.dto.request.SignUpRequestDto;
import org.trinityfforce.sagopalgo.user.entity.User;
import org.trinityfforce.sagopalgo.user.repository.UserRepository;
import org.trinityfforce.sagopalgo.user.service.UserService;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입")
    void signUp() throws BadRequestException {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(
            TEST_EMAIL1,
            TEST_PASSWORD1,
            TEST_USERNAME1
        );

        //when
        userService.addUser(signUpRequestDto);

        //then
        verify(userRepository, times(1)).save(any(User.class));
    }
}
