package com.FullStack.WalletBanking.Registration_test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Optional;

import com.FullStack.WalletBanking.Controller.RegistrationController;
import com.FullStack.WalletBanking.Dao.Repository.AccountDetailsRepo;
import com.FullStack.WalletBanking.Dao.Repository.UserRepo;
import com.FullStack.WalletBanking.EntityUtility.User;
import com.FullStack.WalletBanking.Model.AccountDetails;
import com.FullStack.WalletBanking.Model.Domain.Role;
import com.FullStack.WalletBanking.Model.RegisterRequest;
import com.FullStack.WalletBanking.WebConfig.Token.Config.AuthenticationService;
import com.FullStack.WalletBanking.WebConfig.Token.Config.JwtService;
import com.FullStack.WalletBanking.api.AuthenticationRequest;
import com.FullStack.WalletBanking.api.AuthenticationResponse;
import org.junit.Before;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RegistrationTest {
    @Mock
    private UserRepo userRepo;

    @Mock
    private UserRepo repository;

    @Mock
    private AccountDetailsRepo accountDetailsRepo;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService registration;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setName("Test User");
        request.setPassword("testpassword");

        User us = new User();
        us.setUserId(1);
        us.setEmail("test@example.com");
        us.setRole(Role.USER);
        us.setName("Test User");
        us.setPassword("encodedpassword");

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(repository.save(any(User.class))).thenReturn(us);
        when(accountDetailsRepo.save(any(AccountDetails.class))).thenReturn(new AccountDetails());
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("generatedtoken");
        when(registration.authenticate(any(AuthenticationRequest.class))).thenReturn(new AuthenticationResponse());

        // When
        AuthenticationResponse response = registration.register(request);

        // Then
        assertNotNull(response);
        assertEquals("generatedtoken", response.getToken());
        assertEquals("Test User", response.getUser().getName());
    }

    @Test
    public void testRegisterFailure() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setName("Test User");
        request.setPassword("testpassword");

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // When
        AuthenticationResponse response = registration.register(request);

        // Then
        assertNull(response);
    }

}

