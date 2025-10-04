package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
                           UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Инициализируем роли через UserService
        userService.initializeRoles();

        // Получаем роли из базы данных
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");

        // Создаем админа, если его нет
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Adminov");
            admin.setEmail("admin@mail.ru");
            admin.setAge(30);

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole); // Используем существующую роль
            adminRoles.add(userRole);  // Используем существующую роль
            admin.setRoles(adminRoles);

            userService.saveUser(admin);
        }

            // Создаем обычного пользователя, если его нет
        if (userRepository.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("user");
            user.setFirstName("User");
            user.setLastName("Userov");
            user.setEmail("user@mail.ru");
            user.setAge(25);

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole); // Используем существующую роль
            user.setRoles(userRoles);

            userService.saveUser(user);
        }
        System.out.println("Test users created:");
        System.out.println("Admin: admin / admin");
        System.out.println("User: user / user");
    }
}
