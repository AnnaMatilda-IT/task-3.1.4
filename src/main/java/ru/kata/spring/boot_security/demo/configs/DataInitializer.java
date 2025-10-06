package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.dto.UserCreateDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleService roleService,
                           UserService userService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // метод выполняется после запуска приложения
        // Инициализируем роли через UserService и получаем на них ссылки
        userService.initializeRoles();

        // Получаем роли из базы данных
        Role adminRole = roleService.findByName("ROLE_ADMIN");
        Role userRole = roleService.findByName("ROLE_USER");


        // Создаем админа с использованием DTO
        if (userRepository.findByUsername("admin") == null) {
            UserCreateDto adminDto = new UserCreateDto();
            adminDto.setUsername("admin");
            adminDto.setPassword("admin");
            adminDto.setFirstName("Admin");
            adminDto.setLastName("Adminov");
            adminDto.setEmail("admin@mail.ru");
            adminDto.setAge(30);
            adminDto.setRoleIds(new Long[]{adminRole.getId(), userRole.getId()});

            userService.saveUser(adminDto);
        }

            // Создаем обычного пользователя с использованием DTO
        if (userRepository.findByUsername("user") == null) {
            UserCreateDto userDto = new UserCreateDto();
            userDto.setUsername("user");
            userDto.setPassword("user");
            userDto.setFirstName("User");
            userDto.setLastName("Userov");
            userDto.setEmail("user@mail.ru");
            userDto.setAge(25);
            userDto.setRoleIds(new Long[]{userRole.getId()});

            userService.saveUser(userDto);
        }

        System.out.println("Test users created:");
        System.out.println("Admin: admin / admin");
        System.out.println("User: user / user");
    }
}
