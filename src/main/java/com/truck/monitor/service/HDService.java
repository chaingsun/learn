package com.truck.monitor.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HDService<T> {

	List<T> findAll();

	T findOne(Long id);

	T add(T t);

	void modify(T t);

	void delete(Long id);

	Page<T> findByPage(final T t, Pageable pageable);

}
