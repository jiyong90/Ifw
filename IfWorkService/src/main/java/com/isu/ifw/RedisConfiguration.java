package com.isu.ifw;

//@Configuration
//@EnableRedisRepositories
public class RedisConfiguration {
//    
//	@Value("${spring.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//    @Value("${spring.redis.password}")
//    private String redisPassword;
//    
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//    	
//
//    	RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(redisHost);
//        redisStandaloneConfiguration.setPort(redisPort);
//        redisStandaloneConfiguration.setPassword(redisPassword);
//        
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
//        return lettuceConnectionFactory;
//    }
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
// 
}

