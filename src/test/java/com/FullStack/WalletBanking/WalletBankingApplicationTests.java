package com.FullStack.WalletBanking;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest

class WalletBankingApplicationTests {

	@Test
	void contextLoads() {
	}

}
