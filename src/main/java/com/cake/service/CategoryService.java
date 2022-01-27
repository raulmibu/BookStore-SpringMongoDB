package com.cake.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.cake.model.Category;
import com.cake.repository.ICategoryRepository;


@Service
public class CategoryService extends GenericService<Category, String> {

	@Autowired
	private ICategoryRepository repository;
	@Autowired
	private BookService bookservice;
	
	@Override
	public CrudRepository<Category, String> getDao() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public Optional<Category> selectId(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}
	public List<Category> findByname(String category) {
		// TODO Auto-generated method stub
		return repository.findByname(category);
	}
	
	public Category save(Category category) {
		Category saved= repository.save(category);
		bookservice.updateCategory(saved);
		return saved;
	}

}
