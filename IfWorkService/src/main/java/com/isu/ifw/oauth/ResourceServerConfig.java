package com.isu.ifw.oauth;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
//@EnableRedisHttpSession
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired 
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Autowired 
	private CustomTokenAuthenticationEntryPoint customTokenAuthenticationEntryPoint;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
    private javax.servlet.Filter preFilter;
	
//	@Bean(name="redisConnectionFactory")
//    public RedisConnectionFactory redisConnectionFactory() {
//        LettuceConnectionFactory conn = new LettuceConnectionFactory();
//		conn.setHostName("192.168.111.12");
//		conn.setPort(6379);
//		conn.setPassword("isu!!2019");
//        return conn;
//    }
//   
//    @Bean(name = "redisTemplate")
//    public StringRedisTemplate redisTemplate() {
//    	StringRedisTemplate redisTemplate = new StringRedisTemplate();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
		    

	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		//super.configure(http);
		http.httpBasic()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
		.and()
		.cors().disable()
		.headers().frameOptions().disable().and()
		.authorizeRequests()
		.antMatchers("/intf/**", "/login/**","/login/**/authorize","/we/**","/info/**","/certificate/**","/schedule/**","/interface/**","/api/**","/static/**","/mobile/**").permitAll()
		.anyRequest()//.access("hasRole('ROLE_USER')") 
		.authenticated()
		.and().exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
		//.and().addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class)
		
		;
		
	}
	 

	@Bean 
	public FilterRegistrationBean getPreFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(preFilter);
		//registrationBean.addInitParameter("freePassPath", "/login,/we,/info,/mobile,/certificate,/intf");
		return registrationBean;
	    
	}
//	
//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        //return new CustomAuthenticationEntryPoint("/loginPage");
//    	return customAuthenticationEntryPoint;
//    }
//    
	@Override
	public void configure(ResourceServerSecurityConfigurer config) {
		
		try {
			config
			.tokenServices(tokenServices())
			.tokenStore(tokenStore())
			.tokenExtractor(extractor())
			//.and().oauth2Login().loginPage("/")
			//.authenticationEntryPoint(customTokenAuthenticationEntryPoint)
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Bean
	public TokenExtractor extractor() {
		return new CustomExtractor();
	}
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	/*
	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}
	*/
//	@Autowired 
//	RedisConnectionFactory redisConnectionFactory;
//	
//	@Bean
//	public TokenStore tokenStore() {
//		return new RedisTokenStore ( redisConnectionFactory ); 
//	}
	
    /**
     * jwt converter - signKey 공유 방식
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("iSuSystEmW0rkT1meM@n@gemEnTServ1c2");
        return converter;
    }

    /*
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new FileSystemResource("/Users/claire/isu/workspace/if-hr-work/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/IfWorkService/WEB-INF/classes/oauth2jwt.jks"), "oauth2jwtpass".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth2jwt"));
        return converter;
	}
*/
    
	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}
}