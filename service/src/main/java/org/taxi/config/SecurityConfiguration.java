package org.taxi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.taxi.service.UserService;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

import static org.taxi.entity.enums.Role.ADMIN;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
//                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/admin/**").hasAuthority(ADMIN.getAuthority())
                        .requestMatchers("/", "/login", "/users/registration", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
                )
//                .oauth2Login(config -> config
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/users")
//                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService())))
                .build();
    }

//    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
//        return userRequest -> {
//            String email = userRequest.getIdToken().getClaim("email");
//            UserDetails userDetails = userService.loadUserByUsername(email);
////            new OidcUserService().loadUser()
//            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());
//
//            Set<Method> userDetailsMethods = Set.of(UserDetails.class.getMethods());
//
//            return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
//                    new Class[]{UserDetails.class, OidcUser.class},
//                    (proxy, method, args) -> userDetailsMethods.contains(method)
//                            ? method.invoke(userDetails, args)
//                            : method.invoke(oidcUser, args));
//        };
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}