package org.our_place.identity.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.our_place.identity.api.SecurityContextApi;
import org.our_place.shared.infra.security.RequireRoles;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Aspect
@Component
@Slf4j
public class AuthorizationAspect {

    private final SecurityContextApi securityContextHelper;// usamos la interfaz

    public AuthorizationAspect(SecurityContextApi securityContextHelper) {
        this.securityContextHelper = securityContextHelper;
    }

    @Around("@annotation(org.our_place.shared.infra.security.RequireRoles) && @annotation(requireRoles)")
    public Object requireRoles(ProceedingJoinPoint joinPoint, RequireRoles requireRoles) throws Throwable {
        var userRoles = securityContextHelper.getCurrentUserRoles();
        log.debug("Current user roles: {}", userRoles);
        log.debug("Required roles: {}", (Object) requireRoles.value());
        List<String> requireRole = List.of(requireRoles.value());

        if (!Collections.disjoint(userRoles, requireRole)) {
            return joinPoint.proceed();
        }
        throw new SecurityException("User does not have the required roles");
    }
}
