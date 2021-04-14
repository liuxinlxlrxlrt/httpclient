package com.httpclient.controller;

import com.httpclient.utils.BaseRedisService;
import com.httpclient.utils.ConstantUtils;
import com.httpclient.ext.ExtApiIdempotent;
import com.httpclient.utils.RedisToken;
import com.httpclient.entity.OrderEntity;
import com.httpclient.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisToken redisToken;

    @Autowired
    private BaseRedisService redisJsonUtils;

    @RequestMapping("/redisToken")
    public String RedisToken() {
        return redisToken.getToken();
    }

//    @RequestMapping(value = "/addOrder",produces = "application/json;charset=utf-8")
//    public String addOrder(@ResponseBody OrderEntity orderEntity, HttpServletRequest request){
//        /**
//         * 如何使用Token解决幂等问题
//         *
//         * 步骤
//         * 1、在调用接口之前生成对应的令牌（Token），存放在redis
//         * 2、将调用接口的时候，将令牌（Token）放在请求头中(获取请求头的中令牌)
//         * 3、接口获取对应的令牌
//         * （1）、如果能获取到该令牌的情况下（将该令牌删除掉），执行该方法访问的业务逻辑
//         * （2）、如果没有获取到该令牌，就直接访问请勿重复提交
//         */
//
//        //2、将调用接口的时候，将令牌（Token）放在请求头中(获取请求头的中令牌)
//        String token = request.getHeader("token");
//        if (token==null){
//            return "参数错误";
//        }
//        //3、接口获取对应的令牌
//        //（1）、如果能获取到该令牌的情况下（将该令牌删除掉），执行该方法访问的业务逻辑
//        boolean isToken = redisToken.findToken(token);
//
//        //（2）、如果没有获取到该令牌，就直接访问请勿重复提交
//        if (!isToken){
//            return "请勿重复提交";
//        }
//        //5、存放到数据库中
//        return orderMapper.insertOrder(orderEntity)>0?"success":"fail";
//    }

    @RequestMapping(value = "/addOrder", produces = "application/json;charset=utf-8")
    @ExtApiIdempotent(type = ConstantUtils.EXTAPIHEAD)
    public String addOrder(OrderEntity orderEntity, HttpServletRequest request) {
        return orderMapper.insertOrder(orderEntity) > 0 ? "success" : "fail";
    }

    /**
     * 1、如何解决token解决RPC幂等问题产生的bug
     * （第一次失败了，第二次重试也应该能执行添加，而不是说你限制不管你重试多少次，我接口只能执行一次）
     *
     * 2、如何保证接口的幂等性，并且保证第一次添加失败了，第二次再添加一遍？
     * 基于唯一ID关联业务逻辑解决幂等问题，不应该只是简单使用redis中的token
     * 表单提交的情况下，这个时间采用redis+token
     *
     * 步骤：
     * 1、先根据全局id查询数据是否有插入成功
     * （数据库中添加token唯一字段（添加主键））
     * 2、如果数据没有插入成功，则继续插入
     * 3、如果数据已经插入成功，就返回已经处理业务逻辑
     * @param orderEntity
     * @param request
     * @return
     */
    @RequestMapping(value = "/addOrder", produces = "application/json;charset=utf-8")
    public String addOrderUpdate(OrderEntity orderEntity, HttpServletRequest request) {
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

        //2、将调用接口的时候，将令牌（Token）放在请求头中(获取请求头的中令牌)
        String token = request.getHeader("token");
        if (token == null) {
            return "参数错误";
        }
        //3、接口获取对应的令牌
        //（1）、如果能获取到该令牌的情况下（将该令牌删除掉），执行该方法访问的业务逻辑
        boolean isToken = redisToken.findToken(token);

        //（2）、如果没有获取到该令牌，就直接访问请勿重复提交
        if (!isToken) {
            return "请勿重复提交";
        }
        //5、存放到数据库中
        return orderMapper.insertOrder(orderEntity) > 0 ? "success" : "fail";
    }
}
