package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = roleService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<User> updateUserRole(@PathVariable Long userId, @RequestBody RoleUpdateRequest request) {
        try {
            User updatedUser = roleService.updateUserRole(userId, request.getRole());
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/users/{userId}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable Long userId) {
        roleService.disableUser(userId);
        return ResponseEntity.noContent().build();
    }

    public static class RoleUpdateRequest {
        private User.Role role;

        public User.Role getRole() { return role; }
        public void setRole(User.Role role) { this.role = role; }
    }
}