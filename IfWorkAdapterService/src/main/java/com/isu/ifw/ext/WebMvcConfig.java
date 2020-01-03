package com.isu.ifw.ext;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
/*
	@Autowired
	@Qualifier(value = "httpInterceptor")
	private HandlerInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor)
				.addPathPatterns("/**"); 
	}
	*/
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/").addResourceLocations("WEB-INF/static/");
    }
	
	@Bean
	public RestTemplate getRestTemplate() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(); 
		factory.setReadTimeout(10000); 
		factory.setConnectTimeout(10000); 
		CloseableHttpClient httpClient = HttpClientBuilder.create().build(); 
		factory.setHttpClient(httpClient); 
		return new RestTemplate(factory);
	}

}