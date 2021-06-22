package app.aop;

import org.apache.logging.log4j.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * @author Surkov Aleksey (stibium128@gmail.com)
 */
@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LogManager.getLogger(LoggingAspect.class);
    private final Marker request = MarkerManager.getMarker("REQUEST");
    private final Marker response = MarkerManager.getMarker("RESPONSE");
    private final Marker time = MarkerManager.getMarker("TIME");
    private final Marker error = MarkerManager.getMarker("ERROR");

    @Pointcut("execution(* app.controller.*.*(..)) && " +
            "!execution(* app.controller.NotificationController.*(..)) &&" +
            "!execution(* app.controller.DefaultController.getLogs(..))")
    public void applicationControllerPackage() {
        // в этом методе сопоставляем выполнение всех методов в пакете controller
        // за исключением методов в котроллерах Zaglushki, NotificationController
        // и метода getLogs в контроллере DefaultController
    }

    @Pointcut("execution(* app.*.*.*(..))") // within(app..*) конфликтует с jwt фильтром
    public void applicationExceptionPackage() {
        // в этом методе сопоставляем выполнение всех методов во всем проекте
    }

    @Around("applicationControllerPackage()")
    private Object loggingControllersAll(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info(
                request,
                "Request for {}.{}() with arguments[s] = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs()
        );

        Instant start = Instant.now();

        Object returnValue = joinPoint.proceed();
        logger.info(
                response,
                "Response for {}.{} with result = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                returnValue
        );

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        String timeString = new SimpleDateFormat("mm:ss:SSS").format(new Date(timeElapsed));

        logger.info(
                time,
                "For {}.{}() execution time = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                timeString
        );
        return returnValue;
    }

    @AfterThrowing(pointcut = "applicationExceptionPackage()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        e.printStackTrace();
        logger.error(
                error,
                "Exception in {}.{} with cause = {} , with message = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL",
                e.getMessage() != null ? e.getMessage() : "NULL"
        );
    }

}
