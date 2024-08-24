package com.example.shop.backend.configurations;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import com.vaadin.hilla.route.RouteUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig extends VaadinWebSecurity {

    private final RouteUtil routeUtil;

    /**
     * Configure HTTP security settings.
     * @param http HttpSecurity instance to configure
     * @throws Exception if an error occurs during configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Allow access to H2 console and Hilla internal requests, deny all others
        http.authorizeHttpRequests(authorization -> {
            authorization.requestMatchers("/h2-console/**")
                    .permitAll(); // Allow access to H2 console
            authorization.requestMatchers(routeUtil::isRouteAllowed)
                    .permitAll(); // Allow access to Hilla internal routes
        });

        // Use VaadinWebSecurity's default configuration
        super.configure(http);

        // Set custom login view for the application
        setLoginView(http, "/login", "/login");
    }

    /**
     * Bean definition for in-memory user details manager.
     *
     * @return UserDetailsManager instance with pre-configured user
     */
    @Bean
    public UserDetailsManager userDetailsService() {
        // Configure a single user in memory with username "aa", password "password", and role "ADMIN"
        return new InMemoryUserDetailsManager(
                User.withUsername("aaronOsi").password("{noop}password").roles("ADMIN").build()
        );
    }
}
