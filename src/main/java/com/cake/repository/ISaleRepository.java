package com.cake.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cake.model.Sale;

public interface ISaleRepository extends MongoRepository<Sale, String>{
	
}