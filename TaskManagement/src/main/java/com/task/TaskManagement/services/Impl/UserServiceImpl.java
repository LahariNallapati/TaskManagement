package com.task.TaskManagement.services.Impl;

import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.dao.UsersRepository;
import com.task.TaskManagement.dto.*;
import com.task.TaskManagement.services.UserService;
import com.task.TaskManagement.utils.EmailService;
import com.task.TaskManagement.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtil;
    @Autowired
    private EmailService emailService;

    @Override
    public ResponseWrapper<UsersEntity> createUser(UserDto dto) {
        UsersEntity users = new UsersEntity();
        BeanUtils.copyProperties(dto, users, "userId");
        users.setPassword(passwordEncoder.encode(dto.getPassword()));// Automatically maps matching fields
        users.setUpdatedAt(LocalDateTime.now());
        UsersEntity saved = usersRepository.save(users);
        return new ResponseWrapper<>("User created successfully", saved);
    }

    @Override
    public ResponseWrapper<UsersEntity> getUserById(Integer id) {
        UsersEntity users = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return new ResponseWrapper<>("Userfetched successfully", users);
    }

    @Override
    public ResponseWrapper<List<UsersEntity>> getAllUsers() {
        List<UsersEntity> list = usersRepository.findAll();
        return new ResponseWrapper<>("All users fetched successfully", list);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UsersEntity user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.setFailed_attempts(user.getFailed_attempts() + 1);
            usersRepository.save(user);
            throw new RuntimeException("Invalid email or password");
        }

        user.setLast_login(LocalDateTime.now());
        user.setFailed_attempts(0);
        usersRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(token, "Login successful");
    }

    @Override
    public void sendResetLink(String email) {
        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = jwtUtil.generateToken(email);
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        emailService.sendEmail(email, "Reset Password", "Click to reset: " + resetLink);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String email = jwtUtil.extractUsername(request.getToken());
        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        usersRepository.save(user);

    }

    @Override
    public ResponseWrapper<UsersEntity> updateUser(Integer id, UserDto dto) {
       UsersEntity existing = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        BeanUtils.copyProperties(dto, existing, "userId"); // Copy all matching fields from DTO

        existing.setUpdatedAt(LocalDateTime.now());
        UsersEntity updated = usersRepository.save(existing);
        return new ResponseWrapper<>("user updated successfully", null);
    }

    @Override
    public ResponseWrapper<String> deleteUser(Integer id) {
        UsersEntity users = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        users.setDeleted(true);
        usersRepository.save(users); // Save the updated project

        return new ResponseWrapper<>("Users deleted successfully",null);

    }


}


