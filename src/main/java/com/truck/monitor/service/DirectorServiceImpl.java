package com.truck.monitor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.truck.monitor.domain.Director;
import com.truck.monitor.domain.User;
import com.truck.monitor.repository.DirectorRepository;
import com.truck.monitor.repository.HDRepository;
import com.truck.monitor.repository.UserRepository;

@Service
public class DirectorServiceImpl extends HDServiceImpl<Director> implements DirectorService {

	@Autowired
	private DirectorRepository repository;

	@Autowired
	private UserRepository userRepository;

	public HDRepository<Director> getRepository() {
		return repository;
	}

	public Director add(Director director) {
		List<User> users = userRepository.findByPhone(director.getPhone());
		if (users.size() > 0) {
			throw new RuntimeException("手机号[" + director.getPhone() + "]已存在.");
		}
		director.setRegTime(new Date());
		repository.save(director);
		director.setCode("ZG" + String.format("%04d", director.getId()));
		repository.save(director);
		return director;
	}

	protected Predicate toPredicate(Director director, Root<Director> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		String keyWord = director.getKeyWord();
		if (!StringUtils.isEmpty(keyWord)) {
			predicates.add(cb.or(cb.like(root.get("name").as(String.class), "%" + keyWord + "%"), cb.like(root.get("code").as(String.class), "%" + keyWord + "%"),
					cb.like(root.get("phone").as(String.class), "%" + keyWord + "%")));
		}
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));

	}

	@Override
	public void modify(Director director) {
		List<User> users = userRepository.findByPhone(director.getPhone());
		for (User user : users) {
			if (!user.getId().equals(director.getId())) {
				throw new RuntimeException("手机号[" + director.getPhone() + "]已存在.");
			}
		}
		super.modify(director);
	}

}
