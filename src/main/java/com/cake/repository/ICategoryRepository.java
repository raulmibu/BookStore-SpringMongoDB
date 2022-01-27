package com.cake.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cake.model.Category;

public interface ICategoryRepository extends MongoRepository<Category, String>{
	public List<Category> findByname(String title);
}