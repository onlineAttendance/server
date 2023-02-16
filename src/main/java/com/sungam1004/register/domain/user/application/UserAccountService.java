package com.sungam1004.register.domain.user.application;

import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserAccountService {
    private final UserRepository userRepository;

    public void changePassword(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER));
        user.changeUserPassword(password);
    }


    public void changeFaceImage(Long userId, String faceImageUri) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER));
        user.changeFaceImageUri(faceImageUri);
    }

    public Team findTeam(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER));
        return user.getTeam();
    }
}
