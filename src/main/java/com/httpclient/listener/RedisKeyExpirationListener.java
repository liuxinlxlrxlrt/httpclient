package com.httpclient.listener;

import com.httpclient.entity.OrderEntity;
import com.httpclient.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;


@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private OrderMapper orderMapper;

   public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer){
        super(listenerContainer);
   }
    //待支付
   private static final Integer ORDER_STAYPAY=0;

   //失效
   private static  final Integer ORDER_INVALIED=2;

    /**
     * 使用该方法建监听，当我们的key失效时执行该方法
     * @param message
     * @param pattern
     */
   @Override
   public void onMessage(Message message, byte[] pattern){
       String expireKey = message.toString();
       System.out.println("该key：expireKey："+expireKey+"失效啦~");
       // 前缀判断 - 每个服务，前缀事先商量好，防止冲突 eg:orderToken_XXXXXXX  // 或者是根据库区分
       //如果orderNumber==null，说明不在这个表中
       OrderEntity orderNumber = orderMapper.getOrderNumber(expireKey);
       if(orderNumber==null){
         return;
       }
       //获取订单状态
       Integer orderStatus = orderNumber.getOrderStatus();
       if (orderStatus.equals(ORDER_STAYPAY)){
           orderMapper.updateOrderStatus(expireKey,ORDER_INVALIED);
           //库存加上1
       }
       // 不设置超时时间，但是可能被淘汰回收策略回收掉，也会走回调方法
   }
}
