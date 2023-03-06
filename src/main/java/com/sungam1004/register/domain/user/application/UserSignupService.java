package com.sungam1004.register.domain.user.application;

import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.exception.DuplicateUserNameException;
import com.sungam1004.register.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserSignupService {

    private final UserRepository userRepository;

    public void addUser(User user) {
        if (userRepository.existsByName(user.getName()))
            throw new DuplicateUserNameException();
        userRepository.save(user);
    }
}
