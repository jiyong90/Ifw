package com.isu.ifw.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.service.WtmMobileService;
import com.isu.ifw.util.Sha256;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


@RestController
public class WtmIntfController {
   
   private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
   
   @Autowired
   WtmMobileService mobileService;
   
   @Autowired
   WtmInoutService inoutService;
   
   @Autowired
   @Qualifier("WtmTenantModuleRepository")
   CommTenantModuleRepository tenantModuleRepo;
   
   @RequestMapping (value="/intf/inoutCheck", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
   public @ResponseBody Map<String,Object> inoutCheck(HttpServletRequest request)throws Exception{
   
      ReturnParam rp = new ReturnParam();
      rp.setSuccess("");
      
      SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
      Date now = new Date();
      String today = format1.format(now);
      
      logger.debug("/intf/inoutCheck s " + WtmUtil.paramToString(request));
      try {
         String apiKey = request.getParameter("apiKey");
         String secret = request.getParameter("secret");

         CommTenantModule tm = tenantModuleRepo.findByApiKey(apiKey);
      
         if(tm == null) {
            rp.setFail("secret 불일치");
            return rp;
         }

         
         String encryptCode = tm.getTenantKey().toString();
         if(encryptCode.length() < 12) {
            encryptCode = String.format("%12s", encryptCode).replaceAll(" ", "o");
         }
         String s = Sha256.getHash(secret, encryptCode, 10);
         if(!s.equals(tm.getSecret()))
         {
            rp.setFail("secret 불일치");
            return rp;
         }
         
         Map<String, Object> paramMap = new HashMap();
         paramMap.put("tenantId", tm.getTenantId());
         paramMap.put("enterCd", request.getParameter("enterCd"));
         paramMap.put("sabun", request.getParameter("emp"));
         paramMap.put("time", request.getParameter("time"));
//         paramMap.put("ymd", request.getParameter("ymd"));
         paramMap.put("type", request.getParameter("type"));
         paramMap.put("entryType", request.getParameter("deviceKey"));
         
         logger.debug("getParameter s2 " + paramMap.toString());
      
         rp = inoutService.updateTimecard(paramMap);
         
         logger.debug("/intf/inoutCheck rp : " + rp.toString());

      } catch(Exception e) {
         e.printStackTrace();
         rp.setFail("");
      }
      logger.debug("/intf/inoutCheck e " + rp.toString());
      return rp;
   }
}