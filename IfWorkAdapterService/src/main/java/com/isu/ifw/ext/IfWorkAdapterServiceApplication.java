package com.isu.ifw.ext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.filter.CorsFilter;

@Configuration 
@ComponentScan(basePackages = {"com.isu"} 
	, includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CorsFilter.class)
)
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@SpringBootApplication
public class IfWorkAdapterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IfWorkAdapterServiceApplication.class, args);
	}

}
