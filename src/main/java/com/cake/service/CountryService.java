package com.cake.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.cake.model.Country;
import com.cake.repository.ICountryRepository;


@Service
public class CountryService extends GenericService<Country, String> {

	@Autowired
	private ICountryRepository repository;
	
	@Override
	public CrudRepository<Country, String> getDao() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public Optional<Country> selectId(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}
	public Country findByName(String country) {
		// TODO Auto-generated method stub
		return repository.findByname(country);
	}

}
