package com.cake.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cake.model.User;

@Repository
public interface IUserRepository extends MongoRepository<User, String>{
	User findByemail(String email);
	User findByid(String id);
}
