package webProject.togetherPartyTonight.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.user.service.UserService;
import webProject.togetherPartyTonight.global.common.DefaultResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService usersService;

}
