package com.isu.ifw.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmCodeService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/api")
public class WtmApiController {

	@Autowired
	@Qualifier(value="flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;

	@RequestMapping(value="/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam test(HttpServletRequest request ) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		flexibleEmpService.createWorkteamEmpData(new Long("1"), "ISU", new Long("5"), "112313");	
		
		return rp;
	}
}
