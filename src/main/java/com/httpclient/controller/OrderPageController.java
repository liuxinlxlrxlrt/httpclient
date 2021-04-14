package com.httpclient.controller;

import com.httpclient.entity.OrderEntity;
import com.httpclient.ext.ExtApiToken;
import com.httpclient.mapper.OrderMapper;
import com.httpclient.utils.BaseRedisService;
import com.httpclient.utils.ConstantUtils;
import com.httpclient.ext.ExtApiIdempotent;
import com.httpclient.utils.RedisToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class OrderPageController {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisToken redisToken;

    @Autowired
    private BaseRedisService redisJsonUtils;

    @RequestMapping("/indexPage")
    @ExtApiToken
    public String  RedisToken(HttpServletRequest request){
        return "indexPage";
    }


    @RequestMapping(value = "/addOrderPage")
    @ExtApiIdempotent(type = ConstantUtils.EXTAPIFORM)
    public String addOrder(OrderEntity orderEntity){
        return orderMapper.insertOrder(orderEntity)>0?"success":"fail";
    }
}
