package ru.job4j.accidents.config.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MainLogger {

    @Pointcut("execution(public * ru.job4j.accidents..*.*(..))")
    public void allMethods() {
    }

    @Before("allMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.debug("Вызов метода: '{}' с аргументами: '{}'", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "allMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.debug("Метод: '{}' завершился успешно, возвращено: '{}'", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "allMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.warn("Метод: '{}' выбросил исключение:", joinPoint.getSignature(), ex);
    }
}
