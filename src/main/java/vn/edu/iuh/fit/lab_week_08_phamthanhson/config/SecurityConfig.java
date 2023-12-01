package vn.edu.iuh.fit.lab_week_08_phamthanhson.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Autowired
    public void globalConfig(AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder encoder) throws Exception{
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser(User.withUsername("admin").password(encoder.encode("admin")).roles("ADMIN").build())
                .withUser(User.withUsername("son").password(encoder.encode("son")).roles("ADMIN").build())
                .withUser(User.withUsername("leon").password(encoder.encode("leon")).roles("USER").build());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->auth
                        .requestMatchers("/","/index").permitAll()
                        .requestMatchers("/api/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(("/admin/**")).hasRole("ADMIN")
                        .anyRequest().authenticated()
        );
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
