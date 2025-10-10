package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.exception.RoleNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Set<Role> getRolesByIds(Long[] roleIds) {
        //Получает роли по массиву ID
        if (roleIds == null || roleIds.length == 0) {
            return new HashSet<>();
        }

        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            Role role = findById(roleId);
            roles.add(role);
        }
        return roles;
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
