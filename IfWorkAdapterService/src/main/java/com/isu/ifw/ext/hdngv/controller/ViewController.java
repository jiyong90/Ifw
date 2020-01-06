package com.isu.ifw.ext.hdngv.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ViewController {

	@RequestMapping(value = "/i", method = RequestMethod.GET)
	public ModelAndView init() {
		ModelAndView mv = new ModelAndView("test");
		return mv;
	}
	
	@RequestMapping(value = "/err", method = RequestMethod.GET)
	public ModelAndView errorPage(@RequestParam String msg) {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("message", msg);
		return mv;
	}
}
