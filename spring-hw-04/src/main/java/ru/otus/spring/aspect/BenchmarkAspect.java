package ru.otus.spring.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BenchmarkAspect {

    @Around("@annotation(ru.otus.spring.annotation.QuestionBenchmark)")
    public Object QuestionBenchmark(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(proceedingJoinPoint.getSignature().getDeclaringTypeName());
        Object result;
        long startTime = System.currentTimeMillis();

        try {
            result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (Exception e) {
            logger.error("Error during calling method {}: {}", proceedingJoinPoint.getSignature(), e.toString());

            throw e;
        } finally {
            logger.debug("Student spent {} sec for input",
                    (System.currentTimeMillis() - startTime) / 1000.0);
        }

        return result;
    }
}
