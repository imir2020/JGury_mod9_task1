package by.javaguru.integration.http.controller;

import by.javaguru.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockUser(username = "test@gmail.com", password = "test", authorities = {"ADMIN", "OPERATOR", "USER"})////
public class UserControllerIT {
    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void registration() throws Exception {
        mockMvc.perform(get("/users/registration"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user", "roles", "companies"));
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "test", authorities = {"ADMIN", "OPERATOR"})
    void findById() throws Exception {
        Integer uriVariable = 1;
        mockMvc.perform(get("/users/{id}", uriVariable))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/user"))
                .andExpect(model().attributeExists("user", "roles", "companies"));
    }


    @Test
    void findUserImagesByUserId() throws Exception {
        mockMvc.perform(get("/users/{userId}/userImages", 2))
                .andExpect(view().name("user/userImage"))
                .andExpect(model().attributeExists("user", "imageList"))
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
                        .param("username", "test@gmail.com")
                        .param("firstname", "Test")
                        .param("lastname", "TestTest")
                        .param("role", "ADMIN")
                        .param("companyId", "1")
                        .param("image", "one.jpg")
                        .param("birthDate", "2000-01-01")
                        .param("password", "123")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/*"));//{\d+}
    }


    //TODO NOT WORKED!!!
    @Test
    void update() throws Exception {
        Integer uriVariable = 1;
        MockMultipartFile image = new MockMultipartFile("file", "test.jpg",
                "image/jpeg", "test content".getBytes());
        System.out.println(Arrays.toString(image.getBytes()) + " test");

        mockMvc.perform(multipart("/users/{d}/update", uriVariable)
                .file(image)
                        .param("username", "test@gmail.com")
                        .param("firstname", "Test")
                        .param("lastname", "TestTest")
                        .param("role", "ADMIN")
                        .param("companyId", "1")
                        .param("image", String.valueOf(image))
                        .param("birthDate", "2000-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/users/*"));
    }


    //TODO NOT WORKED!!!
    @Test
    void addImage() throws Exception {
        Integer uriVariable = 1;
        MockMultipartFile image = new MockMultipartFile("file", "test.jpg",
                "image/jpeg", "test content".getBytes());
        System.out.println(Arrays.toString(image.getBytes()) + " test");

        mockMvc.perform(multipart("/users/{userId}/userImages/addImage", uriVariable)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/users/*/userImages"));
    }


    //TODO NOT WORKED!!!
    @Test
    void removeImage() throws Exception {
        Integer userId = 1;
        Integer imageId = 1;
        MockMultipartFile image = new MockMultipartFile("file", "test.jpg",
                "image/jpeg", "test content".getBytes());

        mockMvc.perform(multipart("/users/{userId}/userImages/{imageId}/removeImage", userId, imageId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(redirectedUrlPattern("/users/*/userImages"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void delete() throws Exception {
        Integer id = 1;
        mockMvc.perform(post("/users/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

    }
}
