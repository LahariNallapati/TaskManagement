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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.stream.Collectors;

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
        users.setPassword(passwordEncoder.encode(dto.getPassword()));
        users.setConfirmPassword(passwordEncoder.encode(dto.getConfirmPassword()));// Automatically maps matching fields
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password must match");
        }
        users.setUpdatedAt(LocalDateTime.now());
        UsersEntity saved = usersRepository.save(users);
        return new ResponseWrapper<>("User created successfully", saved);
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

//    @Override
//    public LoginResponse login(LoginRequest request) {
//        UsersEntity user = usersRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            user.setFailed_attempts(user.getFailed_attempts() + 1);
//            usersRepository.save(user);
//            throw new RuntimeException("Invalid email or password");
//        }
//
//        user.setLast_login(LocalDateTime.now());
//        user.setFailed_attempts(0);
//        usersRepository.save(user);
//
////        String token = jwtUtil.generateToken(user.getEmail());
//        String token = jwtUtil.generateToken(
//                user.getEmail(),
//                "YES".equalsIgnoreCase(user.getIsAdmin())
//        );
//
//        return new LoginResponse(token, "Login successful");
//    }

    @Override
    public void sendResetLink(String email) {
        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

//        String token = jwtUtil.generateToken(email);
        String token = jwtUtil.generateToken(
                user.getEmail(),
                "YES".equalsIgnoreCase(user.getIsAdmin())
        );
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
    public ResponseWrapper<List<UsersEntity>> searchUsers(String name, Integer pageNo, Integer pageSize) {

            List<UsersEntity> user = new ArrayList<>();
            PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
            List<UsersEntity> userEntities = usersRepository.searchUsers(name, pageRequest);

            user.forEach(userEntity -> {
                UserDto users = new UserDto();
                BeanUtils.copyProperties(userEntity, users);
                user.add(userEntity);
            });

            return new ResponseWrapper<>("user Detailes fetched successfully", user);


    }
    @Override
    public List<UsersEntity> getUsersPageOnly(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UsersEntity> page = usersRepository.findAll(pageable);
        return page.getContent();  // ✅ Only the user list, no metadata
    }

    @Override
    public ResponseWrapper<String> deactivateUser(Integer id) {
        int count = usersRepository.deactivateUser(id);
        if (count > 0) {
            return new ResponseWrapper<>( "User deactivated successfully.", null);
        } else {
            return new ResponseWrapper<>("User not found or already deactivated.", null);
        }
    }

    @Override
    public List<UsersEntity> getUsersByActiveStatus(Boolean active) {
        if (active == null) {
            return usersRepository.findAll();
        } else {
            return usersRepository.findByActive(active);
        }
    }
    @Override
    public LoginResponse login(LoginRequest request) {
        UsersEntity user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // ✅ Check if user is deleted
        if (Boolean.TRUE.equals(user.isDeleted())) {
            throw new RuntimeException("Your account has been deleted. Contact admin.");
        }

        // ✅ Check if account is inactive
        if (Boolean.FALSE.equals(user.getActive())) {
            throw new RuntimeException("Account is blocked due to multiple failed login attempts. Contact admin.");
        }

        // ✅ Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            int attempts = user.getFailed_attempts() + 1;
            user.setFailed_attempts(attempts);

            // ✅ Block user after 3 failed attempts
            if (attempts >= 3) {
                user.setActive(false);
            }

            usersRepository.save(user);
            throw new RuntimeException("Invalid email or password");
        }

        // ✅ Reset failed attempts on successful login
        user.setFailed_attempts(0);
        usersRepository.save(user);

        // ✅ Generate JWT and return login response
//    String token = jwtUtil.generateToken(user); // You can customize this part
      //  String token = jwtUtil.generateToken(user.getEmail());
        String token = jwtUtil.generateToken(
                user.getEmail(),
                "YES".equalsIgnoreCase(user.getIsAdmin())
        );
//
        return new LoginResponse(token, "Login successful");
    }
    @Override
    public void logout(String token) {
        // Token invalidation logic (e.g., add to blacklist or rely on expiration)
    }

    @Override
    public void changePassword(String email, ChangePassword request) {
        if (request.getCurrentPassword() == null || request.getNewPassword() == null) {
            throw new RuntimeException("Current and new passwords must be provided.");
        }

        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        usersRepository.save(user);
    }
}




