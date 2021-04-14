package com.httpclient.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * 2、Token如何生成？
 * Token_时间戳==分布式场景问题
 */
public class RedisToken {

    private static final long TOKENTIMEOUT = 60*60;

    @Autowired
    private BaseRedisService baseRedisService;


    //synchronized保证线程单一执行
   public synchronized String getToken(){
       //使用Token（令牌）保证临时且唯一（一般15分钟-120分钟），不支持分布式场景，分布式全局id生成规则
//       String token = "token"+System.currentTimeMillis();//不安全，有可能相同
       //使用uuid,也不适合分布式
       String token = "token"+ UUID.randomUUID();
        //将token添加到redis中
       baseRedisService.setString(token,token,TOKENTIMEOUT);

       return token;
   }
    /**
     * 如何使用Token解决幂等问题
     *
     * 步骤
     * 1、在调用接口之前生成对应的令牌（Token），存放在redis
     * 2、将调用接口的时候，将令牌（Token）放在请求头中(获取请求头的中令牌)
     * 3、接口获取对应的令牌
     * （1）、如果能获取到该令牌的情况下（将该令牌删除掉），执行该方法访问的业务逻辑
     * （2）、如果没有获取到该令牌，就直接访问请勿重复提交
     */
   public synchronized boolean findToken(String tokenKey){
        //3、接口获取对应的令牌
        //（1）、如果能获取到该令牌的情况下（将该令牌删除掉），执行该方法访问的业务逻辑
       String tokenValue = (String) baseRedisService.getString(tokenKey);
       if (StringUtils.isEmpty(tokenValue)){
           return false;
       }
       baseRedisService.delKey(tokenValue);
       return true;

   }
//测试
//    public static void main(String[] args) {
//        RedisToken redisToken =  new RedisToken();
//       for (int i=0;i<10;i++){
//           System.out.println(RedisToken.getToken());
//       }
//    }
}

