package com.truck.monitor.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHander {

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String, Object> exception(Exception e) {
		e.printStackTrace();
		Map<String, Object> response = new HashMap<>();
		response.put("success", false);
		response.put("errorMsg", e.getMessage());
		return response;
	}
}
