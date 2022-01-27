package com.cake.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.cake.model.Sale;
import com.cake.repository.ISaleRepository;


@Service
public class SaleService extends GenericService<Sale, String> {
	@Autowired
	private MongoTemplate mongotemplate;
	
	@Autowired
	private ISaleRepository repository;
	
	@Override
	public CrudRepository<Sale, String> getDao() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public Optional<Sale> selectId(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}
	public Sale save(Sale sale) {
		if(sale.getDate()!=null) {
			sale.setDate(LocalDateTime.now());
		}
		return repository.save(sale);
	}
	
	public List<Sale> getSaleBeewtenDate(LocalDate start, LocalDate end, String category){
		Query query = new Query();
		if(start==null&&end==null&&category==null) {
			return repository.findAll();
		}else {
			if(start!=null || end!=null) {
				query.addCriteria(Criteria.where("date").lt(end).gt(start));
			}
			if(category!=null) {
				query.addCriteria(Criteria.where("category._id").is(category));
			}
			return mongotemplate.find(query, Sale.class);
		}
	}
}
