package org.trinityfforce.sagopalgo.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.trinityfforce.sagopalgo.common.TestValue.*;
import static org.mockito.BDDMockito.given;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Nested
    @Order(1)
    @DisplayName("1. 회원가입 테스트")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class 회원가입테스트 {

        @Test
        @Order(1)
        @DisplayName("1-1. 회원가입 성공 테스트")
        void signUpSuccess() throws BadRequestException {
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

        @Test
        @Order(2)
        @DisplayName("1-2. 회원가입 실패 테스트")
        void signUpFailure() {
            //given
            SignUpRequestDto signUpRequestDto = new SignUpRequestDto(
                TEST_EMAIL1,
                TEST_PASSWORD1,
                TEST_USERNAME1
            );
            given(userRepository.existsByEmail(TEST_EMAIL1)).willReturn(true);

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                userService.addUser(signUpRequestDto);
            });

            //then
            assertEquals("이미 존재하는 회원입니다", exception.getMessage());
        }
    }
}
