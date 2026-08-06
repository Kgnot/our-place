package org.our_place.affection.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Qualifier("affection")
public class AffectionUseCaseLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(AffectionUseCaseLoggingAspect.class);

    @Around("execution(* org.our_place.affection.usecase.*UseCase.execute(..))")
    public Object logUseCaseExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String useCaseName = joinPoint.getTarget().getClass().getSimpleName();
        Object command = joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : "No command";

        log.info("[UseCase START] {} - Command: {}", useCaseName, command);

        try {
            Object result = joinPoint.proceed();
            log.info("[UseCase END] {} - Output: {}", useCaseName, result);
            return result;
        } catch (Throwable ex) {
            log.error("[UseCase ERROR] {} - Error: {}", useCaseName, ex.getMessage());
            throw ex;
        }
    }
}