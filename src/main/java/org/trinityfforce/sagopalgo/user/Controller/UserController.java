package org.trinityfforce.sagopalgo.user.Controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trinityfforce.sagopalgo.user.dto.request.SignUpRequestDto;
import org.trinityfforce.sagopalgo.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity addUser(@RequestBody SignUpRequestDto requestDto)
        throws BadRequestException {
        userService.addUser(requestDto);
        return ResponseEntity.ok().build();
    }
}
