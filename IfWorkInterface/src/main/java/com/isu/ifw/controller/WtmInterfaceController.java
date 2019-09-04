package com.isu.ifw.controller;

import java.util.List;
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
import com.isu.option.vo.ReturnParam;

@RestController
public class WtmInterfaceController {
	
	@Autowired
	private WtmInterfaceService WtmInterfaceService;
	
	@RequestMapping(value = "/code",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam codeIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		List<Map<String, Object>> ifCodeList = null;
		rp.setSuccess("");
		try {
			ifCodeList = WtmInterfaceService.getCodeIfResult(lastDataTime); //Servie 호출
			rp.put("ifData", ifCodeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
}
