package com.cake.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cake.auth.Role;
import com.cake.model.Category;
import com.cake.model.Sale;
import com.cake.model.User;
import com.cake.repository.IUserRepository;

@Service
public class UserService extends GenericService<User, String> {
	private final MongoTemplate mongotemplate;

	@Autowired
	private IUserRepository repository;

	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	public CrudRepository<User, String> getDao() {
		// TODO Auto-generated method stub
		return repository;
	}
	
	public UserService(MongoTemplate mongotemplate) {
		this.mongotemplate=mongotemplate;
	}
	
	public void updateHistory(User currentUser) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(currentUser.getId()));
		Update update = new Update();
		update.set("history", currentUser.getHistory());
		mongotemplate.updateFirst(query, update,User.class);
	}
	
	public User save(User element) {
		if(element.getCreated()==null) {
			element.setCreated(LocalDateTime.now());
			element.setPassword(bcrypt.encode(element.getPassword()));
		}
		User ob=repository.save(element);
		return ob;
	}
	

	public User findByemail(String email) {
		// TODO Auto-generated method stub
		return repository.findByemail(email);
	}

	@Override
	public Optional<User> selectId(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	public void updateBought(Set<Sale> sale, String userid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(userid));
		Update update = new Update();
		update.set("bought", sale);
		mongotemplate.updateFirst(query, update,User.class);		
	}

	public List<User> getUsers(Role role) {
		Set<Role> filter=new HashSet<Role>();
		filter.add(Role.CUSTOMER);
		if(role==Role.ADMIN) {
			filter.add(Role.EMPLOYEE);
		}
		if(filter.size()>0) {
			Query query = new Query();
			Iterator<Role>  value=filter.iterator();
			List<Criteria> criterias=new ArrayList<>();
			while (value.hasNext()) {
				criterias.add(Criteria.where("role").is(value.next().name()));
	        }
			query.addCriteria(new Criteria().orOperator(criterias));
			return mongotemplate.find(query, User.class);
		}else {
			return new ArrayList<User>();
		}
	}

	public User getOneUser(String id, Role role) {
		Query query = new Query();
		List<Criteria> criterias=criteriaWithRoleFilter(role);
		if(!criterias.isEmpty() && criterias!=null) {
			query.addCriteria(new Criteria().orOperator(criterias));
			query.addCriteria(Criteria.where("_id").is(id));
			return mongotemplate.findOne(query, User.class);
		}
		return new User();
		
	}
	private List<Criteria> criteriaWithRoleFilter(Role role) {
		Set<Role> filter=new HashSet<Role>();
		filter.add(Role.CUSTOMER);
		if(role==Role.ADMIN) {
			filter.add(Role.EMPLOYEE);
		}
		if(filter.size()>0) {
			Iterator<Role>  value=filter.iterator();
			List<Criteria> criterias=new ArrayList<>();
			while (value.hasNext()) {
				criterias.add(Criteria.where("role").is(value.next().name()));
	        }
			return criterias;
		}else{
			return null;
		}
	}

	public void updateStringType(String field,String profilePicture, String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Update update = new Update();
		update.set(field, profilePicture);
		mongotemplate.updateFirst(query, update,User.class);	
	}

	public String encodePassword(String password) {
		if(password!=null) {
			password=bcrypt.encode(password);
		}
		return password;
	}

	public void updateFavoriteCategories(Set<Category> set, String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Update update = new Update();
		update.set("favoriteCategories", set);
		mongotemplate.updateFirst(query, update,User.class);	
		
	}



	
}