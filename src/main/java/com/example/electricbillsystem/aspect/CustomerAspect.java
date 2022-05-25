package com.example.electricbillsystem.aspect;

import com.example.electricbillsystem.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class CustomerAspect {

    private CustomerService customerService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Pointcut("execution(public * com.example.electricbillsystem.configuration.CustomerConfig.*())")
    public void allMethods() {
    }

    @Pointcut("execution(public * com.example.electricbillsystem.configuration.CustomerConfig.deleteCustomer())")
    public void deleteMethod() {
    }

    @Before("allMethods()")
    public void Before(JoinPoint joinPoint) {
        log.info("Join point kind : " + joinPoint.getKind());
        log.info("Signature declaring type : " + joinPoint.getSignature().getDeclaringTypeName());
        log.info("Signature name : " + joinPoint.getSignature().getName());
        log.info("Arguments : " + Arrays.toString(joinPoint.getArgs()));
        log.info("Target class : " + joinPoint.getTarget().getClass().getName());
        log.info("This class : " + joinPoint.getThis().getClass().getName());
    }

    @After("allMethods()")
    public void After(JoinPoint joinPoint) {
        log.info("Database changes after method execution:");
        customerService.getCustomers().forEach(System.out::println);
        log.info("Exiting from Method :" + joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "deleteMethod()", returning = "result")
    public void AfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method :" + joinPoint.getSignature().getName() + " ended returning some value");
        log.info("Return value :" + result);
    }

    @Around("allMethods()")
    public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("The method " + joinPoint.getSignature().getName() + "() begins with " + Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("The method " + joinPoint.getSignature().getName() + "() ends with " + result);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in " + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }

    @AfterThrowing(value = "allMethods()", throwing = "e")
    public void AfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + "()");
        log.error("Exception :" + e);
        log.error("Cause :" + e.getCause());
    }

}
