package org.our_place.identity.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UseCaseLoggingAspect {

    @Around("execution(* org.our_place..usecase..*UseCase.execute(..)) && args(command)")
    public Object logUseCase(ProceedingJoinPoint pjp, Object command) throws Throwable {
        String useCaseName = pjp.getTarget().getClass().getSimpleName();
        String commandName = command != null
                ? command.getClass().getSimpleName()
                : "NoCommand";

        long start = System.nanoTime();

        log.info("[{}] iniciando con {} ", useCaseName, commandName);
        try {
            Object result = pjp.proceed();
            log.info("[{}] completado en {} ms", useCaseName, elapsedMs(start));
            return result;
        } catch (Exception e) {
            log.warn("[{}] falló en {} ms: {}", useCaseName, elapsedMs(start), e.getMessage());
            throw e;
        }
    }

    private long elapsedMs(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }

}
