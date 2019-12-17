package com.isu.ifw;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.camel.component.mybatis.MyBatisComponent;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement 
@EnableJpaRepositories(
		entityManagerFactoryRef = "entityManager", 
		transactionManagerRef = "jpaTransactionManager", 
		basePackages = {"com.isu.auth", "com.isu.option", "com.isu.ifw","com.isu.ifw.common", "com.pb"}
)
@MapperScan(	basePackages = {"com.isu.ifw.mapper", "com.isu.ifw.*.mapper"})
public class DataSourceConfiguration {
	 
	@Value("${mybatis.config-location}")
	private String mybatisConfigLocation;
	
	@Bean(name= {"authDataSource","dataSource"}, destroyMethod="close")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource dataSource() {
		return DataSourceBuilder
					.create().type(HikariDataSource.class) 
					.build();
	}
   
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	
	@Bean(name = "entityManager")
	@Primary
	public LocalContainerEntityManagerFactoryBean EntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder
					.dataSource(dataSource()) 
					.packages("com.isu.ifw.*.entity","com.isu.*.entity", "com.isu.*.dao", "com.isu.ifw.*.dao")
					.build();
	}
	
	/**
	 * @param entityManagerFactory
	 * @return
	 */
	@Bean(name="jpaTransactionManager")
	@Primary
	public PlatformTransactionManager jpaTransactionManager(@Qualifier("entityManager") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	@Bean(name="transactionManager")
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }
  
	@Bean 
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:config/mybatis-config.xml"));
		Resource[] arrResource = new PathMatchingResourcePatternResolver()
	           .getResources(mybatisConfigLocation); 
		sqlSessionFactoryBean.setMapperLocations(arrResource);
		 
//		 
//		 Resource[] arrResource = new PathMatchingResourcePatternResolver()
//           .getResources("classpath:query/*.xml");
//		 sqlSessionFactoryBean.setMapperLocations(arrResource);
//		 sqlSessionFactoryBean.getObject().getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
//		 sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
		 return sqlSessionFactoryBean.getObject();
	 }
	 
	 @Bean(name="mybatis")
	 public MyBatisComponent myBatisComponent( SqlSessionFactory sqlSessionFactory )
	 {
	     MyBatisComponent result = new MyBatisComponent();
	     result.setSqlSessionFactory( sqlSessionFactory );
	     return result;
	 }
	 
	 @Bean
	 public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
		 return new SqlSessionTemplate(sqlSessionFactory);
	 }
	 
}
