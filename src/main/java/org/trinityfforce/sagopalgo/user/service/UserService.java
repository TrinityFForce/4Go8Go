package org.trinityfforce.sagopalgo.user.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.user.dto.request.SignUpRequestDto;
import org.trinityfforce.sagopalgo.user.entity.User;
import org.trinityfforce.sagopalgo.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void addUser(SignUpRequestDto requestDto) throws BadRequestException {
        checkDuplicateEmail(requestDto.getEmail());
        String encryptpassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto, encryptpassword);
        userRepository.save(user);
    }

    private void checkDuplicateEmail(String email) throws BadRequestException {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("이미 존재하는 회원입니다");
        }
    }
}
