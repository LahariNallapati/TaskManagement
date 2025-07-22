package com.task.TaskManagement.utils;

import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.dao.UsersRepository;
import com.task.TaskManagement.exception.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import io.jsonwebtoken.Claims;

@Component
//@RequiredArgsConstructor
public class CommonUtils {

    private final UsersRepository userRepo;
    private final JwtUtils jwtUtils;

    public CommonUtils(UsersRepository userRepo, JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
    }

    public HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public String getCurrentToken() {
        String token = getCurrentRequest().getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // remove 'Bearer '
        }
        throw new UnAuthorizedException("Missing or invalid Authorization header");
    }

    public String getCurrentLoggedInEmail() {
        String token = getCurrentToken();
        return jwtUtils.extractEmail(token);
    }

    public Integer getCurrentLoggedInUserId() {
        UsersEntity userEntity = userRepo.findByEmail(getCurrentLoggedInEmail())
                .orElseThrow(() -> new UnAuthorizedException("User not found"));
        return userEntity.getUserId();
    }


    public void isAdmin() {
        Claims claims = jwtUtils.getAllClaims(getCurrentToken());
        String isAdmin = claims.get("isAdmin", String.class);
        if (!"YES".equalsIgnoreCase(isAdmin)) {
            throw new UnAuthorizedException("Unauthorized Access: Admins only");
        }
    }
}
