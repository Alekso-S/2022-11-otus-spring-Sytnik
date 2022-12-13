package ru.otus.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BenchmarkAspect {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkAspect.class);

    @Around("@annotation(ru.otus.spring.annotations.QuestionBenchmark)")
    public Object QuestionBenchmark(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());

        logger.debug("Student spent {} sec for the question",
                (System.currentTimeMillis() - startTime) / 1000.0);

        return result;
    }
}
