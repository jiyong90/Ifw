package com.isu.ifw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.filter.CorsFilter;


@Configuration 
@ComponentScan(basePackages = {"com.isu", "com.pb"} 
	, includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CorsFilter.class)
)
@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
public class IfWorkServiceApplication {
	
	@Autowired
	@Qualifier("customOauthFilter")
    private javax.servlet.Filter customOauthFilter;
	


//	@Bean
//	public JedisConnectionFactory connectionFactory()
//	{
//		JedisConnectionFactory conn = new JedisConnectionFactory();
//		conn.setHostName("192.168.111.12");
//		conn.setPort(6379);
//		conn.setPassword("isu!!2019");
////		conn.setDatabase(database);
//		conn.setUsePool(true);
//		return conn;
//	}
//	
//	@Bean
//    public StringRedisTemplate redisTemplate() {
//		StringRedisTemplate redisTemplate = new StringRedisTemplate();
//        redisTemplate.setConnectionFactory(connectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
		
	public static void main(String[] args) {
		SpringApplication.run(IfWorkServiceApplication.class, args);
	}
	
	@Bean 
	public FilterRegistrationBean getCustomOauthFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(customOauthFilter);
		registrationBean.addInitParameter("freePassPath", "/login,/logout,/we,/info,/mobile,/certificate,/intf,/api");
		return registrationBean;
	    
	}
	

}

