package com.truck.monitor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.truck.monitor.domain.OpsUser;
import com.truck.monitor.domain.User;
import com.truck.monitor.service.OpsUserService;

@Controller
@RequestMapping(value = "/")
public class IndexController {

	@Autowired
	private OpsUserService opsUserService;

	/**
	 * 后台用户登录.
	 * 
	 * @param example
	 * @param request
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/ops/login")
	public void opsLogin(@RequestBody OpsUser example, HttpServletRequest request, HttpServletResponse response) {
		OpsUser self = opsUserService.findByPhoneAndPwd(example.getPhone(), example.getPwd());
		if (self == null || !User.Type.OPS.equals(self.getType())) {
			throw new RuntimeException("用户名或密码错误.");
		}
		if (!new Integer(1).equals(self.getStatus())) {
			throw new RuntimeException("该用户未激活.");
		}
		request.getSession().setAttribute(Constants.OPS, self);
		HDController.responseSuccess(response);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ops/self")
	public void self(HttpServletRequest request, HttpServletResponse response) {
		OpsUser self = (OpsUser) request.getSession().getAttribute(Constants.OPS);
		if (self == null) {
			HDController.response(response, "var _self = null;");
		} else {
			HDController.response(response, "var _self = " + JSON.toJSONString(self) + ";");
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ops/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		HDController.responseSuccess(response);
	}
}
