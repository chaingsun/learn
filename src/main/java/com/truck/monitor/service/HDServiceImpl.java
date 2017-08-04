package com.truck.monitor.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.truck.monitor.repository.HDRepository;

@Transactional
public abstract class HDServiceImpl<T> implements HDService<T> {

	public abstract HDRepository<T> getRepository();

	public List<T> findAll() {
		return getRepository().findAll();
	}

	public T findOne(Long id) {
		return getRepository().findOne(id);
	}

	public T add(T t) {
		return getRepository().save(t);
	}

	public void modify(T t) {
		getRepository().save(t);
	}

	public void delete(Long id) {
		getRepository().delete(id);
	}

	protected Predicate toPredicate(T t, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));

	}

	public Page<T> findByPage(final T t, Pageable pageable) {
		return getRepository().findAll(new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return HDServiceImpl.this.toPredicate(t, root, query, cb);
			}
		}, pageable);
	}

}
