package com.hotel.reservation.config.security;

import com.hotel.reservation.config.security.authority.AppUserPermission;
import com.hotel.reservation.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//TODO : ADD ant matchers, add logout support, add @PreAuthorize to controllers,


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurityConfig(AppUserService appUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(SWAGGER_WHITELIST).permitAll()
                .antMatchers(HttpMethod.GET, "/api/orders/**").hasAnyAuthority(AppUserPermission.ORDERS_READ.getPermission())
//                        .antMatchers(HttpMethod.POST,"/api/**").hasAnyAuthority(AppUserPermission.ORDERS_WRITE.getPermission(),
//                                                                                                AppUserPermission.ROOM_TYPE_WRITE.getPermission(),
//                                                                                                AppUserPermission.ROOM_WRITE.getPermission()  )
//                        .antMatchers(HttpMethod.PUT,"/api/**").hasAnyAuthority(AppUserPermission.ORDERS_WRITE.getPermission(),
//                                                                                                AppUserPermission.ROOM_TYPE_WRITE.getPermission(),
//                                                                                                AppUserPermission.ROOM_WRITE.getPermission()  )
//                        .antMatchers(HttpMethod.DELETE,"/api/**").hasAnyAuthority(AppUserPermission.ORDERS_WRITE.getPermission(),
//                                                                                                AppUserPermission.ROOM_TYPE_WRITE.getPermission(),
//                                                                                                AppUserPermission.ROOM_WRITE.getPermission()  )
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
//                        .antMatchers("/api/registration/**").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest()
                .authenticated().and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);

        return provider;
    }


}
