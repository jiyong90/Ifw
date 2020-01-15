package com.isu.ifw.intf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.web.filter.CorsFilter;

@Configuration 
@ComponentScan(basePackages = {"com.isu.ifw.intf"} 
	, includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CorsFilter.class)
)

@SpringBootApplication
public class IfWorkInterfaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IfWorkInterfaceApplication.class, args);
	}

}