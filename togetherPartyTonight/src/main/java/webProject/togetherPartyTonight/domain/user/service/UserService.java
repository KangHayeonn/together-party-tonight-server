package webProject.togetherPartyTonight.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

}
