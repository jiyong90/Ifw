package com.isu.ifw.intf.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IfwiViewController {

	@RequestMapping(value = "/err")//, method = RequestMethod.POST)
	public ModelAndView errorPage(@RequestParam String msg, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("message", msg);
		return mv;
    }
	
}
