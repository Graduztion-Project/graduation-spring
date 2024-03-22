package com.example.demo.controller;

<<<<<<< HEAD
import com.example.demo.dto.TokenPair;
import com.example.demo.infrastructure.jwt.JwtUtil;
import com.example.demo.service.AccountService;
=======
import com.example.demo.dto.JoinRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.infrastructure.jwt.JwtUtil;
import com.example.demo.service.AccountService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.aspectj.lang.annotation.Before;
>>>>>>> feature/회원가입
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@Import({TokenFilterTest.FilterTestConfiguration.class, JwtUtil.class})
class AccountControllerTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    JwtUtil jwtUtil;
    @MockBean
    AccountService accountService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void refresh() throws Exception {
        String email = "user@test.com";
        String refreshToken = jwtUtil.createToken(0L, 1000L * 60 * 60);
        String refreshedAccessToken = jwtUtil.createToken(0L, 1000L * 60 * 60);

        when(accountService.refresh(email, refreshToken)).thenReturn(new TokenPair(refreshedAccessToken, refreshToken));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/account/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"" + email + "\"," + " \"refreshToken\":\"" + refreshToken + "\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value(refreshedAccessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }

    @Test
    void join_withInvalidEmail_throwsMethodArgumentNotValidException() throws Exception {
        String email = "usertest.com";
        String password = "password";

        testValidationEmailAndPassword(email, password);
    }

    @Test
    void join_withInvalidPassword_throwsMethodArgumentNotValidException() throws Exception {
        String email = "user@test.com";
        String password = "password";

        testValidationEmailAndPassword(email, password);

        password = "PASSWORD";
        testValidationEmailAndPassword(email, password);

        password = "pApApApApA";
        testValidationEmailAndPassword(email, password);
    }

    private void testValidationEmailAndPassword(String email, String password) throws Exception {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail(email);
        joinRequest.setPassword(password);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/account/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\": \"" + email + "\"," + " \"password\":\"" + password + "\"}")
                )
                .andExpect(status().isBadRequest());
    }

}