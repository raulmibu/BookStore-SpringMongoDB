package com.cake.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cake.model.Book;

public interface IBookRepository extends PagingAndSortingRepository<Book, String>, MongoRepository<Book, String> {
	public List<Book> findBytitle(String title);
	public List<Book> findByEnabledIsTrue();
	Page<Book> findByEnabled(boolean published, Pageable pageable);
}