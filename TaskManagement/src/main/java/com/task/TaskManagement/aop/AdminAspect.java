package com.task.TaskManagement.aop;

import com.task.TaskManagement.utils.CommonUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminAspect {
    private final CommonUtils commonUtils;

    public AdminAspect(CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
    }

//    @Pointcut("execution(* com.task.TaskManagement.controllers.ClientController.*(..))")
//    public void clientControllerMethods() {
//    }
//

//    @Before("clientControllerMethods()")
//    public void secureUserController() {
//        commonUtils.isAdmin();
//    }

    @Before("@annotation(com.task.TaskManagement.annotations.AdminOnly)")
    public void checkAdminAccess() {
        commonUtils.isAdmin(); // throws UnAuthorizedException if not admin
    }
}
