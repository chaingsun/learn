package com.truck.monitor.service;

import com.truck.monitor.domain.OpsUser;

public interface OpsUserService extends HDService<OpsUser> {

	OpsUser findByPhoneAndPwd(String phone, String pwd);
}
