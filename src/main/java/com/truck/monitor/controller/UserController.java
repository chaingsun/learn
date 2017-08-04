package com.truck.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.Follower;
import com.truck.monitor.domain.User;
import com.truck.monitor.service.UserService;

@RestController
@RequestMapping("users")
public class UserController extends HDController<User> {

	@Autowired
	private UserService service;

	public UserService getService() {
		return service;
	}

	@RequestMapping(value = "/follower/{openid}", method = RequestMethod.GET)
	public User findByFollower(@PathVariable String openid) {
		return service.findByFollower(new Follower(openid));
	}

	/**
	 * 用户绑定微信信息.
	 * 
	 * @param openid
	 * @param example
	 * @return
	 */
	@RequestMapping(value = "/bind/{openid}", method = RequestMethod.POST)
	public HDController.Result bind(@PathVariable String openid, @RequestBody User example) {
		User self = service.findByNameAndPhoneAndCode(example.getName(), example.getPhone(), example.getCode());
		if (self == null) {
			return new Result(false, "用户信息输入错误.");
		}
		if (self.getFollower() != null) {
			return new Result(false, "该用户已被绑定.");
		}
		self.setFollower(new Follower(openid));
		service.modify(self);
		return new Result(Boolean.TRUE, self);
	}
}
