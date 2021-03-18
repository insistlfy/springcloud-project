package org.lfy.first.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * ActionAspect
 * ① @Aspect ： 声明该类是一个注解类；
 * ② @Pointcut ： 定义一个切点，后面可以跟随一个表达式，表达式可以定义为某个package下的方法，也可以是自定义的注解；
 * ③ @Before ：在切点之前，织入相关代码
 * ④ @After ： 在切点之后，织入相关代码
 * ⑤ @AfterReturning ：在切点返回内容后，织入相关代码，一般用于对返回值做加的场景；
 * ⑥ @AfterThrowing ：用来处理当织入的代码抛出异常后的逻辑处理;
 * ⑦ @Around ：在切入点前后织入代码，并且可以自由的控制何时执行切点；
 *
 * @author lfy
 * @date 2021/3/18
 **/
@Slf4j
@Aspect
@Component
public class ActionAspect {

    @Pointcut(value = "execution(* org.lfy.*.api..*.*Controller..*(..))")
    public void aspect() {
    }

    @Before("aspect()")
    public void before(JoinPoint point) {

        // start print request logs
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //print params
        log.info("========================Start========================");
        //print url
        log.info("URL    : 【{}】", request.getRequestURL().toString());
        //print http method
        log.info("HTTP Method : 【{}】", request.getMethod());
        //print Controller fullPath and Method Name
        log.info("Class Method : 【{}.{}】", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
        //print IP
        log.info("IP     : 【{}】", request.getRemoteAddr());
        //print request params
        log.info("Request Args :【{}】", JSON.toJSONString(point.getArgs()));

    }

    @Around("aspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        // print return params
        log.info("Response Args : 【{}】", JSON.toJSONString(result));
        // request cost time
        log.info("Cost Time : 【{}】ms", System.currentTimeMillis() - startTime);
        return result;
    }

    @After("aspect()")
    public void after() {
        log.info("========================End========================");
        log.info("");
    }
}
