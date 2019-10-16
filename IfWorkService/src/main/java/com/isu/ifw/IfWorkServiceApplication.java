package com.isu.ifw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration 
@ComponentScan(basePackages = {"com.isu"} 
	, includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CorsFilter.class)
)
@SpringBootApplication
public class IfWorkServiceApplication {

	@Autowired
	@Qualifier("logFilter")
    private javax.servlet.Filter logFilter;
	  
	@Autowired
	@Qualifier("authSessionFilter")
    private javax.servlet.Filter authSessionFilter;
	
	@Autowired
	@Qualifier("httpStatusFilter")
    private javax.servlet.Filter httpStatusFilter;

	@Autowired
	@Qualifier("accessTokenFilter")
    private javax.servlet.Filter accessTokenFilter;

	@Autowired
	@Qualifier("userTokenFilter")
    private javax.servlet.Filter userTokenFilter;

	@Bean 
	public FilterRegistrationBean getFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(authSessionFilter);
		registrationBean.addUrlPatterns("/console/*");
		registrationBean.addInitParameter("tenantKeyPath", "/console");
		registrationBean.addInitParameter("moduleId", "1");
		registrationBean.addInitParameter("freePassPath", "/");
		return registrationBean;
	    
	}
	
	@Bean 
	public FilterRegistrationBean getLogFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(logFilter);
		registrationBean.addUrlPatterns("/*"); 
		return registrationBean;
	    
	}
	
	@Bean 
	public FilterRegistrationBean getHttpStatusFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(httpStatusFilter);
		registrationBean.addUrlPatterns("/*"); 
		return registrationBean;
	    
	} 
	  
	public static void main(String[] args) {
		SpringApplication.run(IfWorkServiceApplication.class, args);
	}

	@Bean 
	public FilterRegistrationBean getAccessTokenFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(accessTokenFilter);
		registrationBean.addUrlPatterns("/*"); 
		registrationBean.addInitParameter("freePassPath", "/info,/certificate,/error,/mobile/logout,/login,/login/certificate,/interface");
		registrationBean.addInitParameter("moduleId", "1");
		return registrationBean;
	    
	}
	
	@Bean 
	public FilterRegistrationBean getUserTokenFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(userTokenFilter);
		registrationBean.addUrlPatterns("/*");
		registrationBean.addInitParameter("freePassPath", "/error,/logout,/login,/login/certificate,/v2/api-docs,/api,/resource,/schedule,/we,/certificate,/mobile,/interface");
		registrationBean.addInitParameter("moduleId", "1");
		return registrationBean;
	}
}

