package com.sungam1004.register.domain.service;

import com.sungam1004.register.domain.dto.AddUserDto;
import com.sungam1004.register.domain.dto.SignupUserDto;
import com.sungam1004.register.domain.entity.User;
import com.sungam1004.register.domain.repository.UserRepository;
import com.sungam1004.register.global.exception.CustomException;
import com.sungam1004.register.global.exception.ErrorCode;
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
            throw new CustomException(ErrorCode.DUPLICATE_USER_NAME);
        User user = requestDto.toEntity();
        userRepository.save(user);
    }

    public void addUser(AddUserDto.Request requestDto, String faceImageUri) {
        if (userRepository.existsByName(requestDto.getName()))
            throw new CustomException(ErrorCode.DUPLICATE_USER_NAME);
        User user = requestDto.toEntity(faceImageUri);
        userRepository.save(user);
    }
}
