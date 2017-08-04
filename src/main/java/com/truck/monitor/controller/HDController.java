package com.truck.monitor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.truck.monitor.service.HDService;

public abstract class HDController<T> {

	public static final HDController.Result SUCCESS = new HDController.Result(true, "操作成功.");

	public abstract HDService<T> getService();

	public static void response(HttpServletResponse response, String json) {
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(json);
		out.flush();
		out.close();
	}

	public static void responseSuccess(HttpServletResponse response) {
		response(response, "{\"success\":true}");
	}

	public static class Result {
		public Result() {

		}

		public Result(boolean success, String msg) {
			this.success = success;
			this.msg = msg;
		}

		public Result(boolean success, Object data) {
			this.success = success;
			this.data = data;
		}

		private boolean success;

		private String msg;

		private Object data;

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}

	}

	/**
	 * 查询全部.
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<T> findAll() {
		return getService().findAll();
	}

	/**
	 * 单个查询.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public T findOne(@PathVariable Long id) {
		return getService().findOne(id);
	}

	/**
	 * 单个删除.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Result delete(@PathVariable final Long id) {
		getService().delete(id);
		return SUCCESS;
	}

	/**
	 * 添加.
	 * 
	 * @param t
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Result add(@RequestBody T t) {
		getService().add(t);
		return SUCCESS;
	}

	/**
	 * 修改.
	 * 
	 * @param t
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public Result modify(@RequestBody T t) {
		getService().modify(t);
		return SUCCESS;
	}

	/**
	 * 分页查询.
	 * 
	 * @param t
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@ResponseBody
	public Page<T> findByPage(T t, @PageableDefault(sort = { "id" }, page = 0, size = 20, direction = Direction.DESC) Pageable pageable) {
		return getService().findByPage(t, pageable);
	}

}
