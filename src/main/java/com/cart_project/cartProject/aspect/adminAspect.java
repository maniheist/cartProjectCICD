package com.cart_project.cartProject.aspect;

import com.cart_project.cartProject.exceptions.AdminNotFoundException;
import com.cart_project.cartProject.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class adminAspect {

    @Autowired
    private final LoginService loginService;

    @Around("@annotation(com.cart_project.cartProject.annotations.AdminOnly) && args(userId,..)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint, int userId) throws Throwable {
        if(!loginService.getUserById(userId).getRole().equals("Admin")){
            throw new AdminNotFoundException("Admins Only");
        }
        return joinPoint.proceed();
    }
}
