package com.hotel.reservation.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ReservationLoggingAspect {


    //setup pointcut declaration
    @Pointcut("execution(* com.hotel.reservation.controller.*.*(..))")
    private void forControllerPackage() {
    }

    @Pointcut("execution(* com.hotel.reservation.service.*.*(..))")
    private void forServicePackage() {
    }

    @Pointcut("execution(* com.hotel.reservation.repository.*.*(..))")
    private void forRepositoryPackage() {
    }

    @Pointcut("forControllerPackage() || forServicePackage() || forRepositoryPackage()")
    private void forAppFlow() {
    }
    // add @Before annotation

    @Before("forAppFlow()")
    public void before(JoinPoint theJoinPoint) {
        // display methods we are calling
        log.trace("@Before calling method : {}", theJoinPoint.getSignature().toShortString());
        //display the arguments to the method
        Object[] arguments = theJoinPoint.getArgs();

        for (Object tempArg : arguments) {
            log.debug("arguments : {}", tempArg);
        }
    }

    @AfterReturning(
            pointcut = "forAppFlow()",
            returning = "theResult"
    )
    public void afterReturning(JoinPoint theJoinPoint,Object theResult){
        //display method we are returniing from
        log.trace("@After Returning from method : {}", theJoinPoint.getSignature().toShortString());
        //display data returned
        log.debug(" ==> result: {}", theResult);
    }

    @AfterThrowing(pointcut = "forAppFlow()", throwing = "theExc")
    public void afterThrowingException(JoinPoint theJoinPoint, Throwable theExc) {
        log.trace("@AfterThrowing exception on : {}", theJoinPoint.getSignature().toShortString());
        log.info(" ==> Exception Thrown: ", theExc);
    }
}
