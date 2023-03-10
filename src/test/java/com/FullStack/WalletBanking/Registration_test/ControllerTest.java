//package com.FullStack.WalletBanking.Registration_test;
//
//import com.FullStack.WalletBanking.Dao.RepoImplementation.RegistrationDaoImpl;
//import com.FullStack.WalletBanking.Dao.Repository.AccountDetailsRepo;
//import com.FullStack.WalletBanking.Dao.Repository.TransactionRepository;
//import com.FullStack.WalletBanking.EntityUtility.AccDetailTemp;
//import com.FullStack.WalletBanking.Model.AccountDetails;
//import com.FullStack.WalletBanking.Model.Domain.Role;
//import com.FullStack.WalletBanking.Model.Domain.UserEntity;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//
//import java.util.NoSuchElementException;
//
//import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
//import static org.springframework.http.RequestEntity.delete;
//import static org.springframework.http.RequestEntity.post;
//import static org.springframework.test.util.AssertionErrors.assertEquals;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class RegistrationDaoImplTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private AccountDetailsRepo accountDetailsRepo;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private RegistrationDaoImpl registrationDaoImpl;
//
//    @BeforeEach
//    public void setup() {
//        accountDetailsRepo.deleteAll();
//        transactionRepository.deleteAll();
//    }
//
//    @Test
//    public void testCreateAccount() throws Exception {
//        // Given
//        AccDetailTemp accDetailTemp = new AccDetailTemp();
//        accDetailTemp.setDetails(new UserEntity("testuser", "password", "Test User", "testuser@example.com", Role.USER));
//        String requestBody = objectMapper.writeValueAsString(accDetailTemp);
//
//        // When
//        MvcResult mvcResult = mockMvc.perform((RequestBuilder) post("/account")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.valueOf(requestBody)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Then
//        AccountDetails accountDetails = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountDetails.class);
//        assertEquals(1, accountDetailsRepo.count());
//        assertEquals(accDetailTemp.getDetails().getName(), accountDetails.getDetails().getName());
//        assertEquals(accDetailTemp.getDetails().getEmail(), accountDetails.getDetails().getEmail());
//    }
//
//    @Test
//    public void testCreateAccountDuplicateEmail() throws Exception {
//        // Given
//        AccDetailTemp accDetailTemp1 = new AccDetailTemp();
//        accDetailTemp1.setDetails(new UserEntity(11, "password", "Test User 1", "testuser@example.com",12334599996));
//        String requestBody1 = objectMapper.writeValueAsString(accDetailTemp1);
//        mockMvc.perform(post("/account")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body((requestBody1))
//                .andExpect(status().isOk());
//
//        AccDetailTemp accDetailTemp2 = new AccDetailTemp();
//        accDetailTemp2.setDetails(new UserEntity(2, "password", "Test User 2", "testuser@example.com", 42342332222));
//        String requestBody2 = objectMapper.writeValueAsString(accDetailTemp2);
//
//        // When
//        mockMvc.perform(post("/account")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody2))
//                .andExpect(status().isConflict())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateKeyException))
//                .andExpect(result -> assertEquals("Email address already exists", result.getResolvedException().getMessage()));
//
//        // Then
//        assertEquals(1, accountDetailsRepo.count());
//    }
//
//    @Test
//    public void testDeleteAccount() throws Exception {
//        // Given
//        AccDetailTemp accDetailTemp = new AccDetailTemp();
//        accDetailTemp.setDetails(new UserEntity("testuser", "password", "Test User", "testuser@example.com", Role.USER));
//        AccountDetails accountDetails = registrationDaoImpl.createAccount(accDetailTemp);
//        int accountId = accountDetails.getAccNumber();
//
//        // When
//        mockMvc.perform(delete("/account/" + accountId))
//                .andExpect(status().isOk());
//
//        // Then
//        assertEquals(0, accountDetailsRepo.count());
//    }
//
//    @Test
//    public void testDeleteAccountNotFound() throws Exception {
//        // When
//        mockMvc.perform(delete("/account/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
//                .andExpect(result -> assertEquals("No value present", result.getResolvedException().getMessage()));
//
//        // Then
//        assertEquals(0, accountDetailsRepo.count());
//    }
//}
//
//
