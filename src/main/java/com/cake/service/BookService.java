package com.cake.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.cake.model.Book;
import com.cake.model.Category;
import com.cake.repository.IBookRepository;

@Service
public class BookService extends GenericService<Book, String>  {

	@Autowired
	private IBookRepository repository;
	
	private final MongoTemplate mongotemplate;
	
	public BookService(MongoTemplate mongotemplate) {
		this.mongotemplate=mongotemplate;
		
	}
	
	@Override
	public CrudRepository<Book, String> getDao() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public Optional<Book> selectId(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}
	
	public Book save(Book element) {
		if(element.getCreated()==null) {
			element.setCreated(LocalDateTime.now());
		}
		element.getCategory().setDescription(null);
		element.getCategory().setImage(null);
		Book ob=repository.save(element);
		return ob;
	}

	@Override
	public void delete(String id) {
		repository.deleteById(id);
	}

	public List<Book> findBookBytitle(String title) {
		return repository.findBytitle(title);
		
	}

	
	public List<Book> search(String searching) {
//		TextIndexDefinition textIndex = new TextIndexDefinitionBuilder()
//	    		  .onField("title", 5F)
//	    		  .onField("category.name", 3F)
//	    		  .onField("category.description" , 1F)
//	    		  .onField("country.name" , 0.5F)
//	    		  .onField("year", 0.2F)
//	    		  .onField("isbn", 4F)
//	    		  .onField("publisher", 1.1F)
//	    		  .onField("synopsis", 1.5F)
//	    		  .onField("author", 2.5F)
//	    		  .onField("overview", 2F)
//	    		  .build();
//		mongotemplate.indexOps(Book.class).ensureIndex(textIndex);
		Pattern pattern = Pattern.compile("[^a-z A-Z]");
	    Matcher matcher = pattern.matcher(searching);
	    String onlyString = matcher.replaceAll("");
	    
	    pattern = Pattern.compile("[^0-9]");
	    matcher = pattern.matcher(searching);
	    String onlyNumber = matcher.replaceAll("");
	    
	    List<String> parameters=Arrays.asList(
	    		"title",
	    		"category.name",
	    		"category.description",
	    		"country.name",
	    		"year",
	    		"isbn",
	    		"publisher",
	    		"synopsis",
	    		"author",
	    		"overview");
		Query query = new Query();
		List<Criteria> orCriterias=new ArrayList<>();
		Iterator<String> adding=parameters.iterator();
		while(adding.hasNext()) {
			String te=adding.next();
			if(onlyString.compareTo("")!=0) {
				orCriterias.add(Criteria.where(te).regex(onlyString,"i"));
			}
			if(onlyNumber.compareTo("")!=0) {
				orCriterias.add(Criteria.where(te).regex(onlyNumber,"i") );
			}
		
		}
		query.addCriteria(new Criteria().orOperator(orCriterias).and("enabled").is(true));
//		//make sure the book is enabled
	    
//	    TextCriteria criteria = TextCriteria.forDefaultLanguage().
//	    		  matchingAny(onlyNumber, onlyString, searching);
//
//	    		Query query = TextQuery.queryText(criteria)
//	    		  .sortByScore();
		return mongotemplate.find(query, Book.class);
	}
	
	public List<Book> queryById(Set<String> setid, int quanty){
		if(setid.size()>0) {
			Query query = new Query();
			List<String> list=new ArrayList<>(setid);
			Collections.reverse(list);
			Iterator<String>  value=list.iterator();
			List<Criteria> criterias=new ArrayList<>();
			while (value.hasNext() && quanty >0) {
				quanty--;
				criterias.add(Criteria.where("_id").is(value.next()));
	        }
			query.addCriteria(new Criteria().orOperator(criterias));
			return mongotemplate.find(query, Book.class);
		}else {
			return new ArrayList<Book>();
		}
	}
	
	public List<Book> findQuery(String field, String searching) {
		Query query = new Query();
		query.addCriteria(Criteria.where(field).regex(searching,"i"));
		return mongotemplate.find(query, Book.class);
	}
	
	public void updateBought(String book, int quanty) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(book));
		Update update = new Update();
		update.inc("bought", quanty);
		update.inc("stock",-quanty);
		//List<Update> updates=new ArrayList<>();
		mongotemplate.updateFirst(query, update,Book.class);
	}

	public void updateCategory(Category category) {
		Query query = new Query();
		query.addCriteria(Criteria.where("category._id").is(category.getId()));
		Update update = new Update();
		update.set("category", category);
		mongotemplate.updateMulti(query, update,Book.class);
	}
	
	public Page<Book> getAllByPages(Integer page, Integer size, String orderby){
		Pageable pageable = PageRequest.of( page, size);
	    Page<Book> books = repository.findAll(pageable);
        return books;
	}
	
	public Page<Book> findAllActive(Integer page, Integer size, String orderby){
		Pageable pageable = PageRequest.of( page, size);
//	    List<Book> persons = repository.findByEnabledIsTrue();
	    Page<Book> persons = repository.findByEnabled(true,pageable);
	    return persons;
	}
	
}
