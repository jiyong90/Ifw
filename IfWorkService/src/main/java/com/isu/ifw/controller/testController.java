package com.isu.ifw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.isu.ifw.mapper.test;

@RestController
@RequestMapping(value="/api")
public class testController {

	@Autowired
	test t;
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String saveGntData() throws Exception {
		return t.getNow();
	}
	
	
	@RequestMapping(value = "/ttt", method = RequestMethod.GET)
	public ModelAndView test() throws Exception {
		System.out.println("aslkjdlkasjdklaskldjklasjdkljs");
		ModelAndView mv = new ModelAndView("test");
		return mv;
		 
	}
	
}

