//package com.paymentgateway.paymentservice.config;
//
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//    @Bean
//    public GroupedOpenApi merchantApi() {
//        return GroupedOpenApi.builder()
//                .group("Payment Service APIs")
//                .pathsToMatch("/api/v1/auth/**")
//                .build();
//    }
//}
