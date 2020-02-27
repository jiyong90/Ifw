package com.isu.ifw.intf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.filter.CorsFilter;

@Configuration 
@ComponentScan(basePackages = {"com.isu.ifw.intf"} 
	, includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CorsFilter.class)
)

@SpringBootApplication
public class IfWorkInterfaceApplication {

	@Autowired
	@Qualifier("encryptFilter")
    private javax.servlet.Filter encryptFilter;
	
	
	@Bean 
	public FilterRegistrationBean getEncryptFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(encryptFilter);
		registrationBean.addInitParameter("encoding", "utf-8");
		registrationBean.addInitParameter("freePassPath", "/login,/certificate");
		return registrationBean;
	    
	}
	
	public static void main(String[] args) {
		SpringApplication.run(IfWorkInterfaceApplication.class, args);
	}

}