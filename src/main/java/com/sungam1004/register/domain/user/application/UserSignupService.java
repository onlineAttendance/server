package com.sungam1004.register.domain.user.application;

import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.exception.DuplicateUserNameException;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.manager.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public void addUser(User user) {
        validUserName(user.getName());

        String encryptedPassword = passwordEncoder.encrypt(user.getPassword());
        user.updateUserPassword(encryptedPassword);
        userRepository.save(user);
    }

    private void validUserName(String name) {
        if (userRepository.existsByName(name)) {
            throw new DuplicateUserNameException();
        }
    }
}
