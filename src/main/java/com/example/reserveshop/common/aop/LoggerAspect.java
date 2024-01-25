package com.example.reserveshop.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Aspect
@Slf4j
public class LoggerAspect {

    @Autowired
    LoggingFilter loggingFilter;
    @Pointcut("execution(* com.example..*Controller.*(..))") // 이런 패턴이 실행될 경우 수행
    public void loggerPointCut() {

    }

    @Around("loggerPointCut()")
    public Object methodLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            Object result = proceedingJoinPoint.proceed();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); // request 정보를 가져온다.

            String controllerName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = proceedingJoinPoint.getSignature().getName();

            Map<String, Object> params = new HashMap<>();

            String paramters = loggingFilter.logPayload("Request", request.getContentType(), request.getInputStream());
            try {
                params.put("traceId", UUID.randomUUID().toString());
                params.put("controller", controllerName);
                params.put("method", methodName);
//                params.put("params", getParams(request));
                params.put("params",paramters);
//                params.put("log_time", new Date());
                params.put("log_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                params.put("request_uri", request.getRequestURI());
                params.put("http_method", request.getMethod());
                params.put("request_ip",request.getRemoteAddr());

            } catch (Exception e) {
                log.error("LoggerAspect error", e);
            }
            log.info("params : {}", params); // param에 담긴 정보들을 한번에 로깅한다.

            return result;

        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    /**
     * request 에 담긴 정보를 JSONObject 형태로 반환한다.
     * @param request
     * @return
     */
    private static JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }

}
