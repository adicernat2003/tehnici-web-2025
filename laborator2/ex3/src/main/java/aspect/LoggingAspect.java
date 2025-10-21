package aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* service..*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods() && args(name)")
    public void beforeMethodsWithArg(String name) {
        System.out.println("[AOP] @Before  -> about to call method with arg: " + name);
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void afterReturning(Object result) {
        System.out.println("[AOP] @AfterReturning -> result: " + result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void afterThrowing(Throwable ex) {
        System.out.println("[AOP] @AfterThrowing -> exception: " + ex.getMessage());
    }

    @Around("serviceMethods()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            System.out.println("[AOP] @Around (before) -> " + pjp.getSignature());
            Object result = pjp.proceed();
            long tookMs = (System.nanoTime() - start) / 1_000_000;
            System.out.println("[AOP] @Around (after)  -> " + pjp.getSignature() + " took " + tookMs + " ms");
            return result;
        } catch (Throwable t) {
            long tookMs = (System.nanoTime() - start) / 1_000_000;
            System.out.println("[AOP] @Around (error)  -> " + pjp.getSignature() + " failed after " + tookMs + " ms");
            throw t;
        }
    }
}