package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //основная конфигурация безопасности
    //WebSecurityConfigurerAdapter - базовый класс для настройки безопасности
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() //начало настройки авторизации
                    .antMatchers("/login")// определяет шаблоны URL и требования доступа к ним
                    .permitAll()//разрешает доступ всем
                    .antMatchers("/api/users/current").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/api/roles").hasRole("ADMIN")
                    .antMatchers("/api/users/**").hasRole("ADMIN")
                    .antMatchers("/admin/**").hasRole("ADMIN")//требует определенную роль
                    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")//требует одну из ролей
                    .anyRequest().authenticated()
                    .and()
                .formLogin()//Настраивает форму входа(Разрешает доступ к странице логина всем)
                    .loginPage("/login")
                    .successHandler(successUserHandler)
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                .and()
                .csrf().disable(); // Отключаем CSRF для REST API (в продакшене нужно настроить правильно)
    }

    // бин для кодирования паролей using BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}