package by.javaguru.integration.service;


import by.javagur.spring.database.entity.Role;
import by.javagur.spring.dto.DtoToUser;
import by.javagur.spring.dto.UserToDto;
import by.javagur.spring.service.UserService;
import by.javaguru.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


@IT
@RequiredArgsConstructor
public class UserServiceIT {

    private final static Long USER_1 = 1l;
    private final static Integer COMPANY_1 = 1;
    private final UserService userService;

    @Test
    void findAll() {
        List<UserToDto> result = userService.findAll();
        assertThat(result).hasSize(5);
    }

    @Test
    void findById() {
        Optional<UserToDto> maybeUser = userService.findById(USER_1);
        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals("ivan@gmail.com", user.getUsername()));
    }


    @Test
    void create() {
        DtoToUser userDto = new DtoToUser(
                "test@gmail.com",
                LocalDate.now(),
                "Test",
                "Test",
                Role.ADMIN,
                COMPANY_1,
                new MockMultipartFile("test", new byte[0]),
                "test"
        );
        UserToDto actualResult = userService.create(userDto);

        assertEquals(userDto.getUsername(), actualResult.getUsername());
        assertEquals(userDto.getBirthDate(), actualResult.getBirthDate());
        assertEquals(userDto.getFirstname(), actualResult.getFirstname());
        assertEquals(userDto.getLastname(), actualResult.getLastname());
        assertEquals(userDto.getCompanyId(), actualResult.getCompany().id());
        assertSame(userDto.getRole(), actualResult.getRole());
    }

    @Test
    void update() {
        DtoToUser dtoToUser = new DtoToUser(
                "test@gmail.com",
                LocalDate.now(),
                "Test",
                "Test",
                Role.ADMIN,
                COMPANY_1,
                new MockMultipartFile("test", new byte[0]),
                "test"
        );

        Optional<UserToDto> actualResult = userService.update(USER_1, dtoToUser);

        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(user -> {
            assertEquals(dtoToUser.getUsername(), user.getUsername());
            assertEquals(dtoToUser.getBirthDate(), user.getBirthDate());
            assertEquals(dtoToUser.getFirstname(), user.getFirstname());
            assertEquals(dtoToUser.getLastname(), user.getLastname());
            assertEquals(dtoToUser.getCompanyId(), user.getCompany().id());
            assertSame(dtoToUser.getRole(), user.getRole());
        });
    }

    @Test
    void delete() {
        assertFalse(userService.delete(-124L));
        assertTrue(userService.delete(USER_1));
    }

}