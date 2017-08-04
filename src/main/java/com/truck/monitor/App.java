package com.truck.monitor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App extends WebMvcConfigurerAdapter {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(App.class, args);
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			        throws Exception {
				response.addHeader("Access-Control-Allow-Origin", "*");
				response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
				response.addHeader("Access-Control-Allow-Headers",
				        "Access-Control-Allow-Methods，Origin, X-Requested-With, Content-Type, Accept，Access-Control-Allow-Origin");
				return true;
			}

			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			        ModelAndView modelAndView) throws Exception {
			}

			public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			        Exception ex) throws Exception {
			}
		}).addPathPatterns("/**");
	}

	public static String trimOffUTF8(String source) {
		if (null != source) {
			return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
		}
		return null;
	}

}
