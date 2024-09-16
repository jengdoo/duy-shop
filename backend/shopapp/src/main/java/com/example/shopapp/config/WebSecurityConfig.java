package com.example.shopapp.config;

import com.example.shopapp.Model.Role;
import com.example.shopapp.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
//@EnableMethodSecurity
@RequiredArgsConstructor
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(author -> {
                            author
                                    .requestMatchers(
                                    String.format("%s/users/register",apiPrefix),
                                    String.format("%s/users/login",apiPrefix)
                                    ).permitAll()
                                    .requestMatchers(HttpMethod.GET,String.format("%s/product/**",apiPrefix)).permitAll()
                                    .requestMatchers(HttpMethod.GET,String.format("%s/category/**",apiPrefix)).permitAll()
                                    .requestMatchers(HttpMethod.POST,String.format("%s/category/**",apiPrefix)).hasAnyRole(Role.ADMIN)
                                    .requestMatchers(HttpMethod.PUT,String.format("%s/category/**",apiPrefix)).hasAnyRole(Role.ADMIN)
                                    .requestMatchers(HttpMethod.DELETE,String.format("%s/category/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                                    .requestMatchers(HttpMethod.GET,String.format("%s/roles",apiPrefix)).permitAll()

                                    .requestMatchers(HttpMethod.GET,String.format("%s/product/{name}/**",apiPrefix)).permitAll()
                                    .requestMatchers(HttpMethod.GET,String.format("%s/product/images/*",apiPrefix)).permitAll()
                                    .requestMatchers(HttpMethod.POST,String.format("%s/product/**",apiPrefix)).hasAnyRole(Role.ADMIN)
                                    .requestMatchers(HttpMethod.PUT,String.format("%s/product/**",apiPrefix)).hasAnyRole(Role.ADMIN)
                                    .requestMatchers(HttpMethod.DELETE,String.format("%s/product/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                                    .requestMatchers(HttpMethod.POST,String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.USER)
                                    .requestMatchers(HttpMethod.GET,String.format("%s/orders/**",apiPrefix)).permitAll()
                                    .requestMatchers(HttpMethod.DELETE,String.format("%s/orders/**",apiPrefix)).hasRole(Role.ADMIN)
                                    .requestMatchers(HttpMethod.PUT,String.format("%s/orders/**",apiPrefix)).hasRole(Role.ADMIN)

                                    .requestMatchers(HttpMethod.POST,String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.USER)
                                    .requestMatchers(HttpMethod.GET,String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                                    .requestMatchers(HttpMethod.DELETE,String.format("%s/order_details/**",apiPrefix)).hasRole(Role.ADMIN)
                                    .requestMatchers(HttpMethod.PUT,String.format("%s/order_details/**",apiPrefix)).hasRole(Role.ADMIN)

                                    .requestMatchers(HttpMethod.POST,String.format("%s/users/details/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                                    .requestMatchers(HttpMethod.GET,String.format("%s/users/userId/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                                    .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors
                    .configurationSource(request -> {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(List.of("*"));
                        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Auth-Token","Params"));
                        return corsConfiguration;
                    })
        );

        return http.build();
    }
}
//                                    .requestMatchers(HttpMethod.GET,String.format("%s/roles/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
//                http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
//                    @Override
//                    public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
//                        CorsConfiguration configuration = new CorsConfiguration();
//                        configuration.setAllowedOrigins(List.of("*"));
//                        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
//                        configuration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token"));
//                        configuration.setAllowedHeaders(List.of("x-auth-token"));
//                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//                        source.registerCorsConfiguration("/**",configuration);
//                        httpSecurityCorsConfigurer.configurationSource(source);
//                    }
//                });
//                                    .requestMatchers(HttpMethod.GET,String.format("%s/product/by-ids/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)

//                                    .requestMatchers(HttpMethod.GET,String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
