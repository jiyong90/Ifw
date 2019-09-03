package com.isu.ifw.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmInterfaceService;

@RestController
public class WtmInterfaceController {
	
	@Autowired
	private WtmInterfaceService WtmInterfaceService;
	
	@RequestMapping(value = "/code",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void codeIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		WtmInterfaceService.getCodeIfResult(lastDataTime); //Servie 호출
		return;
	}
}
