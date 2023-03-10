package com.FullStack.WalletBanking.Model.Domain;

import com.FullStack.WalletBanking.EntityUtility.UserStatus;
import com.FullStack.WalletBanking.Model.AccountDetails;
import com.FullStack.WalletBanking.WebConfig.Token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.List;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Document(collection ="UserEntity")
public class UserEntity {
    @Transient
    public static final String SEQUENCE_USER="user_sequence";
    @Id
    private int userId;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @NotNull
    @Size(min = 5, max = 25)
    private String email;
    private String name;
    private String password;
    private Long phoneNumber;
    List<AccountDetails> userTransactionEntities;
@Enumerated(EnumType.STRING)
    private Role  role;

    public UserEntity(int userId, String email, String name, String password, Long phoneNumber ) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
//        this.userTransactionEntities = userTransactionEntities;
    }


}
