package com.FullStack.WalletBanking.Dao.Repository;
import com.FullStack.WalletBanking.Model.AccountDetails;
import com.FullStack.WalletBanking.Model.Domain.UserEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
 public interface AccountDetailsRepo extends MongoRepository<AccountDetails,Integer> {

 Optional<AccountDetails> findByDetails_Email(String email);
 AccountDetails findByDetailsEmail(String email);


}
