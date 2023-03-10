package com.FullStack.WalletBanking.Dao.Repository;

import com.FullStack.WalletBanking.EntityUtility.UserTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RewardPointsDAO extends MongoRepository<UserTransaction,Integer> {

}
