package com.cake.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cake.model.Country;

public interface ICountryRepository extends MongoRepository<Country, String>{
	Country findByname(String country);

}