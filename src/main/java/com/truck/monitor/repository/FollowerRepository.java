package com.truck.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truck.monitor.domain.Follower;

public interface FollowerRepository extends JpaRepository<Follower, String> {
}
