package com.truck.monitor.repository;

import com.truck.monitor.domain.OpsUser;

public interface OpsUserRepository extends HDRepository<OpsUser> {

	OpsUser findByPhoneAndPwd(String phone, String pwd);
}
