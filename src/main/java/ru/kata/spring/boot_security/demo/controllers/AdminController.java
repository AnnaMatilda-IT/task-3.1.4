package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserCreateDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin") //задаем базовый URL для всех методов контроллера админа
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("userDto", new UserCreateDto());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "add-user";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute UserCreateDto userDto) {
        userService.saveUser(userDto);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        //@PathVariable - извлекает значение из URL
        User user = userService.getUserById(id);
        if (user != null) {
            // Создаем DTO из существующего пользователя
            UserCreateDto userDto = new UserCreateDto();
            userDto.setUsername(user.getUsername());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setAge(user.getAge());

            // Получаем ID ролей пользователя
            Long[] roleIds = user.getRoles().stream()
                    .map(Role::getId)
                    .toArray(Long[]::new);
            userDto.setRoleIds(roleIds);

            model.addAttribute("userDto", userDto);
            model.addAttribute("userId", id); // Передаем ID отдельно
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "edit-user";
        } else {
            return "redirect:/admin?error=user_not_found";
        }
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute UserCreateDto userDto,
                             @RequestParam("userId") Long userId) {
        userService.updateUser(userDto, userId);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
