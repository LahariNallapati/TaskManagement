package com.task.TaskManagement.Config;

import com.task.TaskManagement.Constants.CommonConstants;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.dao.UsersRepository;
import com.task.TaskManagement.exception.UnAuthorizedException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationConfig {
    @Autowired
    private UsersRepository userRepo;

    @PostConstruct
    public void init() {
        UsersEntity admin= userRepo.findByEmail("admin@gmail.com")
                .orElseThrow(() -> new UnAuthorizedException("User not found"));
       // UsersEntity admin = userRepo.findByEmail("admin@gmail.com");
        if (admin == null) {
            UsersEntity user = new UsersEntity();
            user.setEmail("admin@gmail.com");
            user.setPassword("admin");
            user.setIsAdmin(CommonConstants.IS_ADMIN);
            userRepo.save(user);
        }
    }
}
