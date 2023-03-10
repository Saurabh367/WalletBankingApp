package com.FullStack.WalletBanking.Dao.Repository;

import com.FullStack.WalletBanking.Model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {
//    List<Transaction> findByUserId(int userId);
}
