package com.vibecoding.devlog.config;

import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.LocalDateTypeHandler;
import org.apache.ibatis.type.LocalTimeTypeHandler;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 설정
 *
 * Java 8 날짜/시간 타입 지원을 위한 TypeHandler 등록
 *
 * @author DevLog Team
 * @version 1.0
 */
@Configuration
public class MyBatisConfig {

    /**
     * MyBatis Configuration Customizer
     *
     * Spring Boot의 자동 설정과 충돌하지 않도록 ConfigurationCustomizer 사용
     */
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            // Java 8 날짜/시간 타입 핸들러 등록
            configuration.getTypeHandlerRegistry().register(java.time.LocalDate.class, LocalDateTypeHandler.class);
            configuration.getTypeHandlerRegistry().register(java.time.LocalTime.class, LocalTimeTypeHandler.class);
            configuration.getTypeHandlerRegistry().register(java.time.LocalDateTime.class, LocalDateTimeTypeHandler.class);
        };
    }
}
