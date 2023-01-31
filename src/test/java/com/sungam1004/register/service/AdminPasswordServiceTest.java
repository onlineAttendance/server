package com.sungam1004.register.service;

import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.manager.PasswordManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class AdminPasswordServiceTest {
    @Mock
    PasswordManager passwordManager;

    @InjectMocks
    AdminPasswordService adminPasswordService;

    @Test
    @DisplayName("Admin password 검증 - 성공")
    void loginAdminSuccess() {
        String password = "tjddkaryghl";
        //given
        Mockito.when(passwordManager.isCorrectAdminPassword(password))
                .thenReturn(true);

        //when then
        adminPasswordService.loginAdmin(password);
    }

    @Test
    @DisplayName("Admin password 검증 - 실패")
    void loginAdminFail() {
        //given
        String password = "tjddkaryghl";
        Mockito.when(passwordManager.isCorrectAdminPassword(password))
                .thenReturn(false);

        //when
        CustomException customException =
                assertThrows(CustomException.class, () -> adminPasswordService.loginAdmin(password));

        //then
        assertEquals(customException.getError(), ErrorCode.INCORRECT_PASSWORD);
    }

    @Test
    @DisplayName("user password 변경 - 성공")
    void changeUserPasswordSuccess() {
        //given
        String password = "tjddkaryghl";
        Mockito.doNothing().when(passwordManager).changeUserPassword(password);

        //when then
        adminPasswordService.changeUserPassword(password);
    }

    @Test
    @DisplayName("user password 변경 - 실패")
    void changeUserPasswordFail() {
        //given
        String password = "tjddkaryghl";
        Mockito.doThrow(new CustomException(ErrorCode.NOT_FORMAT_MATCH_USER_PASSWORD))
                .when(passwordManager).changeUserPassword(password);

        //when
        CustomException customException = assertThrows(CustomException.class, () -> {
            adminPasswordService.changeUserPassword(password);
        });

        //then
        assertEquals(customException.getError(), ErrorCode.NOT_FORMAT_MATCH_USER_PASSWORD);
    }

    @Test
    @DisplayName("admin password 변경 - 성공")
    void changeAdminPasswordSuccess() {
        //given
        String password = "tjddkaryghl";
        Mockito.doNothing().when(passwordManager).changeAdminPassword(password);

        //when then
        adminPasswordService.changeAdminPassword(password);
    }

    @Test
    @DisplayName("user password 변경 - 실패")
    void changeAdminPasswordFail() {
        //given
        String password = "tjddkaryghl";
        Mockito.doThrow(new CustomException(ErrorCode.NOT_FORMAT_MATCH_ADMIN_PASSWORD))
                .when(passwordManager).changeAdminPassword(password);

        //when
        CustomException customException = assertThrows(CustomException.class, () -> {
            adminPasswordService.changeAdminPassword(password);
        });

        //then
        assertEquals(customException.getError(), ErrorCode.NOT_FORMAT_MATCH_ADMIN_PASSWORD);
    }


}