package com.example.ApiGateway.config;

import com.example.ApiGateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    private final AuthenticationFilter authenticationFilter;

    public GatewayRoutesConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("driver-service", r -> r.path("/api/driver/**","/api/driverPenalty/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://driver-service"))
                .route("maintenance-service", r -> r.path("/api/maintenance/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://maintenance-service"))
                .route("vehicle-service", r -> r.path("/api/vehicle/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://vehicle-service"))
                .route("trip-service",r -> r.path("/api/trip/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://trip-service"))
                .build();
    }


}
