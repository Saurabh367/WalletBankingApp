package com.FullStack.WalletBanking.Dao.Repository;

import com.FullStack.WalletBanking.EntityUtility.User;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends MongoRepository<User,Integer> {

    Optional<User> findByEmail(String email);
}
//
//    Query query = new Query();
//        query.addCriteria(Criteria.where("email").is(email));