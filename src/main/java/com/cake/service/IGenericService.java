package com.cake.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


public interface IGenericService<T, ID extends Serializable>{
	public T save(T entity);
	public void delete(ID id);
	public T findByid(ID id);
	public List<T> getAll();
	public Optional<T> selectId(String id);
}
