package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Autowired
        DataSource dataSource;

        @Bean
        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests((requests) ->
                    requests.requestMatchers("/h2-console/**").permitAll()
                            .anyRequest().authenticated());
            http.sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) );
            //http.formLogin(withDefaults());
            http.httpBasic(withDefaults());
            http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
            http.csrf(AbstractHttpConfigurer::disable);
            return http.build();

        }
        @Bean
        public UserDetailsService userDetailsService(){

            UserDetails user = User.withUsername("user")
                    .password("{noop}123")
                    .roles("USER")
                    .build();


            UserDetails admin = User.withUsername("admin")
                    .password("{noop}123")
                    .roles("ADMIN")
                    .build();

                JdbcUserDetailsManager userDetailsManager =
                 new JdbcUserDetailsManager(dataSource);

                userDetailsManager.createUser(user);
                userDetailsManager.createUser(admin);

                return userDetailsManager;

            //return new InMemoryUserDetailsManager(user,admin);
        }
}