/*
 * Copyright 2015-2020 reserved by jf61.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.truck.monitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.truck.monitor.domain.Follower;
import com.truck.monitor.domain.User;

/**
 * @desc
 * @author auto-generator
 */
public interface UserRepository extends HDRepository<User> {

	@Query("select new User(u.id, u.phone, u.name) from User u where u.phone = ?1")
	List<User> findByPhone(String phone);

	User findByFollower(Follower follower);

	User findByNameAndPhoneAndCode(String name, String phone, String code);

}
