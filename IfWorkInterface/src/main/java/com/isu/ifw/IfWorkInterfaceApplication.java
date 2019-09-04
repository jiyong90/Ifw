package com.isu.ifw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.web.filter.CorsFilter;

@Configuration 
@ComponentScan(basePackages = {"com.isu"} 
	, includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CorsFilter.class)
)

@SpringBootApplication
public class IfWorkInterfaceApplication {
	@Autowired
	@Qualifier("logFilter")
    private javax.servlet.Filter logFilter;
	  
	@Autowired
	@Qualifier("authSessionFilter")
    private javax.servlet.Filter authSessionFilter;

	@Autowired
	@Qualifier("userTokenFilter")
    private javax.servlet.Filter userTokenFilter;
	
	@Autowired
	@Qualifier("httpStatusFilter")
    private javax.servlet.Filter httpStatusFilter;

//	@Autowired
//	@Qualifier("accessTokenFilter")
//    private javax.servlet.Filter accessTokenFilter;

	@Bean 
	public FilterRegistrationBean getUserTokenFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(userTokenFilter);
		registrationBean.addUrlPatterns("/*");
		//saas 연동시에는 usertokenfilter안쓰고 아래에 accesstokenfilter사용
		registrationBean.addInitParameter("freePassPath", "/error,/logout,/login,/login/certificate,/v2/api-docs,/api,/resource,/schedule,/we,/certificate,/mobile");
		registrationBean.addInitParameter("moduleId", "1");
		return registrationBean;
	    
	}
	  
	@Bean 
	public FilterRegistrationBean getFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(authSessionFilter);
		registrationBean.addUrlPatterns("/console/*");
		registrationBean.addInitParameter("tenantKeyPath", "/console");
		registrationBean.addInitParameter("moduleId", "1");
		return registrationBean;
	    
	}
	
	@Bean 
	public FilterRegistrationBean getLogFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(logFilter);
		registrationBean.addUrlPatterns("/*"); 
		return registrationBean;
	    
	}
	
//	@Bean 
//	public FilterRegistrationBean getAccessTokenFilterRegistrationBean() {
//		FilterRegistrationBean registrationBean = new FilterRegistrationBean(accessTokenFilter);
//		registrationBean.addUrlPatterns("/*"); 
//		registrationBean.addInitParameter("freePassPath", "/certificate,/error,/mobile");
//		registrationBean.addInitParameter("moduleId", "1");
//		return registrationBean;
//	    
//	}
	
	@Bean 
	public FilterRegistrationBean getHttpStatusFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(httpStatusFilter);
		registrationBean.addUrlPatterns("/*"); 
		return registrationBean;
	    
	} 
	public static void main(String[] args) {
		SpringApplication.run(IfWorkInterfaceApplication.class, args);
	}

}
