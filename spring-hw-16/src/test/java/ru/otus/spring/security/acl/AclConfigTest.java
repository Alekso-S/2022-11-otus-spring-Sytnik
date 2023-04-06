package ru.otus.spring.security.acl;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Set;

import static ru.otus.spring.util.DataProducer.getCommentById;

public class AclConfigTest {

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        PermissionEvaluator permissionEvaluator = new TestPermissionEvaluator();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }

    private static class TestPermissionEvaluator implements PermissionEvaluator {
        @Override
        public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
            return false;
        }

        @Override
        public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (!targetType.equals("ru.otus.spring.domain.Comment")) {
                return false;
            }
            if (!Set.of("WRITE", "DELETE").contains((String) permission)) {
                return false;
            }
            return getCommentById((String) targetId)
                    .orElseThrow().getUser().getUsername().equals(userDetails.getUsername());
        }
    }
}
