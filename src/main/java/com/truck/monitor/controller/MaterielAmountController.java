package com.truck.monitor.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.truck.monitor.domain.MaterielAmount;
import com.truck.monitor.service.HDService;
import com.truck.monitor.service.MaterielAmountService;

@Controller
@RequestMapping(value = "/materielamounts")
public class MaterielAmountController {

	@Autowired
	private MaterielAmountService service;

	public HDService<MaterielAmount> getService() {
		return service;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public void modify(@RequestBody List<MaterielAmount> amounts, HttpServletResponse response) {
		service.modify(amounts);
		HDController.responseSuccess(response);
	}

}
