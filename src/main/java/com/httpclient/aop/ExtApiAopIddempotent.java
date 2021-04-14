package com.httpclient.aop;

import com.httpclient.ext.ExtApiToken;
import com.httpclient.utils.ConstantUtils;
import com.httpclient.ext.ExtApiIdempotent;
import com.httpclient.utils.RedisToken;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Aspect
@Component
public class ExtApiAopIddempotent {

    @Autowired
    private RedisToken redisToken;

    /**
     * 1、使用AOP环绕通知拦截所有访问（controler）
     */
    @Pointcut("execution(public *com.httpclient.controler.*.*(..))")
    public void rlAop() {

    }

    @Before("rlAop()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        ExtApiToken extApiToken = methodSignature.getMethod().getDeclaredAnnotation(ExtApiToken.class);
        if (extApiToken != null) {
            //可以放在AOP代码中，放在前置通知
            getRequest().setAttribute("token", redisToken.getToken());
        }
    }

    /**
     * 环绕通知
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("rlAop()")
    public Object dobefore(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //1、使用AOP环绕通知拦截所有访问（controler）
        //2\判断方法上是否加ExtApiIdempotent
        /**
         * 获取切入点方法的名字
         * getSignature());是获取到这样的信息 :修饰符+ 包名+组件名(类名) +方法名
         *   //获取方法签名(通过此签名获取目标方法信息)
         */
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        //获取注解
        ExtApiIdempotent declareAnnotion = methodSignature.getMethod().getDeclaredAnnotation(ExtApiIdempotent.class);
        //3、判断方法上加ExtApiIdempotent
        if (declareAnnotion != null) {
            //标识是否表单获取的token还是从请求头获取的token
            String type = declareAnnotion.type();
            //如何使用Token解决幂等问题
            //步骤
            //2、将调用接口的时候，将令牌（Token）放在请求头中(获取请求头的中令牌)
            String token = null;
            HttpServletRequest request = getRequest();
            if (type.equals(ConstantUtils.EXTAPIFORM)) {
                token = request.getHeader("token");
            } else {
                token = request.getParameter("token");
            }

            if (StringUtils.isEmpty(token)) {
                return "参数错误";
            }
            //3、接口获取对应的令牌
            //（1）、如果能获取到该令牌的情况下（将该令牌删除掉），执行该方法访问的业务逻辑
            boolean isToken = redisToken.findToken(token);

            //（2）、如果没有获取到该令牌，就直接访问请勿重复提交
            if (!isToken) {
                response("请勿重复提交");
                //表示后面方法不在执行
                return null;
            }
            //放行
            Object proceed = proceedingJoinPoint.proceed();
            return proceed;
        }
        return null;
    }

    /**
     * AOP中获取request对象
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    /**
     * AOP中获取response对象
     *
     * @param msg
     * @throws IOException
     */
    public void response(String msg) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try {
            writer.println(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
