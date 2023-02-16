package com.sungam1004.register.domain.user.application;

import com.sungam1004.register.domain.user.dto.AddUserDto;
import com.sungam1004.register.domain.user.dto.SignupUserDto;
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

    public void addUser(SignupUserDto requestDto) {
        if (userRepository.existsByName(requestDto.getName()))
            throw new DuplicateUserNameException();
        User user = requestDto.toEntity();
        userRepository.save(user);
    }

    public void addUser(AddUserDto.Request requestDto, String faceImageUri) {
        if (userRepository.existsByName(requestDto.getName()))
            throw new DuplicateUserNameException();
        User user = requestDto.toEntity(faceImageUri);
        userRepository.save(user);
    }
}
