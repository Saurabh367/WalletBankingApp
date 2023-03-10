package com.FullStack.WalletBanking.Dao.Repository;

import com.FullStack.WalletBanking.Model.Domain.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Optional;

public interface UserEntityRepo extends MongoRepository<UserEntity,Integer> {
    Optional<UserEntity> findByEmail(String email);

}
