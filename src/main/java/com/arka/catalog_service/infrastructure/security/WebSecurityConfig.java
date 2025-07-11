package com.arka.catalog_service.infrastructure.security;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtSecurityContextRepository jwtSecurityContextRepository;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/api/products/**",
                                "/api-docs/**",        // ←  NUEVO
                                "/v3/api-docs/**",     // (por si vuelves al valor por defecto)
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger",         // ← agrega esta línea
                                "/actuator/health",
                                "/webjars/**"
                        ).permitAll()
                       .pathMatchers(
                               "/api/brands/**",
                               "/api/categories/**",
                               "/api/features/**")
                        .hasAnyAuthority("INVENTORY_MANAGER","ADMIN")
                        .anyExchange().authenticated()



                )
                .securityContextRepository(jwtSecurityContextRepository)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
                .build();
    }


    private Converter<Jwt, Mono<? extends AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            jwt.getClaimAsStringList("authorities").forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withSecretKey(new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256"))
                .build();
    }
}

